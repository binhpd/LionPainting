package com.hiccup.kidpainting.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import androidx.annotation.RequiresApi;

public class NotificationHelper {
    public static final String DAILY_CHANNEL = "daily_channel";


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void createChannel(Context context, String channel) {
        createChannel(context, channel, channel);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void createChannel(Context context, String channel, String channelName) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel notificationChannel = new NotificationChannel(channel, channelName, NotificationManager.IMPORTANCE_DEFAULT);
        notificationChannel.setDescription("default channel");
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.parseColor("#00ff00"));
        notificationChannel.setVibrationPattern(new long[]{0});
        notificationChannel.enableVibration(true);
        nm.createNotificationChannel(notificationChannel);
    }
}

