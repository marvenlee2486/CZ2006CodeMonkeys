import websockets
import asyncio
import json
import datetime
import rescueManager
class websocketsManager:
    @staticmethod
    async def run():
        print("websocket server is up.")
        while True:
            try:
                async with websockets.connect("wss://chfo8hmaua.execute-api.ap-southeast-1.amazonaws.com/production") as websocket:
                    await websocket.send('''{"action":"setName","name":"laganma"}''') # auth
                    while True:
                        extractedEvents = []
                        for tel in rescueManager.rescueDaemon.events:
                            x = rescueManager.rescueDaemon.events[tel]
                            te = {"patientTel": tel, "patientLat": x.patientLat, "patientLon": x.patientLon, "accept": x.accept, "decline": x.decline, "informed": x.informed, "startTime": x.time, "endTime": x.endTime} 
                            extractedEvents.append(te)
                        obj = {"action": "sendPrivate", "to": "admin", "message": extractedEvents}
                        print("Websocket: reporting %s" % (json.dumps(obj)))
                        await websocket.send(json.dumps(obj))
                        await asyncio.sleep(15)
            except:
                print("temporarily lost connection to websocket broadcaster. try to reconnect.")
def initWebsocket():
    asyncio.run(websocketsManager.run())
