package com.orion.bitbucket.Bitbucket.model;

public class AuthorDO {

    private int id;
    private String name;
    private int totalPRs;
    private int totalMergedPRs;
    private int totalOpenPRs;
    private int totalDeclinedPRs;

    public AuthorDO(int id,String name, int totalPRs, int totalMergedPRs, int totalOpenPRs, int totalDeclinedPRs) {
        this.id = id;
        this.name = name;
        this.totalPRs = totalPRs;
        this.totalMergedPRs = totalMergedPRs;
        this.totalOpenPRs = totalOpenPRs;
        this.totalDeclinedPRs = totalDeclinedPRs;
    }

    public AuthorDO() {

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getTotalPRs() {
        return totalPRs;
    }

    public int getTotalMergedPRs() {
        return totalMergedPRs;
    }

    public int getTotalOpenPRs() {
        return totalOpenPRs;
    }

    public int getTotalDeclinedPRs() {
        return totalDeclinedPRs;
    }


    @Override
    public String toString() {
        return "AuthorTotalPRs{" +
                "id='" + id + '\'' +
                "name='" + name + '\'' +
                ", totalPRs='" + totalPRs + '\'' +
                ", totalMergedPRs='" + totalMergedPRs +
                ", totalOpenPRs='" + totalOpenPRs + '\'' +
                ", totalDeclinedPRs='" + totalDeclinedPRs +
                '}';
    }

    // Schema of TopAuthor
    public static class TopAuthor {
        private String name;
        private int total;

        public TopAuthor(String name, int total) {
            this.name = name;
            this.total = total;
        }

        public String getName() {
            return name;
        }

        public int getTotal() {
            return total;
        }
    }
}
