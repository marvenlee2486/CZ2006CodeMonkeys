import socket
import datetime 
from threading import Thread

from matplotlib.style import available
from dbConnector import db
from geotool import geoDistance
ALARM_DISTANCE_THRESHOLD = 5000
# we don't dynamically maintain who we send request to.

class rescueManager: # perform all TCP Requests
    sck = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    connectedUsers = {} # telephone: (socketHandle, lat, lon)
    events = {} # patientId: (patientSck, [accept], [decline], patientLat, patientLon, time)
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
            try:
                msg = clientSocket.recv(1024).decode('utf-8')
                if not msg: break
                print("User location info received: ", msg)

                if msg.startswith('ACCEPTREQ'):
                    [mode, patientTel, volTel] = msg.split(";")
                    self.events[patientTel][0].append(volTel)
                    self.updateIncomingAmount(patientTel)

                elif msg.startswith('LOCATION'):
                    [mode, tel, lat, lon] = msg.split(";")
                    self.connectedUsers[tel] = (clientSocket, lat, lon)
                    print("User location updated.")

                elif msg.startswith('DECLINEREQ'):
                    [mode, patientTel, volTel] = msg.split(";")
                    self.events[patientTel][1].append(volTel)
                    self.updateIncomingAmount(patientTel)
                
                elif msg.startswith('RESCUEME'):
                    [mode, name, telephone, lat, lon] = msg.split(';')
                    # create new event entry
                    self.events[telephone] = (clientSocket, [], [], lat, lon, datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S"))
                    # compose emergency message
                    message = ['REQUEST', name, telephone, lat, lon]
                    # # if location is near home, set to home
                    # if geoDistance(float(homeLat), float(homeLon), float(lat), float(lon)) < 100.0:
                    #     message.append(homeAddrDesc)
                    # else:
                    #     message.append("NA")
                    # filter available individuals
                    numAvailable = 0
                    for tel in self.connectedUsers:
                        (tsck, tlat, tlon) = self.connectedUsers[tel]
                        if geoDistance(tlat, tlon, lat, lon) <= ALARM_DISTANCE_THRESHOLD: 
                            tsck.send(';'.join(message))
                            numAvailable += 1
                    print("A new emergency request received. %d volunteers online. %d volunteers fulfilled requirement." % (len(self.connectedUsers), numAvailable))
            
                else:
                    print("Malformed input data. Missing request type.")
            except:
                print("Malformed input data. Missing request argument.")

    def updateIncomingAmount(self, tel): # update everyone involved in event an update of number of volunteers participated, event is characterised by telephone of patient.
        message = ['UPDATERESCUERS', len(self.events[tel][1]), len(self.events[tel][2])]
        for tTel in self.events[tel][1]:
            try:
                self.connectedUsers[tTel][0].send(';'.join(message))
            except:
                print("Unable to push update to a certain volunteer. the error is ignored.")
        self.events[tel][0].send(';'.join(message)) # update patient himself
        
def initTCP():
    global rescueDaemon, onlineEvents
    rescueDaemon = rescueManager()
    onlineEvents = []
    Thread(target=rescueDaemon.run).start() # 3391