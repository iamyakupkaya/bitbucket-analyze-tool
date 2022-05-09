package com.orion.bitbucket.Bitbucket.model;

import java.util.ArrayList;

public class PullRequestDO {

    private int prId;
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
    private ArrayList<ReviewerDO> reviewerList;

    public PullRequestDO(int prId, String title, String state, boolean closed, String description, long updatedDate,
                         long createdDate, long closedDate, String emailAddress, String displayName, String slug,
                         ArrayList<ReviewerDO> reviewerList) {
        this.prId= prId;
        this.title = title;
        this.state = state;
        this.closed = closed;
        this.description = description;
        this.updatedDate = updatedDate;
        this.createdDate = createdDate;
        this.closedDate = closedDate;
        this.emailAddress = emailAddress;
        this.displayName = displayName;
        this.slug = slug;
        this.reviewerList = reviewerList;
    }

    public int getPrId() {
        return prId;
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

    public ArrayList<ReviewerDO> getReviewerList() {
        return reviewerList;
    }

    @Override
    public String toString() {
        return "PullRequestDO{" +
                "id='" + prId + '\'' +
                ", title='" + title + '\'' +
                ", state='" + state + '\'' +
                ", closed=" + closed +
                ", description='" + description + '\'' +
                ", updatedDate=" + updatedDate +
                ", createdDate=" + createdDate +
                ", closedDate=" + closedDate +
                ", emailAddress='" + emailAddress + '\'' +
                ", displayName='" + displayName + '\'' +
                ", slug='" + slug + '\'' +
                ", Reviewers = {'" + reviewerList + "}" + '\'' +
                '}';
    }
}
