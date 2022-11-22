package com.orion.bitbucket.entity.pull_request;

public class PRPropertiesEntity {
    private PRPropertiesMergeResultEntity mergeResult;
    private int resolvedTaskCount=-1;
    private int commentCount=-1;
    private int openTaskCount=-1;

    public PRPropertiesEntity() {
    }

    public PRPropertiesEntity(PRPropertiesMergeResultEntity mergeResult, int resolvedTaskCount, int commentCount, int openTaskCount) {
        this.mergeResult = mergeResult;
        this.resolvedTaskCount = resolvedTaskCount;
        this.commentCount = commentCount;
        this.openTaskCount = openTaskCount;
    }

    public PRPropertiesMergeResultEntity getMergeResult() {
        return mergeResult;
    }

    public void setMergeResult(PRPropertiesMergeResultEntity mergeResult) {
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
