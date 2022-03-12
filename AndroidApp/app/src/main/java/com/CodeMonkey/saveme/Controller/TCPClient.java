package com.CodeMonkey.saveme.Controller;

import android.util.Log;

import com.CodeMonkey.saveme.Util.URLUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;


/***
 * TCPClient created by Wang Tianyu 11/03/2022
 * TCP client(Still cannot working)
 */
public class TCPClient {

    public static Socket socket;

    public static void startClient(){
        if (socket == null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.i("tcp", "Client start");
                        socket = new Socket(URLUtil.tcpIP, URLUtil.tcpPort);
                        Log.i("tcp", "Connect successfully");
                        PrintWriter pw = new PrintWriter(socket.getOutputStream());

                        InputStream inputStream = socket.getInputStream();

                        byte[] buffer = new byte[1024];
                        int len = -1;
                        while (true) {
                            if ((len = inputStream.read(buffer)) != -1){
                                String data = new String(buffer, 0, len);
                                Log.i("tcp", "Receive data---------------------------------------------:" + data);
                            }
//                            EventBus.getDefault().post(new MessageClient(data));
                        }
//                        pw.close();

                    } catch (Exception EE) {
                        EE.printStackTrace();
                        Log.i("tcp", "Connect failed");

                    }finally {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        socket = null;
                    }
                }
            }).start();
        }
    }

    public static void sendTcpMessage(final String msg){
        if (socket != null && socket.isConnected()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        socket.getOutputStream().write(msg.getBytes());
                        socket.getOutputStream().flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
