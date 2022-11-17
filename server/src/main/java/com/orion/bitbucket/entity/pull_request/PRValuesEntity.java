package com.orion.bitbucket.entity.pull_request;

import com.orion.bitbucket.entity.pull_request.common.PRLinksEntity;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

public class PRValuesEntity {
    @Field("id")
    private int id;
    private int version;
    private String title;
    private String description;
    private String state;
    private String open;
    private String closed;
    private long createdDate;
    private long updatedDate;
    private boolean locked;

    private ArrayList<PRParticipantsEntity> participants;

    private PRFromRefEntity fromRef;


    private PRToRefEntity toRef;


    private PRAuthorEntity author;

    private List<PRReviewersEntity> reviewers;


    private PRPropertiesEntity properties;


    private PRLinksEntity links;

    public PRValuesEntity() {
    }

    public PRValuesEntity(int id, int version, String title, String description, String state, String open, String closed, long createdDate, long updatedDate, boolean locked, ArrayList<PRParticipantsEntity> participants, PRFromRefEntity fromRef, PRToRefEntity toRef, PRAuthorEntity author, List<PRReviewersEntity> reviewers, PRPropertiesEntity properties, PRLinksEntity links) {
        this.id = id;
        this.version = version;
        this.title = title;
        this.description = description;
        this.state = state;
        this.open = open;
        this.closed = closed;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.locked = locked;
        this.participants = participants;
        this.fromRef = fromRef;
        this.toRef = toRef;
        this.author = author;
        this.reviewers = reviewers;
        this.properties = properties;
        this.links = links;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getClosed() {
        return closed;
    }

    public void setClosed(String closed) {
        this.closed = closed;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public long getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(long updatedDate) {
        this.updatedDate = updatedDate;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public ArrayList<PRParticipantsEntity> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<PRParticipantsEntity> participants) {
        this.participants = participants;
    }

    public PRFromRefEntity getFromRef() {
        return fromRef;
    }

    public void setFromRef(PRFromRefEntity fromRef) {
        this.fromRef = fromRef;
    }

    public PRToRefEntity getToRef() {
        return toRef;
    }

    public void setToRef(PRToRefEntity toRef) {
        this.toRef = toRef;
    }

    public PRAuthorEntity getAuthor() {
        return author;
    }

    public void setAuthor(PRAuthorEntity author) {
        this.author = author;
    }

    public List<PRReviewersEntity> getReviewers() {
        return reviewers;
    }

    public void setReviewers(List<PRReviewersEntity> reviewers) {
        this.reviewers = reviewers;
    }

    public PRPropertiesEntity getProperties() {
        return properties;
    }

    public void setProperties(PRPropertiesEntity properties) {
        this.properties = properties;
    }

    public PRLinksEntity getLinks() {
        return links;
    }

    public void setLinks(PRLinksEntity links) {
        this.links = links;
    }
}
