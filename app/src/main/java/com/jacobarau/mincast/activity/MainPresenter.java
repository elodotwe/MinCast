package com.jacobarau.mincast.activity;

import com.jacobarau.mincast.model.PodcastModel;
import com.jacobarau.mincast.subscription.Subscription;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class MainPresenter {
    private final MainView mainView;
    private final PodcastModel podcastModel;
    private final Observer subscriptionObserver;

    public MainPresenter(final MainView mainView, PodcastModel podcastModel) {
        this.mainView = mainView;
        this.podcastModel = podcastModel;

        subscriptionObserver = new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                // TODO: This is shit. Let's make it not shit after we get it workingish. Maybe.
                mainView.onSubscriptionListChanged((List<Subscription>) arg);
            }
        };
    }

    public void onStart() {
        podcastModel.observeSubscriptions(subscriptionObserver);
    }

    public void onStop() {
        podcastModel.deleteSubscriptionObserver(subscriptionObserver);
    }
}
