package com.CodeMonkey.saveme.Entity;

public class ReadingData {

    private String station_id;
    private double value;

    public String getStation_id() {
        return station_id;
    }

    public void setStation_id(String station_id) {
        this.station_id = station_id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ReadingData{" +
                "station_id='" + station_id + '\'' +
                ", value=" + value +
                '}';
    }
}
