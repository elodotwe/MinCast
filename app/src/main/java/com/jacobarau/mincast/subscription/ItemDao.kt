package com.jacobarau.mincast.subscription

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

@Dao
interface ItemDao {
    @Insert
    fun insertItem(item: Item)

    @Update
    fun updateItem(item: Item)

    @Delete
    fun deleteItem(item: Item)

    @Query("SELECT * FROM item WHERE parentUrl == :subscriptionUrl")
    fun getSubscriptionItems(subscriptionUrl: String): LiveData<List<Item>>
}