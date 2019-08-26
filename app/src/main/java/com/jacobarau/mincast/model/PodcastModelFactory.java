package com.jacobarau.mincast.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.jacobarau.mincast.db.PodcastDatabase;
import com.jacobarau.mincast.db.PodcastDatabaseHelper;

public class PodcastModelFactory {
    private static PodcastModel podcastModel = null;

    public static synchronized PodcastModel getPodcastModel(Context applicationContext) {
        if (podcastModel != null) {
            return podcastModel;
        }

        PodcastDatabaseHelper helper = new PodcastDatabaseHelper(applicationContext);
        SQLiteDatabase database = helper.getWritableDatabase();
        PodcastDatabase podcastDatabase = new PodcastDatabase(database);
        podcastModel = new PodcastModel(podcastDatabase);
        return podcastModel;
    }
}
