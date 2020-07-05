package com.jacobarau.mincast.db;

import android.database.sqlite.SQLiteDatabase;

import com.jacobarau.mincast.subscription.Item;
import com.jacobarau.mincast.subscription.Subscription;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class PodcastDatabaseTest {
    @Test
    public void testInsertAndGetSubscription() {
        PodcastDatabaseHelper helper = new PodcastDatabaseHelper(RuntimeEnvironment.application.getApplicationContext());
        SQLiteDatabase database = helper.getWritableDatabase();
        PodcastDatabase podcastDatabase = new PodcastDatabase(database);

        Subscription subscription = new Subscription();
        Date date = new Date();
        // Database is only specced to store date rounded to the nearest second, so we need to
        // truncate the millisecond portion of the Date we feed it.
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MILLISECOND, 0);
        date = calendar.getTime();

        subscription.lastUpdated = date;
        subscription.link = "blahblahblah";
        subscription.imageUrl = "an image";
        subscription.description = "description";
        subscription.title = "title";
        subscription.url = "key url";
        podcastDatabase.addSubscription(subscription);

        List<Subscription> subscriptions = podcastDatabase.getSubscriptions();
        Assert.assertEquals(1, subscriptions.size());
        Assert.assertEquals(subscription, subscriptions.get(0));
    }

    @Test
    public void testCRUDItem() {
        PodcastDatabaseHelper helper = new PodcastDatabaseHelper(RuntimeEnvironment.application.getApplicationContext());
        SQLiteDatabase database = helper.getWritableDatabase();
        PodcastDatabase podcastDatabase = new PodcastDatabase(database);

        Item item = new Item();
        item.description = "description";
        item.enclosureLengthBytes = 123;
        item.enclosureMimeType = "mime";
        item.enclosureUrl = "enclosure_url";
        item.id = 12;
        item.publishDate = new Date();
        item.title = "title";
        item.subscriptionUrl = "sub_url";
        podcastDatabase.addItem(item);

        List<Item> items = podcastDatabase.getItems();
        Assert.assertEquals(1, items.size());
        Assert.assertEquals(item, items.get(0));

        item.description = "description++";
        item.enclosureLengthBytes = 124;
        item.enclosureMimeType = "mime2";
        item.enclosureUrl = "enclosure_url2";
        item.publishDate = new Date();
        item.title = "title2";
        item.subscriptionUrl = "sub_url2";
        podcastDatabase.updateItem(item);

        items = podcastDatabase.getItems();
        Assert.assertEquals(1, items.size());
        Assert.assertEquals(item, items.get(0));

        podcastDatabase.deleteItem(item);

        items = podcastDatabase.getItems();
        Assert.assertEquals(0, items.size());
    }
}
