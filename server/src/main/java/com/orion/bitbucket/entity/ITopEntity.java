package com.orion.bitbucket.entity;

public interface ITopEntity {

    int getSize();
    void setSize(int size);

    int getLimit();

    void setLimit(int limit);

    boolean getIsLastPage();

    void setIsLastPage(boolean isLastPage);

    int getStart();

    void setStart(int start);

    int getNextPageStart();

    void setNextPageStart(int nextPageStart);

}
