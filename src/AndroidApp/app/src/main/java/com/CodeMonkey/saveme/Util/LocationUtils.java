package com.CodeMonkey.saveme.Util;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

/***
 * LocationUtils created by Wang Tianyu 22/03/2022
 * Utils for location services
 */
public class LocationUtils {

    /**
     * GPS
     */
    public static Location getGPSLocation(@NonNull Context context) {
        Location location = null;
        LocationManager manager = getLocationManager(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        return location;
    }

    /**
     * network
     */
    public static Location getNetWorkLocation(Context context) {
        Location location = null;
        LocationManager manager = getLocationManager(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        if (manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        return location;
    }

    /**
     * Best location
     */
    public static Location getBestLocation(Context context, Location lastLocation) {
        Location location;
        location = getGPSLocation(context);
        if (location == null) {
            location = getNetWorkLocation(context);
            if (location == null){
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return null;
                }
                LocationManager manager = getLocationManager(context);
                Criteria criteria = new Criteria();
                String provider = manager.getBestProvider(criteria, true);
                location = manager.getLastKnownLocation(provider);
                if (location == null)
                    location = lastLocation;
            }
        }
        return location;
    }



    private static LocationManager getLocationManager(@NonNull Context context) {
        return (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

}

