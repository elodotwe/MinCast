package com.jacobarau.mincast.db;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.jacobarau.mincast.subscription.Subscription;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.threeten.bp.Instant;
import org.threeten.bp.temporal.ChronoUnit;

import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ConcurrentLinkedQueue;

@RunWith(RobolectricTestRunner.class)
public class PodcastDatabaseTest {
    @Test
    public void testInsert() {
        PodcastDatabaseHelper helper = new PodcastDatabaseHelper(RuntimeEnvironment.application.getApplicationContext());
        SQLiteDatabase database = helper.getWritableDatabase();
        PodcastDatabase podcastDatabase = new PodcastDatabase(database);

        final ConcurrentLinkedQueue<List<Subscription>> callbacks = new ConcurrentLinkedQueue<>();

        podcastDatabase.addSubscriptionsObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                @SuppressWarnings("unchecked") List<Subscription> subscriptions = (List<Subscription>) arg;
                System.out.println("Subs updated");
                System.out.println(Arrays.toString(subscriptions.toArray()));
                callbacks.add(subscriptions);
            }
        });

        Assert.assertEquals(1, callbacks.size());
        //noinspection ConstantConditions
        Assert.assertEquals(0, callbacks.poll().size());


        Subscription subscription = new Subscription();
        subscription.setLastUpdated(Instant.now().truncatedTo(ChronoUnit.SECONDS));
        subscription.setLink("blahblahblah");
        subscription.setImageUrl("an image");
        subscription.setDescription("description");
        subscription.setTitle("title");
        subscription.setUrl("key url");
        podcastDatabase.addSubscription(subscription);

        Assert.assertEquals(1, callbacks.size());
        List<Subscription> subscriptions = callbacks.poll();
        //noinspection ConstantConditions
        Assert.assertEquals(1, subscriptions.size());
        Assert.assertEquals(subscription, subscriptions.get(0));
    }
}
