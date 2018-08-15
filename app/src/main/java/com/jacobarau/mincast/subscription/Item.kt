package com.jacobarau.mincast.subscription

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import org.threeten.bp.Instant

@Entity(foreignKeys = [ForeignKey(entity = Subscription::class,
        parentColumns = ["url"],
        childColumns = ["parentUrl"])])
class Item {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
    var parentUrl: String? = null
    var title: String? = null
    var description: String? = null
    var publishDate: Instant? = null
    var enclosureUrl: String? = null
    var enclosureMimeType: String? = null
    var enclosureLengthBytes: Int? = null

    override fun toString(): String {
        return "Item(id=$id, parentUrl=$parentUrl, title=$title, description=$description, publishDate=$publishDate, enclosureUrl=$enclosureUrl, enclosureMimeType=$enclosureMimeType, enclosureLengthBytes=$enclosureLengthBytes)"
    }
}