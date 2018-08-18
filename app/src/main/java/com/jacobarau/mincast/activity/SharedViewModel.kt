package com.jacobarau.mincast.activity

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.jacobarau.mincast.subscription.*

class SharedViewModel(app: Application) : AndroidViewModel(app) {
    private val subscriptionDao = AppDatabase.getInstance(app).subscriptionDao()
    private val itemDao = AppDatabase.getInstance(app).itemDao()

    fun getSubscriptions() : LiveData<List<Subscription>> {
        return subscriptionDao.getSubscriptions()
    }

    fun getItems(subscriptionUrl: String) : LiveData<List<Item>> {
        return itemDao.getSubscriptionItems(subscriptionUrl)
    }
}