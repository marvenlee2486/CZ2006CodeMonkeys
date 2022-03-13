import psycopg2
import socket
import http.server
import socketserver
import datetime 
from http.server import BaseHTTPRequestHandler, HTTPServer
from threading import Thread
from math import *
import websockets
import asyncio
# username must be unique
# username & crendential must not have special characters etc. especially,


def geoDistance(lat1, lon1, lat2, lon2): # return geometry distance of 2 points on earth
    R = 6.3781e6
    dlon = lon2 - lon1
    dlat = lat2 - lat1

    a = sin(dlat / 2)**2 + cos(lat1) * cos(lat2) * sin(dlon / 2)**2
    c = 2 * atan2(sqrt(a), sqrt(1 - a))

    return R * c

class dbConnector:
    def connect(self):
        self.conn = psycopg2.connect("dbname=saveMeDB user=postgres password=postgres")
        self.cur = self.conn.cursor()

        self.cur.execute('SELECT version()')
        print(self.cur.fetchall())
    
    def query(self, sql):
        self.cur.execute(sql)
        self.conn.commit()
        return self.cur.fetchall()

    def do(self, sql):
        self.cur.execute(sql)
        self.conn.commit()

    def close(self):
        self.cur.close()
        self.conn.close()
    
db = dbConnector()
db.connect()

class userValidator:
    @staticmethod
    def checkUserExistence(username):
        s = ("SELECT * FROM Users WHERE name = '%s'" % username)
        return db.query(s) != []

    @staticmethod
    def checkCredentials(username, cache):
        print("Verify user id = %s, crd = %s" % (username, cache))
        if not userValidator.checkUserExistence(username):
            return False
        s = ("SELECT credential FROM Users WHERE name = '%s'" % username)
        if db.query(s)[0][0] == cache:
            print("Verification passed.")
        else:
            print("Verification failed.")
        return db.query(s)[0][0] == cache # it always uniquely exist.

class TCPManager: # perform all TCP Requests
    sck = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    connectedUsers = {} # username: socketHandle

    def run(self):
        self.sck.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        self.sck.bind(('127.0.0.1', 3391))
        self.sck.listen()
        while True:
            newSck, addr = self.sck.accept()
            Thread(target=TCPManager.clientLocationHandler, args=(self, newSck, addr)).start()
        self.sck.close()

    def clientLocationHandler(self, clientSocket, addr): # receives user location
        while True:
            try:
                msg = clientSocket.recv(1024).decode('utf-8')
                print("User location info received: ", msg)
                # 1. Validate User
                [username, cred, lat, lon] = msg.split(",")
                if not userValidator.checkCredentials(username, cred):
                    continue

                # if user is not recognized, add it to list
                if username not in self.connectedUsers:
                    self.connectedUsers[username] = clientSocket

                # 2. update location
                currTime = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
                db.do("UPDATE Users SET LocLat = '%s', LocLon = '%s', lastOnLineTime = '%s' WHERE name = '%s'" % (lat, lon, currTime, username))
                print("User location updated.")
            except:
                print("Malformed input data on IO::onNewClient.")

    def sendRequest(sck, msg):
        sck.send(msg.encode('utf-8'))

tcpdaemon = TCPManager()

class HTTPManager(BaseHTTPRequestHandler): # perform all HTTP requests
    def _set_response(self):
        self.send_response(200)
        self.send_header('Content-type', 'text/plain')
        self.end_headers()

    def do_POST(self): # reply to requests
        post_data = self.rfile.read(int(self.headers['Content-Length']) ).decode('utf-8')
        data = post_data.split(",")

        self._set_response()

        if not userValidator.checkCredentials(data[1], data[2]): # 1. validate user
            self.errorResponse()
            return

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
            availableVolunteers = db.query("SELECT name, lastOnLineTime, LocLat, LocLon FROM Users WHERE (LocLat - %s) * (LocLat - %s) + (LocLon - %s) * (LocLon - %s) <= 0.00203 AND name != '%s' AND isVolunteer = true" % (loclat, loclat, loclon, loclon, name)) # ref: https://www.usna.edu/Users/oceano/pguth/md_help/html/approx_equivalents.htm
            cnt = 0
            for (name, onlineTime, volunteerLat, volunteerLon) in availableVolunteers:
                print("  %s is a candidate rescuer. Distance = %f meters." % (name, geoDistance(float(volunteerLat), float(volunteerLon), float(loclat), float(loclon))))
                if name in tcpdaemon.connectedUsers:
                    tcpdaemon.sendRequest(tcpdaemon.connectedUsers[name], ','.join(message))
                    cnt += 1
            print("A new emergency request received. %d volunteers online. %d volunteers fulfilled requirement." % (len(tcpdaemon.connectedUsers), cnt))
        
    def errorResponse(self):
        self.wfile.write("ERROR".encode('utf-8'))

class websocketsManager:
    @staticmethod
    async def reply(ws):
        while True:
            await asyncio.sleep(5)
            await ws.send('testData')

    @staticmethod
    async def run():
        while True:
            async with websockets.serve(websocketsManager.reply, "localhost", 3392):
                await asyncio.Future()


if __name__ == "__main__":
    # Thread(target=tcpdaemon.run).start() # 3391

    # daemon = HTTPServer(('', 3393), HTTPManager)
    # daemon.serve_forever()
    asyncio.run(websocketsManager.run())
