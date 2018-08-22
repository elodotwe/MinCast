package com.jacobarau.mincast.activity

import android.arch.lifecycle.Observer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.jacobarau.mincast.R
import com.jacobarau.mincast.subscription.AppDatabase
import com.jacobarau.mincast.subscription.ItemDao
import com.jacobarau.mincast.subscription.Subscription
import com.jacobarau.mincast.subscription.SubscriptionDao
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity(), URLDialogFragment.OnFragmentInteractionListener{
    var db: AppDatabase? = null
    var subscriptionDao: SubscriptionDao? = null
    var itemDao: ItemDao? = null

    val TAG = "MainActivity"

    var executor: Executor? = null
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item == null) return super.onOptionsItemSelected(item)
        val id = item.itemId
        if (id == R.id.action_add_podcast) {
            promptForURL()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun promptForURL() {
        URLDialogFragment.newInstance().show(supportFragmentManager, "podcast_url_prompt")
    }

    override fun onAddURL(url: String) {
        val newSub = Subscription()
        newSub.url = url
        Executors.newSingleThreadExecutor().execute {
            try {
                subscriptionDao!!.insertSubscription(newSub)
            } catch (e: Exception) {
                //TODO
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = AppDatabase.getInstance(this)
        subscriptionDao = db!!.subscriptionDao()
        itemDao = db!!.itemDao()

        setSupportActionBar(findViewById(R.id.toolbar))

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
