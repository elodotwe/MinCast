package com.jacobarau.mincast.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jacobarau.mincast.subscription.Subscription;

import org.threeten.bp.Instant;

import java.util.ArrayList;
import java.util.List;

import static com.jacobarau.mincast.db.PodcastDatabaseContract.Subscriptions;

public class PodcastDatabase {
    private SQLiteDatabase database;

    public PodcastDatabase(SQLiteDatabase database) {
        this.database = database;
    }

    public void addSubscription(Subscription subscription) {
        database.insertOrThrow(Subscriptions.TABLE_NAME, null, subscriptionToValues(subscription));
    }

    public void deleteSubscription(Subscription subscription) {
        database.delete(Subscriptions.TABLE_NAME, Subscriptions.COLUMN_NAME_URL + " = ?", new String[]{subscription.getUrl()});
    }

    public void updateSubscription(Subscription subscription) {
        database.update(Subscriptions.TABLE_NAME, subscriptionToValues(subscription),
                Subscriptions.COLUMN_NAME_URL + " = ?", new String[]{subscription.getUrl()});
    }

    public List<Subscription> getSubscriptions() {
        Cursor cursor = database.query(Subscriptions.TABLE_NAME,
                new String[]{
                        Subscriptions.COLUMN_NAME_URL,
                        Subscriptions.COLUMN_NAME_TITLE,
                        Subscriptions.COLUMN_NAME_DESCRIPTION,
                        Subscriptions.COLUMN_NAME_IMAGE_URL,
                        Subscriptions.COLUMN_NAME_LINK,
                        Subscriptions.COLUMN_NAME_LAST_UPDATED},
                null, null, null, null,
                Subscriptions.COLUMN_NAME_TITLE + " ASC");
        List<Subscription> subscriptions = new ArrayList<>();
        while (cursor.moveToNext()) {
            Subscription subscription = new Subscription();
            subscription.setUrl(cursor.getString(0));
            subscription.setTitle(cursor.getString(1));
            subscription.setDescription(cursor.getString(2));
            subscription.setImageUrl(cursor.getString(3));
            subscription.setLink(cursor.getString(4));
            subscription.setLastUpdated(Instant.ofEpochSecond(cursor.getLong(5)));
            subscriptions.add(subscription);
        }
        cursor.close();
        return subscriptions;
    }

    private ContentValues subscriptionToValues(Subscription subscription) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Subscriptions.COLUMN_NAME_DESCRIPTION, subscription.getDescription());
        contentValues.put(Subscriptions.COLUMN_NAME_IMAGE_URL, subscription.getImageUrl());
        contentValues.put(Subscriptions.COLUMN_NAME_LAST_UPDATED, subscription.getLastUpdated().toEpochMilli()/1000);
        contentValues.put(Subscriptions.COLUMN_NAME_LINK, subscription.getLink());
        contentValues.put(Subscriptions.COLUMN_NAME_TITLE, subscription.getTitle());
        contentValues.put(Subscriptions.COLUMN_NAME_URL, subscription.getUrl());
        return contentValues;
    }
}
