package com.jacobarau.mincast.ui;

import android.app.Activity;
import android.os.Bundle;

import com.jacobarau.mincast.R;
import com.jacobarau.mincast.model.PodcastModelFactory;

public class EpisodesActivity extends Activity {

    private EpisodesPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episodes);
        presenter = new EpisodesPresenter(this, PodcastModelFactory.getPodcastModel(getApplicationContext()));
    }

    public void onEpisodesListChanged() {

    }
}
