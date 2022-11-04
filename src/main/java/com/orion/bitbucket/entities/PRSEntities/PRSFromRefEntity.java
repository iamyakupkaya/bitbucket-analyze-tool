package com.orion.bitbucket.entities.PRSEntities;

import org.springframework.data.annotation.Id;

public class PRSFromRefEntity {
    @Id
    private String id;
    private String displayId;
    private String latestCommit;
    private String type;
    private PRSRepositoryEntity repository;

    public PRSFromRefEntity() {
    }

    public PRSFromRefEntity(String id, String displayId, String latestCommit, String type, PRSRepositoryEntity repository) {
        this.id = id;
        this.displayId = displayId;
        this.latestCommit = latestCommit;
        this.type = type;
        this.repository = repository;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayId() {
        return displayId;
    }

    public void setDisplayId(String displayId) {
        this.displayId = displayId;
    }

    public String getLatestCommit() {
        return latestCommit;
    }

    public void setLatestCommit(String latestCommit) {
        this.latestCommit = latestCommit;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public PRSRepositoryEntity getRepository() {
        return repository;
    }

    public void setRepository(PRSRepositoryEntity repository) {
        this.repository = repository;
    }
}

