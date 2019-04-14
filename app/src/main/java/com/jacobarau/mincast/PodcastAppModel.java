package com.jacobarau.mincast;

import android.content.Context;

import com.jacobarau.mincast.subscription.Subscription;

import java.util.ArrayList;
import java.util.List;

public class PodcastAppModel {
    private Context appContext;

    public PodcastAppModel(Context appContext) {
        this.appContext = appContext;
    }

    public List<Subscription> getSubscriptions() {
        List<Subscription> subscriptions = new ArrayList<>();
        Subscription subscription = new Subscription();
        subscription.setTitle("Podcast 1");
        subscription.setDescription("Just a podcast");
        subscriptions.add(subscription);

        subscription = new Subscription();
        subscription.setTitle("Another podcast");
        subscription.setDescription("Just a podcast with a way longer description. If things are working well, this will elide in a sensible way that looks nice on all platforms.");
        subscriptions.add(subscription);

        subscription = new Subscription();
        subscription.setTitle("A podcast with a ridiculously long name; hopefully this doesn't break my layouts (or if it does, I will certainly learn something about Android layouts)");
        subscription.setDescription("Just a podcast");
        subscriptions.add(subscription);

        subscription = new Subscription();
        subscription.setTitle("Podcast 5");
        subscription.setDescription("Just a podcast");
        subscriptions.add(subscription);

        return subscriptions;
    }
}
