package com.jacobarau.mincast.subscription;

import org.threeten.bp.Instant;

import java.util.Objects;

public class Subscription {
    /**
     * URL of the RSS feed associated with this subscription.
     */
    private String url;

    /**
     * Title of this subscription, from the RSS feed.
     */
    private String title;

    /**
     * Link to the feed's website, from the RSS.
     */
    private String link;

    /**
     * Phrase/sentence describing the feed, from the RSS.
     */
    private String description;

    /**
     * If present in the RSS, a URL to an associated image.
     */
    private String imageUrl;

    /**
     * The instant at which the podcast app has last fetched and parsed the RSS for this subscription.
     * This does not come from the RSS itself.
     * <p>
     * It can be null if, for instance, a feed was just added--the new subscription will be added to
     * the database before we've fetched the RSS and filled out any fields yet.
     */
    private Instant lastUpdated;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Instant getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subscription that = (Subscription) o;
        return Objects.equals(url, that.url) &&
                Objects.equals(title, that.title) &&
                Objects.equals(link, that.link) &&
                Objects.equals(description, that.description) &&
                Objects.equals(imageUrl, that.imageUrl) &&
                Objects.equals(lastUpdated, that.lastUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, title, link, description, imageUrl, lastUpdated);
    }
}