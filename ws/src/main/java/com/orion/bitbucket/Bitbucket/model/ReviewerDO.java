package com.orion.bitbucket.Bitbucket.model;

public class ReviewerDO {
    private String displayName;
    private String emailAddress;
    private boolean approved;
    private String status;

    public ReviewerDO(String displayName, String emailAddress, String status, boolean approved) {
        this.displayName = displayName;
        this.emailAddress = emailAddress;
        this.status = status;
        this.approved = approved;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmailAddress() {
        return displayName;
    }

    public boolean getApproved() {
        return approved;
    }

    @Override
    public String toString() {
        return "ReviewerDO{" +
                "displayName='" + displayName + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", approved=" + approved +
                ", status='" + status + '\'' +
                '}';
    }
}
