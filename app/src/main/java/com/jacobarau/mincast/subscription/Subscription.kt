package com.jacobarau.mincast.subscription

import org.threeten.bp.Instant

class Subscription {
    /**
     * URL of the RSS feed associated with this subscription.
     */
    var url: String = ""
    /**
     * Title of this subscription, from the RSS feed.
     */
    var title: String = ""
    /**
     * Link to the feed's website, from the RSS.
     */
    var link: String = ""
    /**
     * Phrase/sentence describing the feed, from the RSS.
     */
    var description: String = ""
    /**
     * If present in the RSS, a URL to an associated image.
     */
    var imageUrl: String? = null
    /**
     * The instant at which the podcast app has last fetched and parsed the RSS for this subscription.
     * This does not come from the RSS itself.
     *
     * It can be null if, for instance, a feed was just added--the new subscription will be added to
     * the database before we've fetched the RSS and filled out any fields yet.
     */
    var lastUpdated: Instant? = null

    override fun toString(): String {
        return "Subscription(url=$url, title=$title, link=$link, description=$description, imageUrl=$imageUrl, lastUpdated=$lastUpdated)"
    }
}