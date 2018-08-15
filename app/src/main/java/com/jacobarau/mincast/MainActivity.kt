package com.jacobarau.mincast

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.jacobarau.mincast.subscription.AppDatabase
import com.jacobarau.mincast.subscription.ItemDao
import com.jacobarau.mincast.subscription.SubscriptionDao

class MainActivity : AppCompatActivity() {

    var db: AppDatabase? = null
    var subscriptionDao: SubscriptionDao? = null
    var itemDao: ItemDao? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = AppDatabase.getInstance(this)
        subscriptionDao = db!!.subscriptionDao()
        itemDao = db!!.itemDao()

        
    }
}
