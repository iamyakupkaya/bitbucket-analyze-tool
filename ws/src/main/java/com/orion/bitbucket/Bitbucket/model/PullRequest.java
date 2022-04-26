package com.orion.bitbucket.Bitbucket.model;

public class PullRequest {
   
   private String authorName;
   private int total;
   private int mergedPRs;
   private int declinedPRs;
   private int openPRs;
   
   private static PullRequest instance = new PullRequest();
   public static PullRequest getInstance() {
	   return instance;
   }
   
   public PullRequest(String authorName, int total, int mergedPRs, int declinedPRs, int openPRs) {
      super();
      this.authorName = authorName;
      this.total = total;
      this.mergedPRs = mergedPRs;
      this.declinedPRs = declinedPRs;
      this.openPRs = openPRs;
   }
   
   public PullRequest() {
   }
   
   public String getAuthorName() {
      return authorName;
   }
   public void setAuthorName(String authorName) {
      this.authorName = authorName;
   }
   public int getTotal() {
      return total;
   }
   public void setTotal(int total) {
      this.total = total;
   }
   public int getMergedPRs() {
      return mergedPRs;
   }
   public void setMergedPRs(int mergedPRs) {
      this.mergedPRs = mergedPRs;
   }
   public int getDeclinedPRs() {
      return declinedPRs;
   }
   public void setDeclinedPRs(int declinedPRs) {
      this.declinedPRs = declinedPRs;
   }
   public int getOpenPRs() {
      return openPRs;
   }
   public void setOpenPRs(int openPRs) {
      this.openPRs = openPRs;
   }
   
   
}
