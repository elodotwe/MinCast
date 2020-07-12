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

    public void save(Subscription subscription) {
        if (subscription.id != null) {
            int affected = database.update(Subscriptions.TABLE_NAME,
                    subscriptionToValues(subscription),
                    Subscriptions._ID + " = ?", new String[]{String.valueOf(subscription.id)});
            if (affected != 1) {
                throw new RuntimeException("Updating Subscription didn't affect 1 row as expected; sub: " + subscription);
            }
        } else {
            subscription.id = database.insertOrThrow(Subscriptions.TABLE_NAME, null, subscriptionToValues(subscription));
        }
    }

    public void deleteSubscription(Subscription subscription) {
        database.delete(Subscriptions.TABLE_NAME, Subscriptions._ID + " = ?", new String[]{String.valueOf(subscription.id)});
    }

    private Date getDate(Cursor cursor, int columnIndex) {
        if (cursor.isNull(columnIndex)) {
            return null;
        }
        return new Date(cursor.getLong(columnIndex) * 1000);
    }

    private String getString(Cursor cursor, int columnIndex) {
        if (cursor.isNull(columnIndex)) {
            return null;
        }
        return cursor.getString(columnIndex);
    }

    private Integer getInteger(Cursor cursor, @SuppressWarnings("SameParameterValue") int columnIndex) {
        if (cursor.isNull(columnIndex)) {
            return null;
        }
        return cursor.getInt(columnIndex);
    }

    public List<Subscription> getSubscriptions() {
        Cursor cursor = database.query(Subscriptions.TABLE_NAME,
                new String[]{
                        Subscriptions._ID,
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
            Subscription subscription = new Subscription(cursor.getString(1));
            subscription.id = cursor.getLong(0);
            subscription.title = getString(cursor, 2);
            subscription.description = getString(cursor, 3);
            subscription.imageUrl = getString(cursor, 4);
            subscription.link = getString(cursor, 5);
            subscription.lastUpdated = getDate(cursor, 6);
            subscriptions.add(subscription);
        }
        cursor.close();
        return subscriptions;
    }

    private Long dateToLong(Date date) {
        if (date == null) {
            return null;
        }
        return date.getTime() / 1000;
    }

    private ContentValues subscriptionToValues(Subscription subscription) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Subscriptions.COLUMN_NAME_DESCRIPTION, subscription.description);
        contentValues.put(Subscriptions.COLUMN_NAME_IMAGE_URL, subscription.imageUrl);
        contentValues.put(Subscriptions.COLUMN_NAME_LAST_UPDATED, dateToLong(subscription.lastUpdated));
        contentValues.put(Subscriptions.COLUMN_NAME_LINK, subscription.link);
        contentValues.put(Subscriptions.COLUMN_NAME_TITLE, subscription.title);
        contentValues.put(Subscriptions.COLUMN_NAME_URL, subscription.url);
        return contentValues;
    }

    private ContentValues itemToValues(Item item) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PodcastDatabaseContract.Items.COLUMN_NAME_DESCRIPTION, item.description);
        contentValues.put(PodcastDatabaseContract.Items.COLUMN_NAME_ENCLOSURE_LENGTH_BYTES, item.enclosureLengthBytes);
        contentValues.put(PodcastDatabaseContract.Items.COLUMN_NAME_ENCLOSURE_MIME_TYPE, item.enclosureMimeType);
        contentValues.put(PodcastDatabaseContract.Items.COLUMN_NAME_ENCLOSURE_URL, item.enclosureUrl);
        contentValues.put(PodcastDatabaseContract.Items.COLUMN_NAME_PUBLISH_DATE, dateToLong(item.publishDate));
        contentValues.put(PodcastDatabaseContract.Items.COLUMN_NAME_SUBSCRIPTION_ID, item.subscriptionId);
        contentValues.put(PodcastDatabaseContract.Items.COLUMN_NAME_TITLE, item.title);
        return contentValues;
    }

    public List<Item> getItems() {
        Cursor cursor = database.query(PodcastDatabaseContract.Items.TABLE_NAME,
                new String[]{
                        PodcastDatabaseContract.Items._ID,
                        PodcastDatabaseContract.Items.COLUMN_NAME_SUBSCRIPTION_ID,
                        PodcastDatabaseContract.Items.COLUMN_NAME_TITLE,
                        PodcastDatabaseContract.Items.COLUMN_NAME_DESCRIPTION,
                        PodcastDatabaseContract.Items.COLUMN_NAME_PUBLISH_DATE,
                        PodcastDatabaseContract.Items.COLUMN_NAME_ENCLOSURE_URL,
                        PodcastDatabaseContract.Items.COLUMN_NAME_ENCLOSURE_LENGTH_BYTES,
                        PodcastDatabaseContract.Items.COLUMN_NAME_ENCLOSURE_MIME_TYPE},
                null, null, null, null,
                PodcastDatabaseContract.Items.COLUMN_NAME_TITLE + " ASC");
        List<Item> items = new ArrayList<>();
        while (cursor.moveToNext()) {
            Item item = new Item();
            item.id = cursor.getLong(0);
            item.subscriptionId = cursor.getLong(1);
            item.title = getString(cursor, 2);
            item.description = getString(cursor, 3);
            item.publishDate = getDate(cursor, 4);
            item.enclosureUrl = getString(cursor, 5);
            item.enclosureLengthBytes = getInteger(cursor, 6);
            item.enclosureMimeType = getString(cursor, 7);
            items.add(item);
        }
        cursor.close();
        return items;
    }

    public void save(Item item) {
        if (item.id != null) {
            int affected = database.update(PodcastDatabaseContract.Items.TABLE_NAME,
                    itemToValues(item),
                    PodcastDatabaseContract.Items._ID + " = ?", new String[]{String.valueOf(item.id)});
            if (affected != 1) {
                throw new RuntimeException("Updating Item didn't affect 1 row as expected; item: " + item);
            }
        } else {
            item.id = database.insertOrThrow(PodcastDatabaseContract.Items.TABLE_NAME, null, itemToValues(item));
        }
    }

    public void delete(Item item) {
        database.delete(PodcastDatabaseContract.Items.TABLE_NAME, PodcastDatabaseContract.Items._ID + " = ?", new String[]{String.valueOf(item.id)});
    }
}
