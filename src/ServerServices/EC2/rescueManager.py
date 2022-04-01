from dataclasses import dataclass
import socket
import datetime 
import typing
from threading import Thread
from geotool import geoDistance
ALARM_DISTANCE_THRESHOLD = 5000

@dataclass
class Event:
    patientSck: socket.socket
    accept: set[str]
    decline: set[str]
    informed: set[str]
    patientLat: str
    patientLon: str
    time: str

@dataclass
class ConnectedUser:
    sck: socket.socket
    lat: str
    lon: str

class rescueManager: 
    sck = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    connectedUsers: dict[str, ConnectedUser] = {} # telephone: (socketHandle, lat, lon); notice this only stores connected volunteers; socket for connected patients due to emergency are stored in events
    events: dict[str, Event] = {}
    def run(self):
        print("TCPManager started.")
        self.sck.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        self.sck.bind(('0.0.0.0', 3391))
        self.sck.listen()
        while True:
            newSck, addr = self.sck.accept()
            print("new TCP client.")
            Thread(target=rescueManager.clientRequestHandler, args=(self, newSck, addr)).start()
    
    def clientRequestHandler(self, clientSocket, addr): # receives user location
        while True:
            if True:
                msg = clientSocket.recv(1024).decode('utf-8')
                if not msg: break
                print("Data received: ", msg)

                if msg.startswith('ACCEPTREQ'):
                    [mode, patientTel, volTel] = msg.split(";")
                    if volTel in self.events[patientTel].decline: self.events[patientTel].decline.remove(volTel)
                    self.events[patientTel].accept.add(volTel)
                    self.updateIncomingAmount(patientTel)

                elif msg.startswith('LOCATION'): # new update to location
                    [mode, tel, lat, lon] = msg.split(";")
                    if tel == 'null': continue
                    self.connectedUsers[tel] = ConnectedUser(clientSocket, lat, lon)
                    for patientTel in self.events: # see if there is anything he can do
                        tp = self.events[patientTel] # current event
                        if geoDistance(tp.patientLat, tp.patientLon, lat, lon) <= ALARM_DISTANCE_THRESHOLD and tel not in tp.informed:
                                tp.informed.add(tel) # add to informed
                                message = ['REQUEST', patientTel, tp.patientLat, tp.patientLon]
                                clientSocket.send(';'.join(message).encode('utf-8'))
                    
                    print("User location updated.")

                elif msg.startswith('DECLINEREQ'):
                    [mode, patientTel, volTel] = msg.split(";")
                    if volTel in self.events[patientTel].accept: self.events[patientTel].accept.remove(volTel)
                    
                    self.events[patientTel].decline.add(volTel)
                    self.updateIncomingAmount(patientTel)
                
                elif msg.startswith('RESCUEME'):
                    [mode, telephone, lat, lon] = msg.split(';')
                    # create new event entry, overwriting previous one if any
                    if telephone in self.events:
                        print("Warning: previous event of same patient not closed, overwriting previous event")
                    self.events[telephone] = Event(clientSocket, accept = set(), decline = set(), informed = set(), patientLat = lat, patientLon = lon, time = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S"))
                    # compose emergency message
                    message = ['REQUEST', telephone, lat, lon]
                    # # if location is near home, set to home
                    # if geoDistance(float(homeLat), float(homeLon), float(lat), float(lon)) < 100.0:
                    #     message.append(homeAddrDesc)
                    # else:
                    #     message.append("NA")
                    # filter available individuals
                    numAvailable = 0
                    for tel in self.connectedUsers:
                        tp = self.connectedUsers[tel]
                        if geoDistance(tp.lat, tp.lon, lat, lon) <= ALARM_DISTANCE_THRESHOLD: 
                            tp.sck.send(';'.join(message).encode('utf-8'))
                            numAvailable += 1
                    print("A new emergency request received. %d volunteers online. %d volunteers fulfilled requirement." % (len(self.connectedUsers), numAvailable))
                elif msg.startswith('CANCELRESCUEME'): # either finish or manual cancel TODO delete entry and report
                    [mode, telephone] = msg.split(';')
                    if telephone not in self.events:
                        print("Unexpected: client try to cancel a nonexist event.")
                    else:
                        for tTel in self.events[telephone].informed:
                            try: self.connectedUsers[telephone].sck.send(msg.encode('utf-8'))
                            except: print("fail to send cancel operation info for one of the volunteers. error is ignored.")   
                else:
                    print("Malformed input data. Missing request type.")

    def updateIncomingAmount(self, tel): # update detail to everyone informed
        message = ['UPDATERESCUERS', tel, len(self.events[tel].accept)] 
        for tTel in self.events[tel].informed:
            try: self.connectedUsers[tTel].sck.send(';'.join(message).encode('utf-8'))
            except: print("fail to reach one of the volunteers while updating information. error is ignored.")
    
def initTCP():
    global rescueDaemon, onlineEvents
    rescueDaemon = rescueManager()
    onlineEvents = []
    Thread(target=rescueDaemon.run).start() # 3391
