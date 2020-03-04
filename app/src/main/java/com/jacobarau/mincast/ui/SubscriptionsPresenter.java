package com.jacobarau.mincast.ui;

import com.jacobarau.mincast.model.PodcastModel;
import com.jacobarau.mincast.subscription.Subscription;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

class SubscriptionsPresenter {
    private final SubscriptionsView subscriptionsView;
    private final PodcastModel podcastModel;
    private final Observer subscriptionObserver;

    SubscriptionsPresenter(final SubscriptionsView subscriptionsView, PodcastModel podcastModel) {
        this.subscriptionsView = subscriptionsView;
        this.podcastModel = podcastModel;

        subscriptionObserver = new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                // TODO: This is shit. Let's make it not shit after we get it workingish. Maybe.
                subscriptionsView.onSubscriptionListChanged((List<Subscription>) arg);
            }
        };
    }

    void onStart() {
        podcastModel.observeSubscriptions(subscriptionObserver);
    }

    void onStop() {
        podcastModel.deleteSubscriptionObserver(subscriptionObserver);
    }

    void onAddPodcastSelected() {
        subscriptionsView.showAddPodcastDialog();
    }

    void onUnsubscribe(List<Subscription> subscriptions) {
        podcastModel.unsubscribeFrom(subscriptions);
    }

    void onUpdatePodcastsSelected() {
        podcastModel.startUpdate();
    }
}
