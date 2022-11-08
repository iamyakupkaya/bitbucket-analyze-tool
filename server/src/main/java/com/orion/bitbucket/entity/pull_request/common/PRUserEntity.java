package com.orion.bitbucket.entity.pull_request.common;

import org.springframework.data.annotation.Id;

// this common class is used in values/author/user and values/reviewers/user
public class PRUserEntity {
    @Id
    private String id;
    private String name;
    private String emailAddress;
    private String displayName;
    private boolean active;
    private String slug;
    private String type;
    private PRLinksEntity links;

    public PRUserEntity() {}

    public PRUserEntity(String id, String name, String emailAddress, String displayName, boolean active, String slug, String type, PRLinksEntity links) {
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

    public PRLinksEntity getLinks() {
        return links;
    }

    public void setLinks(PRLinksEntity links) {
        this.links = links;
    }
}

