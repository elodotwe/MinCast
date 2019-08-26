package com.jacobarau.mincast.db;

import android.provider.BaseColumns;

final class PodcastDatabaseContract {
    static class Subscriptions implements BaseColumns {
        static final String TABLE_NAME = "subscriptions";
        static final String COLUMN_NAME_URL = "url";
        static final String COLUMN_NAME_TITLE = "title";
        static final String COLUMN_NAME_LINK = "link";
        static final String COLUMN_NAME_DESCRIPTION = "description";
        static final String COLUMN_NAME_IMAGE_URL = "imageUrl";
        static final String COLUMN_NAME_LAST_UPDATED = "lastUpdated";
    }
}
