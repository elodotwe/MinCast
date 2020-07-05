package com.jacobarau.mincast.subscription;

import java.util.Date;

public class Item {
    /**
     * Internal database ID for this item. Not tied to any value in the RSS.
     */
    public Integer id;

    /**
     * URL of parent Subscription object.
     */
    public String subscriptionUrl;

    /**
     * Human-readable title of this item, if present in RSS.
     *
     * Either title or description will be present at a bare minimum.
     */
    public String title;

    /**
     * Human-readable description of this item, if present in RSS.
     *
     * Either title or description will be present at a bare minimum.
     */
    public String description;

    /**
     * Date-timestamp of when this item was published; null if not present in the RSS
     */
    public Date publishDate;

    /**
     * If present in the RSS, the URL to the enclosure (typically the podcast audio goes here)
     */
    public String enclosureUrl;

    /**
     * MIME type of the enclosure.
     *
     * If enclosure URL is not null, this will be not null as well.
     */
    public String enclosureMimeType;

    /**
     * Length in bytes of the enclosure.
     *
     * If enclosure URL is not null, this will be not null as well.
     */
    public Integer enclosureLengthBytes;

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", subscriptionUrl='" + subscriptionUrl + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", publishDate=" + publishDate +
                ", enclosureUrl='" + enclosureUrl + '\'' +
                ", enclosureMimeType='" + enclosureMimeType + '\'' +
                ", enclosureLengthBytes=" + enclosureLengthBytes +
                '}';
    }
}
