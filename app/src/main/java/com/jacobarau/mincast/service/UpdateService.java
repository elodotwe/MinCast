package com.jacobarau.mincast.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.jacobarau.mincast.model.PodcastModel;
import com.jacobarau.mincast.model.PodcastModelFactory;
import com.jacobarau.mincast.ui.Notifications;

import static com.jacobarau.mincast.ui.Notifications.ONGOING_NOTIFICATION_ID;

public class UpdateService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        startForeground(ONGOING_NOTIFICATION_ID, new Notifications().buildUpdateServiceNotification(this));

        final PodcastModel model = PodcastModelFactory.getPodcastModel(getApplicationContext());
        model.onServiceOnCreate();
    }
}
