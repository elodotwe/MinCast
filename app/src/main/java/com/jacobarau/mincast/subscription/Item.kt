package com.jacobarau.mincast.subscription

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import org.threeten.bp.Instant

@Entity(foreignKeys = [ForeignKey(entity = Subscription::class,
        parentColumns = ["url"],
        childColumns = ["parentUrl"])])
class Item {
    /**
     * Internal database ID for this item. Not tied to any value in the RSS.
     */
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    /**
     * The URL of the Subscription's URL, the URL to the RSS file we fetched.
     */
    var parentUrl: String = ""

    /**
     * Human-readable title of this item, if present in RSS.
     *
     * Either title or description will be present at a bare minimum.
     */
    var title: String? = null

    /**
     * Human-readable description of this item, if present in RSS.
     *
     * Either title or description will be present at a bare minimum.
     */
    var description: String? = null

    /**
     * Date-timestamp of when this item was published; null if not present in the RSS
     */
    var publishDate: Instant? = null

    /**
     * If present in the RSS, the URL to the enclosure (typically the podcast audio goes here)
     */
    var enclosureUrl: String? = null

    /**
     * MIME type of the enclosure.
     *
     * If enclosure URL is not null, this will be not null as well.
     */
    var enclosureMimeType: String? = null

    /**
     * Length in bytes of the enclosure.
     *
     * If enclosure URL is not null, this will be not null as well.
     */
    var enclosureLengthBytes: Int? = null

    override fun toString(): String {
        return "Item(id=$id, parentUrl=$parentUrl, title=$title, description=$description, publishDate=$publishDate, enclosureUrl=$enclosureUrl, enclosureMimeType=$enclosureMimeType, enclosureLengthBytes=$enclosureLengthBytes)"
    }
}