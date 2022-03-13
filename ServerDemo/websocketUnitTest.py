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

class websocketsManager:
    @staticmethod
    async def reply(ws):
        while True:
            await asyncio.sleep(5)
            await ws.send('yup you got this')

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
