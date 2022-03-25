from geotool import *
import dbConnector
import tcpManager
import websocketManager

if __name__ == "__main__":
    dbConnector.initDB() 
    tcpManager.initTCP()
    websocketManager.initWebsocket()
    
