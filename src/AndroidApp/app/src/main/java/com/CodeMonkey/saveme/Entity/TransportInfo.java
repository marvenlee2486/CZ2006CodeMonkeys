package com.CodeMonkey.saveme.Entity;

import java.sql.Timestamp;

public class TransportInfo {
    

    private String transportName;
    private String stopName;
    private Timestamp nextArriveTime;
  

    public void setTransportName(String transportName){
        this.transportName = transportName;
    }

    public String getTransportName(){
        return transportName;
    }

    public void setStopName(String stopName){
        this.stopName = stopName;
    }

    public String getStopName(){
        return stopName;
    }

    public void setNextArriveTime(Timestamp nextArriveTime){
        this.nextArriveTime = nextArriveTime;
    }

    public Timestamp getNextArriveTime(){
        return nextArriveTime;
    }
}

