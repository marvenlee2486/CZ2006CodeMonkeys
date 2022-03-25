package com.CodeMonkey.saveme.Entity;

import com.google.gson.JsonObject;

import java.util.List;
import java.sql.Timestamp;

public class Event {
    private String id;
    private Location userLocation;
    private Timestamp callStartTime;
    private Timestamp endTime;
    private String result;
    private String additionalInfoFromUser;
    private ArrayList<String> rescueActions;

    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }

    public void setUserLocation (String rescuserLocationuerLocation){
        this.userLocation = userLocation;
    }

    public Location getUserLocation(){
        return userLocation;
    }

    public void setCallStartTime(Timestamp callStartTime){
        this.callStartTime = callStartTime;
    }

    public Timestamp getCallStartTime(){
        return callStartTime;
    }

    public void setEndTime(Timestamp endTime){
        this.endTime = endTime;
    }

    public Timestamp getEndTime(){
        return endTime;
    }

    public void setAdditionalInfoFromUser(String additionalInfoFromUser){
        this.additionalInfoFromUser = additionalInfoFromUser;
    }

    public String getAdditionalInfoFromUser(){
        return additionalInfoFromUser;
    }

    public void setRescueActions(ArrayList<String> rescueActions){
        this.rescueActions = rescueActions;
    }

    public ArrayList<String> getRescueActions(){
        return rescueActions;
    }
}

