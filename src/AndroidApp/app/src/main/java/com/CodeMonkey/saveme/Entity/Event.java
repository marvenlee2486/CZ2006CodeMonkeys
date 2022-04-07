package com.CodeMonkey.saveme.Entity;

import android.location.Location;
import android.os.Message;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;

public class Event {

    private int rescueNumber = 0;
    private User user;
    private double latitude;
    private double longitude;
    private double temperature;
    private double humidity;
    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

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

    public int getRescueNumber() {
        return rescueNumber;
    }

    public void setRescueNumber(int rescueNumber) {
        this.rescueNumber = rescueNumber;
    }

    @Override
    public String toString() {
        return "Event{" +
                "rescueNumber=" + rescueNumber +
                ", user=" + user +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", location='" + location + '\'' +
                '}';
    }
}

