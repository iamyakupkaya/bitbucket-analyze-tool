package com.orion.bitbucket.entities.PRSEntities;

public class PRSPropertiesEntity {
    private PRSPropertiesMergeResultEntity mergeResult;
    private int resolvedTaskCount;
    private int commentCount;
    private int openTaskCount;

    public PRSPropertiesEntity() {
    }

    public PRSPropertiesEntity(PRSPropertiesMergeResultEntity mergeResult, int resolvedTaskCount, int commentCount, int openTaskCount) {
        this.mergeResult = mergeResult;
        this.resolvedTaskCount = resolvedTaskCount;
        this.commentCount = commentCount;
        this.openTaskCount = openTaskCount;
    }

    public PRSPropertiesMergeResultEntity getMergeResult() {
        return mergeResult;
    }

    public void setMergeResult(PRSPropertiesMergeResultEntity mergeResult) {
        this.mergeResult = mergeResult;
    }

    public int getResolvedTaskCount() {
        return resolvedTaskCount;
    }

    public void setResolvedTaskCount(int resolvedTaskCount) {
        this.resolvedTaskCount = resolvedTaskCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getOpenTaskCount() {
        return openTaskCount;
    }

    public void setOpenTaskCount(int openTaskCount) {
        this.openTaskCount = openTaskCount;
    }
}
