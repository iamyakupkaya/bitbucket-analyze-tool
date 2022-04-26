package com.orion.bitbucket.Bitbucket.model;

public class PullRequestDO {

    private String title;
    private String state;
    private boolean closed;
    private String description;
    private long updatedDate;
    private long createdDate;
    private long closedDate;
    private String emailAddress;
    private String displayName;
    private String slug;

    // description ve emailAddress eklenecek
    public PullRequestDO(String title, String state, boolean closed, long updatedDate, long createdDate, long closedDate, String displayName, String slug) {
        this.title = title;
        this.state = state;
        this.closed = closed;
        this.updatedDate = updatedDate;
        this.createdDate = createdDate;
        this.closedDate = closedDate;
        this.displayName = displayName;
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public String getState() {
        return state;
    }

    public boolean isClosed() {
        return closed;
    }

    public String getDescription() {
        return description;
    }

    public long getUpdatedDate() {
        return updatedDate;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public long getClosedDate() {
        return closedDate;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getSlug() {
        return slug;
    }

    @Override
    public String toString() {
        return "PullRequestDO{" +
                "title='" + title + '\'' +
                ", state='" + state + '\'' +
                ", closed=" + closed +
                ", description='" + description + '\'' +
                ", updatedDate=" + updatedDate +
                ", createdDate=" + createdDate +
                ", closedDate=" + closedDate +
                ", emailAddress='" + emailAddress + '\'' +
                ", displayName='" + displayName + '\'' +
                ", slug='" + slug + '\'' +
                '}';
    }
}


