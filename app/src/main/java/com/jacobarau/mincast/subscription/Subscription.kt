package com.jacobarau.mincast.subscription

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import org.threeten.bp.Instant

@Entity
class Subscription {
    @PrimaryKey
    @NotNull
    var url: String? = null
    var title: String? = null
    var link: String? = null
    var description: String? = null
    var imageUrl: String? = null
    var lastUpdated: Instant? = null

    override fun toString(): String {
        return "Subscription(url=$url, title=$title, link=$link, description=$description, imageUrl=$imageUrl, lastUpdated=$lastUpdated)"
    }
}