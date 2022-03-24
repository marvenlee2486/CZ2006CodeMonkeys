from geotool import *
from dbConnector import initDB
from tcpManager import initTCP
from websocketManager import initWebsocket

if __name__ == "__main__":
    initDB(); initTCP(); initWebsocket()
    