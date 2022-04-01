package com.CodeMonkey.saveme.Controller;


import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.CodeMonkey.saveme.Boundary.MainPage;
import com.CodeMonkey.saveme.Entity.Event;
import com.CodeMonkey.saveme.Entity.UserRsp;
import com.CodeMonkey.saveme.Util.NotificationUtil;
import com.CodeMonkey.saveme.Util.RequestUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observer;

public class EventController{

    private Map<String, Event> eventList = new HashMap<>();
    private volatile static EventController eventController;
    private Handler handler;

    private EventController(){}

    public static EventController getEventController(){
        if (eventController == null){
            synchronized (EventController.class){
                if (eventController == null){
                    eventController = new EventController();
                }
            }
        }
        return eventController;
    }

    public void addNewEvent(String phoneNumber, String latitude, String longitude){
        RequestUtil.getUserData(new Observer<UserRsp>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Log.e("Error", e.toString());
            }

            @Override
            public void onNext(UserRsp userRsp) {
                Event event = new Event();
                event.setUser(userRsp.getBody());
                event.setLatitude(Double.parseDouble(latitude));
                event.setLongitude(Double.parseDouble(longitude));
                eventList.put(phoneNumber, event);
                Message msg = new Message();
                msg.what = 2;
                msg.obj = phoneNumber;
                handler.sendMessage(msg);
            }
        }, phoneNumber, UserController.getUserController().getToken());
    }

    public void removeEvent(String phoneNumber){
        eventList.remove(phoneNumber);
        Message msg = new Message();
        msg.what = 3;
        msg.obj = phoneNumber;
        handler.sendMessage(msg);
    }

    public Map<String, Event> getEventList() {
        return eventList;
    }

    public void setEventList(Map<String, Event> eventList) {
        this.eventList = eventList;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public Handler getHandler() {
        return handler;
    }
}
