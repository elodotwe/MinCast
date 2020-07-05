package com.jacobarau.mincast.subscription;

import java.util.Date;

public class Item {
    /**
     * Internal database ID for this item. Not tied to any value in the RSS.
     */
    private Integer id;

    /**
     * URL of parent Subscription object.
     */
    private String subscriptionUrl;

    /**
     * Human-readable title of this item, if present in RSS.
     *
     * Either title or description will be present at a bare minimum.
     */
    private String title;

    /**
     * Human-readable description of this item, if present in RSS.
     *
     * Either title or description will be present at a bare minimum.
     */
    private String description;

    /**
     * Date-timestamp of when this item was published; null if not present in the RSS
     */
    private Date publishDate;

    /**
     * If present in the RSS, the URL to the enclosure (typically the podcast audio goes here)
     */
    private String enclosureUrl;

    /**
     * MIME type of the enclosure.
     *
     * If enclosure URL is not null, this will be not null as well.
     */
    private String enclosureMimeType;

    /**
     * Length in bytes of the enclosure.
     *
     * If enclosure URL is not null, this will be not null as well.
     */
    private Integer enclosureLengthBytes;

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

    public Integer getId() {
        return id;
    }

    public String getSubscriptionUrl() {
        return subscriptionUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public String getEnclosureUrl() {
        return enclosureUrl;
    }

    public String getEnclosureMimeType() {
        return enclosureMimeType;
    }

    public Integer getEnclosureLengthBytes() {
        return enclosureLengthBytes;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setSubscriptionUrl(String subscriptionUrl) {
        this.subscriptionUrl = subscriptionUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public void setEnclosureUrl(String enclosureUrl) {
        this.enclosureUrl = enclosureUrl;
    }

    public void setEnclosureMimeType(String enclosureMimeType) {
        this.enclosureMimeType = enclosureMimeType;
    }

    public void setEnclosureLengthBytes(Integer enclosureLengthBytes) {
        this.enclosureLengthBytes = enclosureLengthBytes;
    }
}
