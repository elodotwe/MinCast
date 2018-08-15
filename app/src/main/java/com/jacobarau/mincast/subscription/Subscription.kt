package com.jacobarau.mincast.subscription

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.time.Instant

@Entity
class Subscription {
    @PrimaryKey
    var url: String? = null
    var title: String? = null
    var link: String? = null
    var description: String? = null
    var imageUrl: String? = null
    var lastUpdated: Instant? = null
}