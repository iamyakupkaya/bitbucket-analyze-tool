package com.orion.bitbucket.Bitbucket.model;

public class Reviewers {
	private String displayName;
	private int approved;
	private int unApproved;
	
	public Reviewers(String displayName, int approved, int unApproved) {
		super();
		this.displayName = displayName;
		this.approved = approved;
		this.unApproved = unApproved;
	}
	
	 private static Reviewers instance = new Reviewers();
	   public static Reviewers getInstance() {
		   return instance;
	   }
	   
	public Reviewers() {
		
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public int getApproved() {
		return approved;
	}
	public void setApproved(int approved) {
		this.approved = approved;
	}
	public int getUnApproved() {
		return unApproved;
	}
	public void setUnApproved(int unApproved) {
		this.unApproved = unApproved;
	}
	
	

}
