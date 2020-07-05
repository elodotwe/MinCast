package com.jacobarau.mincast.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jacobarau.mincast.subscription.Item;
import com.jacobarau.mincast.subscription.Subscription;

import java.util.ArrayList;
import java.util.Date;
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
        database.delete(Subscriptions.TABLE_NAME, Subscriptions.COLUMN_NAME_URL + " = ?", new String[]{subscription.url});
    }

    public void updateSubscription(Subscription subscription) {
        database.update(Subscriptions.TABLE_NAME, subscriptionToValues(subscription),
                Subscriptions.COLUMN_NAME_URL + " = ?", new String[]{subscription.url});
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
            subscription.url = cursor.getString(0);
            subscription.title = cursor.getString(1);
            subscription.description = cursor.getString(2);
            subscription.imageUrl = cursor.getString(3);
            subscription.link = cursor.getString(4);
            subscription.lastUpdated = new Date(cursor.getLong(5) * 1000);
            subscriptions.add(subscription);
        }
        cursor.close();
        return subscriptions;
    }

    private ContentValues subscriptionToValues(Subscription subscription) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Subscriptions.COLUMN_NAME_DESCRIPTION, subscription.description);
        contentValues.put(Subscriptions.COLUMN_NAME_IMAGE_URL, subscription.imageUrl);
        contentValues.put(Subscriptions.COLUMN_NAME_LAST_UPDATED, subscription.lastUpdated.getTime()/1000);
        contentValues.put(Subscriptions.COLUMN_NAME_LINK, subscription.link);
        contentValues.put(Subscriptions.COLUMN_NAME_TITLE, subscription.title);
        contentValues.put(Subscriptions.COLUMN_NAME_URL, subscription.url);
        return contentValues;
    }

    public List<Item> getItems() {
        return null;
    }

    public void addItem(Item item) {
        throw new RuntimeException("Not implemented yet");
    }

    public void deleteItem(Item item) {
        throw new RuntimeException("Not implemented yet");
    }

    public void updateItem(Item item) {
        throw new RuntimeException("Not implemented yet");
    }
}
