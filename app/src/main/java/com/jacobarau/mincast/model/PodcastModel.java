package com.jacobarau.mincast.model;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;

import com.jacobarau.mincast.db.PodcastDatabase;
import com.jacobarau.mincast.db.PodcastDatabaseHelper;
import com.jacobarau.mincast.service.Downloader;
import com.jacobarau.mincast.service.UpdateService;
import com.jacobarau.mincast.subscription.Subscription;
import com.jacobarau.mincast.sync.rss.ParseException;
import com.jacobarau.mincast.sync.rss.ParseResult;
import com.jacobarau.mincast.sync.rss.RssParser;

import org.threeten.bp.Instant;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.List;
import java.util.Observer;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PodcastModel {
    private final String TAG = this.getClass().getSimpleName();
    private PodcastDatabase podcastDatabase;

    private final ValueObservable<List<Subscription>> subscriptionsObservable;
    private ExecutorService dbExecutor = Executors.newSingleThreadExecutor();
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private final Context appContext;

    PodcastModel(final Context appContext) {
        subscriptionsObservable = new ValueObservable<>();
        dbExecutor.execute(new Runnable() {
            @Override
            public void run() {
                PodcastDatabaseHelper helper = new PodcastDatabaseHelper(appContext);
                SQLiteDatabase database = helper.getWritableDatabase();
                podcastDatabase = new PodcastDatabase(database);
            }
        });

        this.appContext = appContext;
        updateSubscriptionList();
    }

    private void updateSubscriptionList() {
        dbExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final List<Subscription> subscriptions = podcastDatabase.getSubscriptions();
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        subscriptionsObservable.onValueChanged(subscriptions);
                    }
                });
            }
        });
    }

    // Observe changes to each of the below:
    // Get current subscriptions (summary screen)
    // Get podcast feed list (all episodes of one podcast) (works for both subscribed and not)
    // Subscribe or unsubscribe
    // Get podcast episodes matching search query


    /**
     * Bind the given observer to the subscriptions list. This will immediately call into the
     * given Observer with the current subscriptions list.
     *
     * @param subscriptionObserver The observer to watch the subscriptions list
     */
    public void observeSubscriptions(Observer subscriptionObserver) {
        subscriptionsObservable.addObserver(subscriptionObserver);
    }

    /**
     * Unbind the given observer and stop receiving updates.
     * @param subscriptionObserver The previously added observer to remove (added via {@link PodcastModel#observeSubscriptions(Observer)} )
     */
    public void deleteSubscriptionObserver(Observer subscriptionObserver) {
        subscriptionsObservable.deleteObserver(subscriptionObserver);
    }

    public void subscribeTo(String url) {
        final Subscription subscription = new Subscription();
        subscription.setUrl(url);
        subscription.setLastUpdated(Instant.now());
        subscription.setTitle(url);

        dbExecutor.execute(new Runnable() {
            @Override
            public void run() {
                podcastDatabase.addSubscription(subscription);
                updateSubscriptionList();
            }
        });
    }

    public void unsubscribeFrom(final Subscription subscription) {
        dbExecutor.execute(new Runnable() {
            @Override
            public void run() {
                podcastDatabase.deleteSubscription(subscription);
                updateSubscriptionList();
            }
        });
    }

    public void unsubscribeFrom(List<Subscription> subscriptions) {
        for (Subscription subscription : subscriptions) {
            unsubscribeFrom(subscription);
        }
    }

    public void startUpdate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            appContext.startForegroundService(new Intent(appContext, UpdateService.class));
        } else {
            appContext.startService(new Intent(appContext, UpdateService.class));
        }
    }

    public Future<List<Subscription>> getSubscriptions() {
        return dbExecutor.submit(new Callable<List<Subscription>>() {
            @Override
            public List<Subscription> call() {
                return podcastDatabase.getSubscriptions();
            }
        });
    }

    private ExecutorService executorService = Executors.newCachedThreadPool();

    // TODO: I don't like that this returns null, but I dislike exceptions even more at the moment.
    private ParseResult parseFromUrl(String url) {
        File destination;
        try {
            // Local temp file will be in the cache directory, named after the base64 conversion
            // of the given URL. I'd just use the URL, except URLs contain characters I'm sure
            // some file systems will not enjoy.

            // Suppressed because we can't actually use the suggested charset object because our
            // minimum API level is too low.

            //noinspection CharsetObjectCanBeUsed
            destination = new File(appContext.getCacheDir(),
                    Base64.encodeToString(url.getBytes("utf8"), Base64.DEFAULT));
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "getSubscriptionFromURL: couldn't encode URL as base64", e);
            return null;
        }

        try {
            new Downloader().downloadFile(new URL(url), destination, new Downloader.ProgressListener() {
                @Override
                public void onProgress(long position, Long total) {
                    Log.i(TAG, "onProgress: position is " + position + ", total is " + total);
                    // TODO: download progress to the user?
                }
            });
        } catch (IOException e) {
            Log.e(TAG, "getSubscriptionFromURL: IOException doing downloadFile()", e);
        }

        RssParser parser = new RssParser();
        ParseResult result;
        try {
            result = parser.parseRSS(new FileInputStream(destination), "utf-8");
        } catch (XmlPullParserException | IOException | ParseException e) {
            Log.e(TAG, "getSubscriptionFromURL: Error parsing downloaded file", e);
            //TODO how to manage cached file cleanup...some sort of RAII thing?
            return null;
        }
        Log.i(TAG, "run: result was " + result);
        return result;
    }

    /**
     * Called only from the UpdateService when said service is created.
     *
     */
    public void onServiceOnCreate() {
        //TODO don't allow multiple instances of this function to run in parallel
        //honestly I'm not convinced this will hold up under strain--user running an update
        //while browsing around the app and adding/deleting podcasts
        //Probably ultimately need a data model that can broadcast events about itself
        //to the workers so e.g. we can kill a downloader that's downloading an episode of a
        //freshly deleted podcast. But I don't feel like writing that just this second.
        try {
            for (final Subscription subscription : getSubscriptions().get()) {
                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        ParseResult result = parseFromUrl(subscription.getUrl());
                        if (result == null) {
                            //TODO: tell the user there's an error
                            Log.e(TAG, "Error updating subscription with URL " + subscription.getUrl());
                            return;
                        }

                        subscription.setTitle(result.subscription.getTitle());
                        dbExecutor.submit(new Runnable() {
                            @Override
                            public void run() {
                                podcastDatabase.updateSubscription(subscription);
                                //TODO ooooohhhh hacky as shit
                                updateSubscriptionList();
                            }
                        });
                    }
                });
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
