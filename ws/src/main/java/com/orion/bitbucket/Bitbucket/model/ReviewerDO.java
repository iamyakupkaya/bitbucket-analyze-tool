package com.orion.bitbucket.Bitbucket.model;

public class ReviewerDO {
    private int id;
    private String name;
    private int totalReview;
    private int totalApprove;
    private int totalUnApprove;
    

    public ReviewerDO(int id, String name, int totalReview, int totalApprove, int totalUnApprove) {
        this.id = id;
        this.name = name;
        this.totalReview =totalReview;
        this.totalApprove = totalApprove;
        this.totalUnApprove = totalUnApprove;
    }

    public ReviewerDO() {

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getTotalReview() {
        return totalReview;
    }

    public int getTotalApprove() {
        return totalApprove;
    }

    public int getTotalUnApprove() {
        return totalUnApprove;
    }
  
    @Override
    public String toString() {
        return "ReviewerTotalReview{" +
                "id='" + id + '\'' +
                "name='" + name + '\'' +
                ", totalReview='" + totalReview + '\'' +
                ", totalApprove='" + totalApprove + '\'' +
                ", totalUnApprove='" + totalUnApprove +
                '}';
    }
}
