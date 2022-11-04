package com.orion.bitbucket.entities.PRSEntities;

import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

public class PRValuesEntity {

    @Id
    private String id;
    private int version;
    private String title;
    private String description;
    private String state;
    private String open;
    private String closed;
    private long createdDate;
    private long updatedDate;
    private boolean locked;

    private ArrayList<PRSParticipantsEntity> participants;

    private PRSFromRefEntity fromRef;


    private PRSToRefEntity toRef;


    private PRSAuthorEntity author;

    private List<PRSReviewersEntity> reviewers;


    private PRSPropertiesEntity properties;


    private PRSLinksEntity links;

    public PRValuesEntity() {
    }

    public PRValuesEntity(String id, int version, String title, String description, String state, String open, String closed, long createdDate, long updatedDate, boolean locked, ArrayList<PRSParticipantsEntity> participants, PRSFromRefEntity fromRef, PRSToRefEntity toRef, PRSAuthorEntity author, List<PRSReviewersEntity> reviewers, PRSPropertiesEntity properties, PRSLinksEntity links) {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public ArrayList<PRSParticipantsEntity> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<PRSParticipantsEntity> participants) {
        this.participants = participants;
    }

    public PRSFromRefEntity getFromRef() {
        return fromRef;
    }

    public void setFromRef(PRSFromRefEntity fromRef) {
        this.fromRef = fromRef;
    }

    public PRSToRefEntity getToRef() {
        return toRef;
    }

    public void setToRef(PRSToRefEntity toRef) {
        this.toRef = toRef;
    }

    public PRSAuthorEntity getAuthor() {
        return author;
    }

    public void setAuthor(PRSAuthorEntity author) {
        this.author = author;
    }

    public List<PRSReviewersEntity> getReviewers() {
        return reviewers;
    }

    public void setReviewers(List<PRSReviewersEntity> reviewers) {
        this.reviewers = reviewers;
    }

    public PRSPropertiesEntity getProperties() {
        return properties;
    }

    public void setProperties(PRSPropertiesEntity properties) {
        this.properties = properties;
    }

    public PRSLinksEntity getLinks() {
        return links;
    }

    public void setLinks(PRSLinksEntity links) {
        this.links = links;
    }
}
