package com.CodeMonkey.saveme.Entity;

import java.util.Arrays;

public class ReadingItem {

    private String timestamp;
    private ReadingData[] readings;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public ReadingData[] getReadings() {
        return readings;
    }

    public void setReadings(ReadingData[] readings) {
        this.readings = readings;
    }

    @Override
    public String toString() {
        return "ReadingItem{" +
                "timestamp='" + timestamp + '\'' +
                ", readings=" + Arrays.toString(readings) +
                '}';
    }
}
