package com.CodeMonkey.saveme.Controller;


import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.CodeMonkey.saveme.Boundary.MainPage;
import com.CodeMonkey.saveme.Entity.Event;
import com.CodeMonkey.saveme.Entity.GovDataRsp;
import com.CodeMonkey.saveme.Entity.UserRsp;
import com.CodeMonkey.saveme.Fragment.RequestListPageFrag;
import com.CodeMonkey.saveme.Util.NotificationUtil;
import com.CodeMonkey.saveme.Util.RequestUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observer;

public class EventController{

    private Map<String, Event> eventList = new HashMap<>();
    private Event acceptEvent = null;
    private volatile static EventController eventController;
    private Handler handler;
    private RequestListPageFrag.RequestListItemHolder acceptHolder;

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

    public void getWeather(){
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String time = dataFormat.format(date);
        time = time.replace(" ", "T");

        RequestUtil.getTemperature(new Observer<GovDataRsp>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(GovDataRsp govDataRsp) {
                Map<double[], Double> result = govDataRsp.getFormData();
                double min = 10000;
                double temp;
                double latitude;
                double longitude;
                double temperature = 0;
                for (Event event: eventList.values()){
                    latitude = event.getLatitude();
                    longitude = event.getLongitude();
                    for (double[] location: result.keySet()){
                        temp = Math.pow((location[0] - latitude), 2) + Math.pow((location[1] - longitude), 2);
                        if (temp < min) {
                            temp = min;
                            temperature = result.get(location);
                        }
                    }
                    event.setTemperature(temperature);
                }
            }
        }, time);

        RequestUtil.getHumidity(new Observer<GovDataRsp>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(GovDataRsp govDataRsp) {
                Map<double[], Double> result = govDataRsp.getFormData();
                double min = 10000;
                double temp;
                double latitude;
                double longitude;
                double humidity = 0;
                for (Event event: eventList.values()){
                    latitude = event.getLatitude();
                    longitude = event.getLongitude();
                    for (double[] location: result.keySet()){
                        temp = Math.pow((location[0] - latitude), 2) + Math.pow((location[1] - longitude), 2);
                        if (temp < min) {
                            temp = min;
                            humidity = result.get(location);
                        }
                    }
                    event.setHumidity(humidity);
                }
            }
        }, time);
    }

    public void addNewEvent(String phoneNumber, String latitude, String longitude){
        RequestUtil.getUserData(new Observer<UserRsp>() {
            @Override
            public void onCompleted() {
                getWeather();
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

    public void acceptEvent(String phoneNumber, RequestListPageFrag.RequestListItemHolder holder) {
        acceptEvent = eventList.get(phoneNumber);
        acceptHolder = holder;
        TCPManager.getTCPManager().send("ACCEPTREQ;" + phoneNumber + ";"
                + UserController.getUserController().getUser().getPhoneNumber());
    }

    public void declineEvent(String phoneNumber){
        acceptEvent = null;
        acceptHolder = null;
        TCPManager.getTCPManager().send("DECLINEREQ;" + phoneNumber + ";"
                    + UserController.getUserController().getUser().getPhoneNumber());
    }

    public Event getAcceptEvent() {
        return acceptEvent;
    }

    public RequestListPageFrag.RequestListItemHolder getAcceptHolder() {
        return acceptHolder;
    }

    public void setRescueNumber(String phoneNumber, int rescueNumber){
        eventList.get(phoneNumber).setRescueNumber(rescueNumber);
        Message message = new Message();
        message.what = 6;
        message.obj = eventList.get(phoneNumber);
        handler.sendMessage(message);
    }
}
