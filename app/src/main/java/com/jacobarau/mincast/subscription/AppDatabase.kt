package com.jacobarau.mincast.subscription

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import android.arch.persistence.room.Room
import android.arch.persistence.room.TypeConverters


@Database(entities = [Subscription::class, Item::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun subscriptionDao() : SubscriptionDao
    abstract fun itemDao() : ItemDao

    companion object {
        @JvmStatic
        private var sInstance: AppDatabase? = null

        @JvmStatic
        val DATABASE_NAME = "mincast"

        @JvmStatic
        fun getInstance(context: Context): AppDatabase {
            if (sInstance == null) {
                synchronized(AppDatabase::class) {
                    if (sInstance == null) {
                        sInstance = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DATABASE_NAME)
                                .build()
                    }
                }
            }
            return sInstance!!
        }
    }
}