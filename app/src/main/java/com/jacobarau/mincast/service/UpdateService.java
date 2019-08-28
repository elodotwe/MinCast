package com.jacobarau.mincast.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import com.jacobarau.mincast.R;
import com.jacobarau.mincast.activity.MainActivity;

public class UpdateService extends Service {
    private static final int ONGOING_NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "update";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        Notification.Builder notificationBuilder = new Notification.Builder(this)
                        .setContentTitle(getText(R.string.update_notification_title))
                        .setContentText(getText(R.string.update_notification_message))
                        .setSmallIcon(R.drawable.ic_refresh_black)
                        .setContentIntent(pendingIntent)
                        .setTicker(getText(R.string.update_ticker_text));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder.setChannelId(CHANNEL_ID);
        }

        Notification notification = notificationBuilder.getNotification();

        startForeground(ONGOING_NOTIFICATION_ID, notification);
    }
}
