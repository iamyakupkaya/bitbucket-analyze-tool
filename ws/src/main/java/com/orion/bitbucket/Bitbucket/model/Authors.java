package com.orion.bitbucket.Bitbucket.model;

public class Author {

    private String name;
    private int totalPRs;
    private int totalMergedPRs;
    private int totalOpenPRs;
    private int totalDeclinedPRs;

    public Author(String name, int totalPRs, int totalMergedPRs, int totalOpenPRs, int totalDeclinedPRs) {
        this.name = name;
        this.totalPRs = totalPRs;
        this.totalMergedPRs=totalMergedPRs;
        this.totalOpenPRs = totalOpenPRs;
        this.totalDeclinedPRs = totalDeclinedPRs;
    }

    public Author(){

    }


    public String getName() {
        return name;
    }

    public int getTotalPRs(){
        return totalPRs;
    }

    public int getTotalMergedPRs(){
        return totalMergedPRs;
    }

    public int getTotalOpenPRs(){
        return totalOpenPRs;
    }

    public int getTotalDeclinedPRs(){
        return totalDeclinedPRs;
    }



    @Override
    public String toString() {
        return "AuthorTotalPRs{" +
                "name='" + name + '\'' +
                ", totalPRs='" + totalPRs + '\'' +
                ", totalMergedPRs='" + totalMergedPRs +
                ", totalOpenPRs='" + totalOpenPRs + '\'' +
                ", totalDeclinedPRs='" + totalDeclinedPRs +
                '}';
    }
}
