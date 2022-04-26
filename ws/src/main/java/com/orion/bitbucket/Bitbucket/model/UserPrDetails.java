package com.orion.bitbucket.Bitbucket.model;

public class UserPrDetails {

	private String title;
	private String status;
	private int prId;
	// Date ile değişecek
	private String date;
	private int totalPRs;
	private int totalMerges;
	private int totalOpen;
	private int totalDecline;
	
	 private static UserPrDetails instance = new UserPrDetails();
	   public static UserPrDetails getInstance() {
		   return instance;
	   }
	   
	public UserPrDetails(String title, String status, int prId , String date,int totalPRs, int totalMerges, int totalOpen, int totalDecline) {
		super();
		
		this.title = title;
		this.status = status;
		this.prId = prId;
		this.date=date;
		this.totalPRs=totalPRs;
		this.totalMerges=totalMerges;
		this.totalOpen=totalOpen;
		this.totalDecline=totalDecline;
	}
	
	public UserPrDetails() {
	
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getPrId() {
		return prId;
	}
	public void setPrId(int prId) {
		this.prId = prId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getTotalPRs() {
		return totalPRs;
	}

	public void setTotalPRs(int totalPRs) {
		this.totalPRs = totalPRs;
	}

	public int getTotalMerges() {
		return totalMerges;
	}

	public void setTotalMerges(int totalMerges) {
		this.totalMerges = totalMerges;
	}

	public int getTotalOpen() {
		return totalOpen;
	}

	public void setTotalOpen(int totalOpen) {
		this.totalOpen = totalOpen;
	}

	public int getTotalDecline() {
		return totalDecline;
	}

	public void setTotalDecline(int totalDecline) {
		this.totalDecline = totalDecline;
	}
	
	
}
