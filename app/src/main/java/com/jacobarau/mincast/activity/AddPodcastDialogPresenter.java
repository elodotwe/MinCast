package com.jacobarau.mincast.activity;

import com.jacobarau.mincast.model.PodcastModel;

public class AddPodcastDialogPresenter {
    private final PodcastModel podcastModel;
    private final AddPodcastDialogView view;

    public AddPodcastDialogPresenter(PodcastModel podcastModel, AddPodcastDialogView view) {
        this.podcastModel = podcastModel;
        this.view = view;
    }

    public void onShown() {

    }

    public void onAddPodcast(String url) {
        podcastModel.subscribeTo(url);
        view.dismiss();
    }
}
