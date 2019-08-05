package com.jacobarau.mincast.model;

import com.jacobarau.mincast.db.PodcastDatabase;
import com.jacobarau.mincast.subscription.Subscription;

import org.threeten.bp.Instant;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class PodcastModel {
    private final PodcastDatabase podcastDatabase;

    private final ValueObservable<List<Subscription>> subscriptionsObservable;

    public PodcastModel(PodcastDatabase podcastDatabase) {
        subscriptionsObservable = new ValueObservable<>();
        //TODO: Also shit. Don't broadcast a null on startup. There's a better way to do this.
        subscriptionsObservable.onValueChanged(new ArrayList<Subscription>());

        this.podcastDatabase = podcastDatabase;
        podcastDatabase.addSubscriptionsObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                subscriptionsObservable.onValueChanged((List<Subscription>) arg);
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
        Subscription subscription = new Subscription();
        subscription.setUrl(url);
        subscription.setLastUpdated(Instant.now());
        podcastDatabase.addSubscription(subscription);
    }
}
