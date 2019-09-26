package com.jacobarau.mincast.db;

import android.database.sqlite.SQLiteDatabase;

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
    public void testInsert() {
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
}
