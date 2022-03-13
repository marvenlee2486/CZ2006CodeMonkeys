import psycopg2
import asyncio
import datetime
from aiohttp import web
from threading import Thread
from math import *
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
    onlineUsers = {} # handle: writer 
    async def run(self):
        self.server = await asyncio.start_server(self.clientHandler, '127.0.0.1', 3392)
        async with self.server:
            await self.server.serve_forever()

    async def clientHandler(self, reader, writer):
        while True:
            try:
                msg = (await reader.read(1024)).decode('utf-8')
                print("User location info received: ", msg)
                # 1. Validate User
                [username, cred, lat, lon] = msg.split(",")
                if not userValidator.checkCredentials(username, cred):
                    continue

                # 2. if user is not recognized, add it to list
                if username not in self.onlineUsers:
                    self.onlineUsers[username] = writer

                # 3. update location
                currTime = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
                db.do("UPDATE Users SET LocLat = '%s', LocLon = '%s', lastOnLineTime = '%s' WHERE name = '%s'" % (lat, lon, currTime, username))
                print("User location updated.")
            except:
                print("Read socket data fail. Possibly malformed user data?")

tcpdaemon = TCPManager()

class HTTPManager():
    async def handle(request):


class HTTPManagerObsolete(BaseHTTPRequestHandler): # perform all HTTP requests
    def _set_response(self):
        self.send_response(200)
        self.send_header('Content-type', 'text/plain')
        self.end_headers()

    def do_POST(self): # reply to requests
        post_data = self.rfile.read(int(self.headers['Content-Length']) ).decode('utf-8')

    def errorResponse(self):
        self.wfile.write("ERROR".encode('utf-8'))

if __name__ == "__main__":
    Thread(target=tcpdaemon.run).start()

    daemon = HTTPServer(('', 3393), HTTPManager)
    daemon.serve_forever()