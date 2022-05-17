package com.orion.bitbucket.Bitbucket.model;

import java.util.ArrayList;

public class PullRequestDO {

    private int prId;
    private String title;
    private String state;
    private boolean closed;
    private String description;
    private String updatedDate;
    private String createdDate;
    private String closedDate;
    private String emailAddress;
    private String displayName;
    private String slug;
    private ArrayList<ReviewDO> reviewerList;

    public PullRequestDO(int prId, String title, String state, boolean closed, String description, String updatedDate,
    String createdDate, String closedDate, String emailAddress, String displayName, String slug,
                         ArrayList<ReviewDO> reviewerList) {
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

    public PullRequestDO (){

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

    public String getUpdatedDate() {
        return updatedDate;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getClosedDate() {
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

    public ArrayList<ReviewDO> getReviewerList() {
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
