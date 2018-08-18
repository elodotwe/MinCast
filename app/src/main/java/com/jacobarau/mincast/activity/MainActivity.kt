package com.jacobarau.mincast.activity

import android.arch.lifecycle.Observer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

        val subList = ArrayList<Subscription>()

        for (i in 0..10) {
            val subscription = Subscription()
            subscription.url = "http://blah.com$i"
            subscription.description = "blah description"
            subscription.title = "${i}BLAH TITLE"
            subList.add(subscription)
        }

        executor!!.execute {
            for (sub in subList) {
                try {
                    subscriptionDao!!.insertSubscription(sub)
                } catch (e: Exception) {
                    Log.e("MainActivity", "I can't even", e)
                }
            }
        }

        val subs = subscriptionDao!!.getSubscriptions()
        subs.observe(this, Observer { t -> if (t != null) {
            for (sub in t) {
                println(sub)
            }
        }
        })

    }
}
