package com.CodeMonkey.saveme.Entity;

import com.google.gson.JsonObject;

import java.util.List;
import java.sql.Timestamp;

public class RescueAction {
    private String id;
    private Location rescuerLocation;
    private Timestamp arriveTime;
    private Timestamp leaveTime;

    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }

    public void setRescuerLocation(String rescuerLocation){
        this.rescuerLocation = rescuerLocation;
    }

    public Location getRescuerLocation(){
        return rescuerLocation;
    }

    public void setArriveTime(Timestamp arriveTime){
        this.arriveTime = arriveTime;
    }

    public Timestamp getArriveTime(){
        return arriveTime;
    }

    public void setLeaveTime(Timestamp leaveTime){
        this.leaveTime = leaveTime;
    }

    public Timestamp getLeaveTime(){
        return leaveTime;
    }
}

