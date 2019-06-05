package com.jacobarau.mincast.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class PodcastDatabaseFactory {
    private static PodcastDatabase podcastDatabase = null;

    public static PodcastDatabase getPodcastDatabase(Context applicationContext) {
        if (podcastDatabase != null) {
            return podcastDatabase;
        }

        PodcastDatabaseHelper helper = new PodcastDatabaseHelper(applicationContext);
        SQLiteDatabase database = helper.getWritableDatabase();
        podcastDatabase = new PodcastDatabase(database);
        return podcastDatabase;
    }
}
