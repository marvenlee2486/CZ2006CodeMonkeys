package com.CodeMonkey.saveme.Entity;

public class WeatherStation {

    private String id;
    private String device_id;
    private String name;
    private LocationJson location;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocationJson getLocation() {
        return location;
    }

    public void setLocation(LocationJson location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "WeatherStation{" +
                "id='" + id + '\'' +
                ", device_id='" + device_id + '\'' +
                ", name='" + name + '\'' +
                ", location=" + location +
                '}';
    }
}
