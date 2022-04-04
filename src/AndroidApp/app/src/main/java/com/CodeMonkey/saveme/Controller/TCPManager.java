package com.CodeMonkey.saveme.Controller;


import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.CodeMonkey.saveme.Util.LocationUtils;
import com.CodeMonkey.saveme.Util.URLUtil;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/***
 * TCPManager created by Wang Tianyu 10/03/2022
 * Singleton manager for tcp
 */
public class TCPManager{

    private static final String TAG = TCPManager.class.getSimpleName();
    private volatile static TCPManager mTCPManager;
    private InputStream mInputStream;
    private OutputStream mOutputStream;
    private Socket mSocket;
    private byte[] mBuffer;
    private boolean isRunning = true;
    private Thread mThread;
    private Handler handler;

    private TCPManager(Handler handler){
        this.handler = handler;
        connect();
    }

    private TCPManager(){
        connect();
    }

    public static TCPManager getTCPManager() {
        if (mTCPManager == null) {
            synchronized (TCPManager.class) {
                if (mTCPManager == null) {
                    mTCPManager = new TCPManager();
                }
            }
        }
        return mTCPManager;
    }

    public static TCPManager getTCPManager(Handler handler) {
        if (mTCPManager == null) {
            synchronized (TCPManager.class) {
                if (mTCPManager == null) {
                    mTCPManager = new TCPManager(handler);
                }
            }
        }
        return mTCPManager;
    }

    private void connect() {
        if (mSocket != null)
            return;
        new Thread() {
            @Override
            public void run() {
                try {
                    mSocket = new Socket(URLUtil.tcpIP, URLUtil.tcpPort);
                    if (mSocket != null) {
                        mOutputStream = mSocket.getOutputStream();
                        mInputStream = mSocket.getInputStream();
//                        receive(mSocket);
                        DataInputStream input = new DataInputStream(mInputStream);
                        while (true) {
                            if (mSocket.isConnected()) {
                                if (!mSocket.isInputShutdown()) {
                                    mBuffer = new byte[input.available()];
                                    if (mBuffer.length != 0){
                                        input.read(mBuffer);
                                        String string = new String(mBuffer);
                                        Log.e(TAG, string);
                                        Message msg = new Message();
                                        msg.what = 1;
                                        msg.obj = string;
                                        handler.sendMessage(msg);
                                    }
                                }
                            }
                            else
                                connect();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    public void send(String msg) {
        try {
            mOutputStream = mSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mOutputStream.write(msg.getBytes());
                    mOutputStream.flush();
                    Log.i(TAG, "Sent " + msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void sendLocation(Context context){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Location location = null;
                while (true){
                    try {
                        location  = LocationUtils.getBestLocation(context, location);
                        String msg = "LOCATION;" + UserController.getUserController().getUser().getPhoneNumber() + ";" + UserController.getUserController().getUser().getName() + ";" + location.getLatitude() + ";" + location.getLongitude();
                        Thread.sleep(10000);
                        mOutputStream = mSocket.getOutputStream();
                        mOutputStream.write(msg.getBytes());
                        mOutputStream.flush();
                        Log.i(TAG, "Sent " + msg);
                        msg = "MSG;94489600;94489600;寄";
                        mOutputStream = mSocket.getOutputStream();
                        mOutputStream.write(msg.getBytes());
                        mOutputStream.flush();
                        Log.i(TAG, "Sent " + msg);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

}
