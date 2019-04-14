package com.jacobarau.mincast.activity;

import com.jacobarau.mincast.PodcastAppModel;

public class MainPresenter {
    MainView view;
    PodcastAppModel appModel;

    MainPresenter(MainView view, PodcastAppModel appModel) {
        this.view = view;
        this.appModel = appModel;
        this.view.onSubscriptionListChanged(this.appModel.getSubscriptions());
    }
}
