package com.example.prm392_craft_management.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.prm392_craft_management.R;

public class NotificationUtils {
    private final Context context;
    private final String channelId;

    public NotificationUtils(Context context,String channelId) {
        this.context = context;
        this.channelId = channelId;
    }

    public void showNotification(String title, String text) {
        NotificationCompat.Builder builder = buildNotification(title, text);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = notificationManager.getNotificationChannel(channelId);
                if (notificationChannel == null) {
                    int importance = NotificationManager.IMPORTANCE_DEFAULT;
                    notificationChannel = new NotificationChannel(channelId, "Channel Name", importance);
                    notificationChannel.setLightColor(Color.GREEN);
                    notificationChannel.enableVibration(true);
                    notificationManager.createNotificationChannel(notificationChannel);
                }
            }
            notificationManager.notify(0, builder.build());
        }
    }

    private NotificationCompat.Builder buildNotification(String title, String text) {
        return new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_notification)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_notification))
                .setContentTitle(title)
                .setContentText(text)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    }
}