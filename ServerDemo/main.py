from geotool import *
from dbConnector import dbConnector, initDB
from tcpManager import TCPManager, initTCP, initiateSaveMeRequest, tcpdaemon, onlineEvents
from websocketManager import websocketsManager, initWebsocket

if __name__ == "__main__":
    initDB(); initTCP(); initWebsocket()
    