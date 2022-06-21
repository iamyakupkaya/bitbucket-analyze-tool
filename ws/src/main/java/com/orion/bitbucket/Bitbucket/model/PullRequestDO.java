package com.orion.bitbucket.Bitbucket.model;

import java.sql.Date;
import java.util.ArrayList;

public class PullRequestDO {
    private int prId;
    private String title;
    private String jiraId;
    private String state;
    private boolean closed;
    private String description;
    private String updatedDate;
    private Date createdDate;
    private Date closedDate;
    private String emailAddress;
    private String displayName;
    private String slug;
    private ArrayList<ReviewDO> reviewerList;
    private Long dayDiff;

    public PullRequestDO(int prId, String title, String jiraId, String state, boolean closed, String description, String updatedDate,
    Date createdDate, Date closedDate, String emailAddress, String displayName, String slug,
                         ArrayList<ReviewDO> reviewerList, Long dayDiff) {
        this.prId= prId;
        this.title = title;
        this.jiraId= jiraId;
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
        this.dayDiff = dayDiff;
    }

    public PullRequestDO (){

    }

    public int getPrId() {
        return prId;
    }

    public String getTitle() {
        return title;
    }

    public String getJiraId() {
        return jiraId;
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public Date getClosedDate() {
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
    public Long getDayDiff() {
        return dayDiff;
    }

    @Override
    public String toString() {
        return "PullRequestDO{" +
                "id='" + prId + '\'' +
                ", title='" + title + '\'' +
                ", jiraId='" + jiraId + '\'' +
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
                ", dayDiff ='" + dayDiff + '\'' +
                '}';
    }
}
