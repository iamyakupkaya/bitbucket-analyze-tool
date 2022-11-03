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

    private ArrayList<ParticipantsEntity> participants;

    private FromRefEntity fromRef;


    private PRSToRefEntity toRef;


    private AuthorEntity author;

    private List<ReviewersEntity> reviewers;


    private PropertiesEntity properties;


    private LinksEntity links;

    public PRValuesEntity() {
    }

    public PRValuesEntity(String id, int version, String title, String description, String state, String open, String closed, long createdDate, long updatedDate, boolean locked, ArrayList<ParticipantsEntity> participants, FromRefEntity fromRef, PRSToRefEntity toRef, AuthorEntity author, List<ReviewersEntity> reviewers, PropertiesEntity properties, LinksEntity links) {
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

    public ArrayList<ParticipantsEntity> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<ParticipantsEntity> participants) {
        this.participants = participants;
    }

    public FromRefEntity getFromRef() {
        return fromRef;
    }

    public void setFromRef(FromRefEntity fromRef) {
        this.fromRef = fromRef;
    }

    public PRSToRefEntity getToRef() {
        return toRef;
    }

    public void setToRef(PRSToRefEntity toRef) {
        this.toRef = toRef;
    }

    public AuthorEntity getAuthor() {
        return author;
    }

    public void setAuthor(AuthorEntity author) {
        this.author = author;
    }

    public List<ReviewersEntity> getReviewers() {
        return reviewers;
    }

    public void setReviewers(List<ReviewersEntity> reviewers) {
        this.reviewers = reviewers;
    }

    public PropertiesEntity getProperties() {
        return properties;
    }

    public void setProperties(PropertiesEntity properties) {
        this.properties = properties;
    }

    public LinksEntity getLinks() {
        return links;
    }

    public void setLinks(LinksEntity links) {
        this.links = links;
    }
}
