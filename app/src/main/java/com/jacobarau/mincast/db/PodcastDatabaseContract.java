package com.jacobarau.mincast.db;

import android.provider.BaseColumns;

import org.threeten.bp.Instant;

public final class PodcastDatabaseContract {
    public static class Subscriptions implements BaseColumns {
        public static final String TABLE_NAME = "subscriptions";
        public static final String COLUMN_NAME_URL = "url";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_LINK = "link";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_IMAGE_URL = "imageUrl";
        public static final String COLUMN_NAME_LAST_UPDATED = "lastUpdated";
    }
}
