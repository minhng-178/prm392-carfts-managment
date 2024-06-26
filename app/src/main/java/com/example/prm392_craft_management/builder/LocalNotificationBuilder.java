package com.example.prm392_craft_management.builder;

import android.content.Context;
import android.graphics.BitmapFactory;

import androidx.core.app.NotificationCompat;

import com.example.prm392_craft_management.R;


public class LocalNotificationBuilder {
    private final Context context;
    private final String channelId;

    public LocalNotificationBuilder(Context context, String channelId) {
        this.context = context;
        this.channelId = channelId;
    }

    public NotificationCompat.Builder buildNotification(String title, String text) {

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
