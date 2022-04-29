package com.orion.bitbucket.Bitbucket.model;

public class ReviewerDO {
	private String displayName;
	private String emailAddress;
	private String lastReviewedCommit;
	private boolean approved;
	private String status;

	public ReviewerDO(String displayName, String emailAddress, String lastReviewedCommit, boolean approved, String status){
		this.displayName=displayName;
		this.emailAddress=emailAddress;
		this.lastReviewedCommit=lastReviewedCommit;
		this.approved=approved;
		this.status=status;
	}


	public String getDisplayName() {
        return displayName;
    }

	public String getEmailAddress() {
        return displayName;
    }

	public String getLastReviewedCommit(){
		return lastReviewedCommit;
	}

	public boolean getApproved(){
		return approved;
	}

	public String getStatus(){
		return status;
	}


	@Override
    public String toString() {
        return "ReviewerDO{" +
                "displayName='" + displayName + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", lastReviewedCommit='" + lastReviewedCommit + '\'' +
				", approved='" + approved + '\'' +
				", status='" + status + '\'' +
                '}';
    }
}
