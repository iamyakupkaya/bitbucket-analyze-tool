package com.orion.bitbucket.entity.pull_request;

import com.orion.bitbucket.entity.pull_request.common.PRRepositoryEntity;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

public class PRFromRefEntity {
    @Field("id")
    private String id;
    private String displayId;
    private String latestCommit;
    private String type;
    private PRRepositoryEntity repository;

    public PRFromRefEntity() {
    }

    public PRFromRefEntity(String id, String displayId, String latestCommit, String type, PRRepositoryEntity repository) {
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

    public PRRepositoryEntity getRepository() {
        return repository;
    }

    public void setRepository(PRRepositoryEntity repository) {
        this.repository = repository;
    }
}

