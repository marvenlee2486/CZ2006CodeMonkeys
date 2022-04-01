package com.CodeMonkey.saveme.Entity;

import android.location.Location;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;

public class Event {

    private User user;
    private double latitude;
    private double longitude;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Event{" +
                "user=" + user.toString() +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}

