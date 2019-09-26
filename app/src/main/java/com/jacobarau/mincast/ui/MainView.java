package com.jacobarau.mincast.ui;

import com.jacobarau.mincast.subscription.Subscription;

import java.util.List;

public interface MainView {
    void onSubscriptionListChanged(List<Subscription> subscriptions);
    void showAddPodcastDialog();
}
