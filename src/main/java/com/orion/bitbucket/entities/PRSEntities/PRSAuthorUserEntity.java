package com.orion.bitbucket.entities.PRSEntities;

import org.springframework.data.annotation.Id;

public class PRSAuthorUserEntity {
    @Id
    private String id;
    private String name;
    private String emailAddress;
    private String displayName;
    private boolean active;
    private String slug;
    private String type;
    private PRSLinksEntity links;

    public PRSAuthorUserEntity() {}

    public PRSAuthorUserEntity(String id, String name, String emailAddress, String displayName, boolean active, String slug, String type, PRSLinksEntity links) {
        this.id = id;
        this.name = name;
        this.emailAddress = emailAddress;
        this.displayName = displayName;
        this.active = active;
        this.slug = slug;
        this.type = type;
        this.links = links;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public PRSLinksEntity getLinks() {
        return links;
    }

    public void setLinks(PRSLinksEntity links) {
        this.links = links;
    }
}

