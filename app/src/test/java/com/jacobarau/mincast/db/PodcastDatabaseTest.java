package com.jacobarau.mincast.db;

import android.database.sqlite.SQLiteDatabase;

import com.jacobarau.mincast.subscription.Item;
import com.jacobarau.mincast.subscription.Subscription;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.threeten.bp.Instant;
import org.threeten.bp.temporal.ChronoUnit;

import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class PodcastDatabaseTest {
    @Test
    public void testInsertAndGetSubscription() {
        PodcastDatabaseHelper helper = new PodcastDatabaseHelper(RuntimeEnvironment.application.getApplicationContext());
        SQLiteDatabase database = helper.getWritableDatabase();
        PodcastDatabase podcastDatabase = new PodcastDatabase(database);

        Subscription subscription = new Subscription();
        subscription.setLastUpdated(Instant.now().truncatedTo(ChronoUnit.SECONDS));
        subscription.setLink("blahblahblah");
        subscription.setImageUrl("an image");
        subscription.setDescription("description");
        subscription.setTitle("title");
        subscription.setUrl("key url");
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
        item.setDescription("description");
        item.setEnclosureLengthBytes(123);
        item.setEnclosureMimeType("mime");
        item.setEnclosureUrl("enclosure_url");
        item.setId(12);
        item.setPublishDate(Instant.now().truncatedTo(ChronoUnit.SECONDS));
        item.setTitle("title");
        item.setSubscriptionUrl("sub_url");
        podcastDatabase.addItem(item);

        List<Item> items = podcastDatabase.getItems();
        Assert.assertEquals(1, items.size());
        Assert.assertEquals(item, items.get(0));

        item.setDescription("description++");
        item.setEnclosureLengthBytes(124);
        item.setEnclosureMimeType("mime2");
        item.setEnclosureUrl("enclosure_url2");
        item.setPublishDate(Instant.now().truncatedTo(ChronoUnit.SECONDS));
        item.setTitle("title2");
        item.setSubscriptionUrl("sub_url2");
        podcastDatabase.updateItem(item);

        items = podcastDatabase.getItems();
        Assert.assertEquals(1, items.size());
        Assert.assertEquals(item, items.get(0));

        podcastDatabase.deleteItem(item);

        items = podcastDatabase.getItems();
        Assert.assertEquals(0, items.size());
    }
}
