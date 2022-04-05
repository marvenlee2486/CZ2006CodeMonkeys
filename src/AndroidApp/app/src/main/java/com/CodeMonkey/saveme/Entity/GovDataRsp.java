package com.CodeMonkey.saveme.Entity;

import android.util.Log;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GovDataRsp {

    private MetaData metadata;
    private ReadingItem[] items;
    private APIInfo api_info;

    public MetaData getMetadata() {
        return metadata;
    }

    public void setMetadata(MetaData metadata) {
        this.metadata = metadata;
    }

    public ReadingItem[] getItems() {
        return items;
    }

    public void setItems(ReadingItem[] items) {
        this.items = items;
    }

    public APIInfo getApi_info() {
        return api_info;
    }

    public void setApi_info(APIInfo api_info) {
        this.api_info = api_info;
    }

    @Override
    public String toString() {
        return "TemperatureRsp{" +
                "metadata=" + metadata +
                ", items=" + Arrays.toString(items) +
                ", api_info=" + api_info +
                '}';
    }

    public Map<double[], Double> getFormData(){
        Map<double[], Double> result = new HashMap<>();
        WeatherStation[] stations = metadata.getStations();
        int len = stations.length;
        double[] location;
        double value;
        for (int i = 0; i < len; i++){
            location = new double[2];
            location[0] = Double.parseDouble(stations[i].getLocation().getLatitude());
            location[1] = Double.parseDouble(stations[i].getLocation().getLongitude());
            value = items[0].getReadings()[i].getValue();
            result.put(location, value);
        }
        return result;
    }
}
