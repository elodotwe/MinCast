package com.jacobarau.mincast.model;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.jacobarau.mincast.db.PodcastDatabase;
import com.jacobarau.mincast.subscription.Subscription;

import org.threeten.bp.Instant;

import java.util.List;
import java.util.Observer;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PodcastModel {
    private final PodcastDatabase podcastDatabase;

    private final ValueObservable<List<Subscription>> subscriptionsObservable;

    private Executor dbExecutor = Executors.newCachedThreadPool();
    private Handler mainHandler = new Handler(Looper.getMainLooper());


    PodcastModel(final PodcastDatabase podcastDatabase) {
        subscriptionsObservable = new ValueObservable<>();
        this.podcastDatabase = podcastDatabase;
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
}
