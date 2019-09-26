package com.jacobarau.mincast.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.jacobarau.mincast.R;
import com.jacobarau.mincast.model.PodcastModel;
import com.jacobarau.mincast.model.PodcastModelFactory;
import com.jacobarau.mincast.subscription.Subscription;
import com.jacobarau.mincast.sync.rss.ParseException;
import com.jacobarau.mincast.sync.rss.ParseResult;
import com.jacobarau.mincast.sync.rss.RssParser;
import com.jacobarau.mincast.ui.MainActivity;
import com.jacobarau.mincast.ui.Notifications;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.jacobarau.mincast.ui.Notifications.ONGOING_NOTIFICATION_ID;

public class UpdateService extends Service {
    private final String TAG = this.getClass().getName();

    ExecutorService executorService;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        startForeground(ONGOING_NOTIFICATION_ID, new Notifications().buildUpdateServiceNotification(this));
        executorService = Executors.newCachedThreadPool();

        final PodcastModel model = PodcastModelFactory.getPodcastModel(getApplicationContext());

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    for (final Subscription subscription : model.getSubscriptions().get()) {
                        executorService.submit(new Runnable() {
                            @Override
                            public void run() {
                                File destination = new File(getCacheDir(), String.valueOf(subscription.getUrl().hashCode()));
                                try {
                                    new Downloader().downloadFile(new URL(subscription.getUrl()), destination, new Downloader.ProgressListener() {
                                        @Override
                                        public void onProgress(long position, Long total) {
                                            Log.d(TAG, "onProgress() called with: position = [" + position + "], total = [" + total + "]");
                                        }
                                    });
                                    RssParser parser = new RssParser();
                                    ParseResult result = parser.parseRSS(new FileInputStream(destination), "utf-8");
                                    Log.i(TAG, "run: result was " + result);
                                    subscription.setTitle(result.subscription.getTitle());
                                    model.dbExecutor.submit(new Runnable() {
                                        @Override
                                        public void run() {
                                            model.podcastDatabase.updateSubscription(subscription);
                                        }
                                    });
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                } catch (XmlPullParserException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
