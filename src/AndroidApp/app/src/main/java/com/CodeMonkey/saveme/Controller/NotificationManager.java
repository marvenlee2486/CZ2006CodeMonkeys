package com.CodeMonkey.saveme.Controller;

import android.app.NotificationChannel;
import android.os.Build;

import com.CodeMonkey.saveme.R;

public class NotificationManager {

    private static final String CHANNEL_ID= "EventNotificationChannel";
    private static final String name = "EventNotificationChannel";
    private static final String description = "This is the channel for the notification of events";

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, android.app.NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(description);
            channel.enableVibration(true);
//            android.app.NotificationManager notificationManager = activity.getSystemService(android.app.NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
        }
    }
}
