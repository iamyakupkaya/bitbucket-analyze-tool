package com.orion.bitbucket.entity.common;

import com.orion.bitbucket.entity.project.ProjectSelfEntity;

import javax.persistence.MappedSuperclass;
import java.util.List;

@MappedSuperclass
public abstract class CommonLinksEntity {
    private List<ProjectSelfEntity> self;

    public CommonLinksEntity() {
    }

    public CommonLinksEntity(List<ProjectSelfEntity> self) {
        this.self = self;
    }

    public List<ProjectSelfEntity> getSelf() {
        return self;
    }

    public void setSelf(List<ProjectSelfEntity> self) {
        this.self = self;
    }
}
