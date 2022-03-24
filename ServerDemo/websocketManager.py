import websockets
import asyncio
import json

class websocketsManager:
    @staticmethod
    async def reply(ws):
        print("new ws client connected.")
        while True:
            dummyLocation = [{"latitude": 1.0, "longitude": 1.0, "timeStarted": datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S"), "victim": "foo", "respondedVolunteers": 1}]
            await ws.send(json.dumps(dummyLocation))
            print("Location updated.")
            await asyncio.sleep(10)
            
    @staticmethod
    async def run():
        print("websocket server is up.")
        while True:
            async with websockets.serve(websocketsManager.reply, "0.0.0.0", 3392):
                await asyncio.Future()

def initWebsocket():
    asyncio.run(websocketsManager.run())