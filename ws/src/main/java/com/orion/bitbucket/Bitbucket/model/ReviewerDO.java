package com.orion.bitbucket.Bitbucket.model;

public class ReviewerDO {
	private String displayName;
	private int approved;
	private int unApproved;
	
	public ReviewerDO(String displayName, int approved, int unApproved) {
		this.displayName = displayName;
		this.approved = approved;
		this.unApproved = unApproved;
	}
	
	
	public String getDisplayName() {
		return displayName;
	}
	

	public int getApproved() {
		return approved;
	}
	
	public int getUnApproved() {
		return unApproved;
	}
	public void setUnApproved(int unApproved) {
		this.unApproved = unApproved;
	}
	
	

}
