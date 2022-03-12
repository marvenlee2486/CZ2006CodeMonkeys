package com.CodeMonkey.saveme.Controller;


import android.os.Handler;
import android.util.Log;

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
    private byte[] mBuffer = new byte[4096];
    private boolean isRunning = true;
    private Thread mThread;
    private Handler mHandler;

    private TCPManager(){
        connect();
    }

    public static TCPManager getTCPManager(){
        if (mTCPManager == null){
            synchronized (TCPManager.class){
                if (mTCPManager == null){
                    mTCPManager = new TCPManager();
                }
            }
        }
        return mTCPManager;
    }

    private void connect() {
        new Thread() {
            @Override
            public void run() {
                try {
                    mSocket = new Socket(URLUtil.tcpIP, URLUtil.tcpPort);
                    if (mSocket != null) {
                        mOutputStream = mSocket.getOutputStream();
                        mInputStream = mSocket.getInputStream();
                        mOutputStream.write("Connected".getBytes());
                        mOutputStream.flush();
                        receive(mSocket);
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
                    mOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void receive(Socket mSocket){
        DataInputStream input = new DataInputStream(mInputStream);
        new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        if (mSocket.isConnected()) {
                            if (!mSocket.isInputShutdown()) {
                                input.read(mBuffer);
                                Log.e(TAG, mBuffer.length+"");
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

}
