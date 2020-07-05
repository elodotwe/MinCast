package com.jacobarau.mincast.subscription;

import java.util.Date;

public class Subscription {
    /**
     * URL of the RSS feed associated with this subscription.
     */
    public String url;

    /**
     * Title of this subscription, from the RSS feed.
     */
    public String title;

    /**
     * Link to the feed's website, from the RSS.
     */
    public String link;

    /**
     * Phrase/sentence describing the feed, from the RSS.
     */
    public String description;

    /**
     * If present in the RSS, a URL to an associated image.
     */
    public String imageUrl;

    /**
     * The instant at which the podcast app has last fetched and parsed the RSS for this subscription.
     * This does not come from the RSS itself.
     * <p>
     * It can be null if, for instance, a feed was just added--the new subscription will be added to
     * the database before we've fetched the RSS and filled out any fields yet.
     */
    public Date lastUpdated;

    @SuppressWarnings("NullableProblems")
    @Override
    public String toString() {
        return "Subscription{" +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", lastUpdated=" + lastUpdated +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Subscription that = (Subscription) o;

        if (url != null ? !url.equals(that.url) : that.url != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (link != null ? !link.equals(that.link) : that.link != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null)
            return false;
        if (imageUrl != null ? !imageUrl.equals(that.imageUrl) : that.imageUrl != null)
            return false;
        return lastUpdated != null ? lastUpdated.equals(that.lastUpdated) : that.lastUpdated == null;
    }

    @Override
    public int hashCode() {
        int result = url != null ? url.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (link != null ? link.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
        result = 31 * result + (lastUpdated != null ? lastUpdated.hashCode() : 0);
        return result;
    }
}