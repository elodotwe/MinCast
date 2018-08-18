package com.jacobarau.mincast.activity

import android.arch.lifecycle.Observer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.jacobarau.mincast.R
import com.jacobarau.mincast.subscription.AppDatabase
import com.jacobarau.mincast.subscription.ItemDao
import com.jacobarau.mincast.subscription.Subscription
import com.jacobarau.mincast.subscription.SubscriptionDao
import org.threeten.bp.Instant
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    var db: AppDatabase? = null
    var subscriptionDao: SubscriptionDao? = null
    var itemDao: ItemDao? = null

    val TAG = "MainActivity"

    var executor: Executor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = AppDatabase.getInstance(this)
        subscriptionDao = db!!.subscriptionDao()
        itemDao = db!!.itemDao()

        executor = Executors.newSingleThreadExecutor()

        var subscription: Subscription = Subscription()
        subscription.url = "http://blah.com"
        subscription.description = "blah description"
        subscription.title = "BLAH TITLE"
        executor!!.execute { subscriptionDao!!.insertSubscription(subscription) }

        var subscription2 = Subscription()
        subscription2.title = "title 2"
        subscription2.url = "Anotherone.com"
        subscription2.lastUpdated = Instant.now()
        executor!!.execute { subscriptionDao!!.insertSubscription(subscription2) }

        val subs = subscriptionDao!!.getSubscriptions()
        subs.observe(this, Observer { t -> if (t != null) {
            for (sub in t) {
                println(sub)
            }
        }
        })

    }
}
