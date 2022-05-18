package com.orion.bitbucket.Bitbucket.model;

public class ReviewDO {
    private int id;
    private String displayName;
    private String emailAddress;
    private boolean approved;
    private String status;

    public ReviewDO(int id, String displayName, String emailAddress, String status, boolean approved) {
        this.id = id;
        this.displayName = displayName;
        this.emailAddress = emailAddress;
        this.status = status;
        this.approved = approved;
    }

    public int getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getStatus() {
        return status;
    }

    public boolean getApproved() {
        return approved;
    }

    @Override
    public String toString() {
        return "ReviewerDO{" +
                "id='" + id + '\'' +
                "displayName='" + displayName + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", approved=" + approved +
                ", status='" + status + '\'' +
                '}';
    }

    // PullRequestReviewRelation Schema
    public static class PullRequestReviewRelation {
        private PullRequestDO pullRequest;
        private ReviewDO review;

        public PullRequestReviewRelation(PullRequestDO pullRequest, ReviewDO review) {
            this.pullRequest = pullRequest;
            this.review = review;
        }

        public PullRequestDO getPullRequest() {
            return this.pullRequest;
        }

        public ReviewDO getReview() {
            return this.review;
        }
    }
}
