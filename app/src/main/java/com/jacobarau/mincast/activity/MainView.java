package com.jacobarau.mincast.activity;

import com.jacobarau.mincast.subscription.Subscription;

import java.util.List;

public interface MainView {
    void onSubscriptionListChanged(List<Subscription> subscriptions);
}
