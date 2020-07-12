package com.jacobarau.mincast.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.jacobarau.mincast.subscription.Item;
import com.jacobarau.mincast.subscription.Subscription;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class PodcastDatabaseTest {
    private Date getTruncatedDate() {
        Date date = new Date();
        // Database is only specced to store date rounded to the nearest second, so we need to
        // truncate the millisecond portion of the Date we feed it.
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MILLISECOND, 0);
        date = calendar.getTime();
        return date;
    }

    @Test
    public void testInsertAndGetSubscription() {
        PodcastDatabaseHelper helper = new PodcastDatabaseHelper(RuntimeEnvironment.application.getApplicationContext());
        SQLiteDatabase database = helper.getWritableDatabase();
        PodcastDatabase podcastDatabase = new PodcastDatabase(database);

        Subscription subscription = new Subscription("a url");

        subscription.lastUpdated = getTruncatedDate();
        subscription.link = "blahblahblah";
        subscription.imageUrl = "an image";
        subscription.description = "description";
        subscription.title = "title";
        podcastDatabase.save(subscription);

        List<Subscription> subscriptions = podcastDatabase.getSubscriptions();
        Assert.assertEquals(1, subscriptions.size());
        Assert.assertEquals(subscription, subscriptions.get(0));
    }

    @Test
    public void testCRUDItem() {
        PodcastDatabaseHelper helper = new PodcastDatabaseHelper(RuntimeEnvironment.application.getApplicationContext());
        SQLiteDatabase database = helper.getWritableDatabase();
        PodcastDatabase podcastDatabase = new PodcastDatabase(database);

        Subscription subscription = new Subscription("a url");
        podcastDatabase.save(subscription);

        Item item = new Item();
        item.description = "description";
        item.enclosureLengthBytes = 123;
        item.enclosureMimeType = "mime";
        item.enclosureUrl = "enclosure_url";
        item.publishDate = getTruncatedDate();
        item.title = "title";
        item.subscriptionId = subscription.id;
        podcastDatabase.save(item);

        List<Item> items = podcastDatabase.getItems();
        Assert.assertEquals(1, items.size());
        Assert.assertEquals(item, items.get(0));

        item.description = "description++";
        item.enclosureLengthBytes = 124;
        item.enclosureMimeType = "mime2";
        item.enclosureUrl = "enclosure_url2";
        item.publishDate = getTruncatedDate();
        item.title = "title2";
        podcastDatabase.save(item);

        items = podcastDatabase.getItems();
        Assert.assertEquals(1, items.size());
        Assert.assertEquals(item, items.get(0));

        podcastDatabase.delete(item);

        items = podcastDatabase.getItems();
        Assert.assertEquals(0, items.size());
    }
}
