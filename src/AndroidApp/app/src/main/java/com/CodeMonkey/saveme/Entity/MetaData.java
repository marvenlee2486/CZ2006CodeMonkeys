package com.CodeMonkey.saveme.Entity;

import java.util.Arrays;

public class MetaData {

    private WeatherStation[] stations;
    private String reading_type;
    private String reading_unit;

    public WeatherStation[] getStations() {
        return stations;
    }

    public void setStations(WeatherStation[] stations) {
        this.stations = stations;
    }

    public String getReading_type() {
        return reading_type;
    }

    public void setReading_type(String reading_type) {
        this.reading_type = reading_type;
    }

    public String getReading_unit() {
        return reading_unit;
    }

    public void setReading_unit(String reading_unit) {
        this.reading_unit = reading_unit;
    }

    @Override
    public String toString() {
        return "MetaData{" +
                "stations=" + Arrays.toString(stations) +
                ", reading_type='" + reading_type + '\'' +
                ", reading_unit='" + reading_unit + '\'' +
                '}';
    }
}
