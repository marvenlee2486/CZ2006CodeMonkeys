import socket
import datetime 
from threading import Thread
from dbConnector import db
from geotool import geoDistance


class TCPManager: # perform all TCP Requests
    sck = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    connectedUsers = {} # username: socketHandle
    userLocs = {} # username: (lat, lon)

    def run(self):
        print("TCPManager started.")
        self.sck.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        self.sck.bind(('0.0.0.0', 3391))
        self.sck.listen()
        while True:
            newSck, addr = self.sck.accept()
            print("new TCP client.")
            Thread(target=TCPManager.clientLocationHandler, args=(self, newSck, addr)).start()
        self.sck.close()

    def clientLocationHandler(self, clientSocket, addr): # receives user location
        while True:
            try:
                msg = clientSocket.recv(1024).decode('utf-8')
                if not msg: break
                clientSocket.send(("I received: " + msg).encode('utf-8'))
                print("User location info received: ", msg)
                [username, lat, lon] = msg.split(";")

                # if user is not recognized, add it to list
                if username not in self.connectedUsers:
                    self.connectedUsers[username] = clientSocket
                    self.userLocs[username] = (lat, lon)
                
                # 2. update location
                currTime = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
                db.do("UPDATE Users SET LocLat = '%s', LocLon = '%s', lastOnLineTime = '%s' WHERE name = '%s'" % (lat, lon, currTime, username))
                print("User location updated.")
                clientSocket.send("ojbk".encode('utf-8'))
            except:
                print("Malformed input data on IO::onNewClient.")

    def initiateSaveMeRequest(self):
        data = [] # TODO connect with Lambda for data
        if (data[0] == "ACHIEVEMENTS"): # Query user achievements: ACHIEVEMENTS user cred
            a = db.query("SELECT nBeingSaved, keepOpen, nSaved FROM Users WHERE name = '%s'" % data[1])[0]
            print("Achievement request handled for user = %s." % (data[1]))
            self.wfile.write(("%s, %s, %s" % (a[0], a[1], a[2])).encode('utf-8'))
        
        elif (data[0] == "RESCUE"): # rescue request: RESCUE user cred loclat loclon => sendmsg
            loclat = data[3]; loclon = data[4]
            (homeLat, homeLon, name, telephone, medicalinfo) = db.query("SELECT homeLoc, homeLon, name, telephone, medicalInfo FROM Users WHERE name = '%s'" % data[1])[0]
            (homeAddrDesc) = db.query("SELECT homeDesc FROM Users WHERE name = '%s'" % data[1])[0]
            
            # compose emergency message
            message = ['REQUEST', name, telephone, medicalinfo, loclat, loclon]
            # if location is near home, set to home
            if geoDistance(float(homeLat), float(homeLon), float(loclat), float(loclon)) < 100.0:
                message.append(homeAddrDesc)
            else:
                message.append("NA")

            # filter available individuals
            availableVolunteers = db.query("SELECT name, lastOnLineTime, LocLat, LocLon FROM Users WHERE (LocLat - %s) * (LocLat - %s) + (LocLon - %s) * (LocLon - %s) <= 0.04 AND name != '%s' AND isVolunteer = true" % (loclat, loclat, loclon, loclon, name)) # ref: https://www.usna.edu/Users/oceano/pguth/md_help/html/approx_equivalents.htm
            cnt = 0
            for (name, onlineTime, volunteerLat, volunteerLon) in availableVolunteers:
                print("  %s is a candidate rescuer. Distance = %f meters." % (name, geoDistance(float(volunteerLat), float(volunteerLon), float(loclat), float(loclon))))
                if name in tcpdaemon.connectedUsers:
                    tcpdaemon.sendRequest(tcpdaemon.connectedUsers[name], ','.join(message))
                    cnt += 1
            print("A new emergency request received. %d volunteers online. %d volunteers fulfilled requirement." % (len(tcpdaemon.connectedUsers), cnt))
            td = {"latitude": loclat, "longitude": loclon, "timeStarted": datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S"), "victim": name, "respondedVolunteers": cnt} # should not be cnt TODO
            onlineEvents.append(td)

def initTCP():
    global tcpdaemon, onlineEvents
    tcpdaemon = TCPManager()
    onlineEvents = []
    Thread(target=tcpdaemon.run).start() # 3391