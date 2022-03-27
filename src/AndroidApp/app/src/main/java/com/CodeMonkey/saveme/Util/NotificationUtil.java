package com.CodeMonkey.saveme.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.CodeMonkey.saveme.Boundary.MainPage;
import com.CodeMonkey.saveme.R;

public class NotificationUtil {

    private static final String CHANNEL_ID= "EventNotificationChannel";
    private static final String name = "EventNotificationChannel";
    private static final String description = "This is the channel for the notification of events";

    public static void createNotificationChannel(Activity activity) {
        NotificationManager notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = notificationManager.getNotificationChannel(CHANNEL_ID);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && channel == null) {
            channel = new NotificationChannel(CHANNEL_ID, name, android.app.NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(description);
            channel.enableVibration(true);
            android.app.NotificationManager notificationMgr = activity.getSystemService(android.app.NotificationManager.class);
            notificationMgr.createNotificationChannel(channel);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (channel.getImportance() == NotificationManager.IMPORTANCE_DEFAULT) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    NotificationChannel finalChannel = channel;
                    builder.setMessage("Please turn on the notification for banner so that you can see any need help directly!")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, activity.getPackageName());
                                    intent.putExtra(Settings.EXTRA_CHANNEL_ID, finalChannel.getId());
                                    activity.startActivity(intent);
                                }
                            });
                    builder.setCancelable(false);
                    builder.show();
                }
            }
        }
    }

    public static void createNotification(Activity activity, String title, String description){
        Intent intent = new Intent(activity, MainPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(activity, "EventNotificationChannel")
                .setSmallIcon(R.mipmap.logo)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent)
                .setFullScreenIntent(pendingIntent, true)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(activity);
        notificationManager.notify(1, builder.build());
    }
}
