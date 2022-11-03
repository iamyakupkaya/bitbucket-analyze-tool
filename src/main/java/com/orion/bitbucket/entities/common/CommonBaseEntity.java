package com.orion.bitbucket.entities.common;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class CommonBaseEntity {
    private int size;
    private int limit;
    private boolean isLastPage;
    private int start;
    private int nextPageStart;

    public CommonBaseEntity() {
    }

    public CommonBaseEntity(int size, int limit, boolean isLastPage, int start, int nextPageStart) {
        this.size = size;
        this.limit = limit;
        this.isLastPage = isLastPage;
        this.start = start;
        this.nextPageStart = nextPageStart;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public boolean getIsLastPage() {
        return isLastPage;
    }

    public void setIsLastPage(boolean isLastPage) {
        this.isLastPage = isLastPage;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getNextPageStart() {
        return nextPageStart;
    }

    public void setNextPageStart(int nextPageStart) {
        this.nextPageStart = nextPageStart;
    }

}
