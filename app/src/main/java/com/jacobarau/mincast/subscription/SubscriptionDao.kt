package com.jacobarau.mincast.subscription

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

@Dao
interface SubscriptionDao {
    @Insert
    fun insertSubscription(subscription: Subscription)

    @Update
    fun updateSubscription(subscription: Subscription)

    @Delete
    fun deleteSubscription(subscription: Subscription)

    @Query("SELECT * FROM subscription")
    fun getSubscriptions(): LiveData<List<Subscription>>
}