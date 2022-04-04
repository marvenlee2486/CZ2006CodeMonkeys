import websockets
import asyncio
import json
import datetime
import rescueManager
class websocketsManager:
    @staticmethod
    async def run():
        print("websocket server is up.")
        if True:
            if True:
                async with websockets.connect("wss://chfo8hmaua.execute-api.ap-southeast-1.amazonaws.com/production") as websocket:
                    # await websocket.send('''{"action":"setName","name":"laganma"}''') # greet
                    while True:
                        extractedEvents = []
                        for tel in rescueManager.rescueDaemon.events:
                            x = rescueManager.rescueDaemon.events[tel]
                            te = {"patientTel": tel, "patientLat": x.patientLat, "patientLon": x.patientLon, "accept": [rescueManager.rescueDaemon.connectedUsers[tTel].name for tTel in x.accept], "decline": [rescueManager.rescueDaemon.connectedUsers[tTel].name for tTel in x.decline], "informed": [rescueManager.rescueDaemon.connectedUsers[tTel].name for tTel in x.informed], "startTime": x.time, "endTime": x.endTime} 
                            extractedEvents.append(te)
                        print("Websocket: reporting %s" % (json.dumps({"message": extractedEvents})))
                        #locations = {}
                        #for x in rescueManager.rescueDaemon.connectedUsers:
                        #    try:
                        #        User = rescueManager.rescueDaemon.connectedUsers[x]
                        #        ocations[x] = (tUser.lat, tUser.lon)
                        #    except:
                        #        print("skipped one user in generating report
                        await websocket.send(json.dumps({"message": extractedEvents, "locations": locations}))
                        await asyncio.sleep(15)
def initWebsocket():
    asyncio.run(websocketsManager.run())
