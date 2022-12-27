package com.orion.bitbucket.entity.common;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class CommonBaseEntity {
    //default values
    private int size =-1;
    private int limit=-1;
    private boolean isLastPage=false;
    private int start=-1;
    private int nextPageStart=-1;
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
