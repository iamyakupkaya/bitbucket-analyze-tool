package com.orion.bitbucket.entities.common;

import com.orion.bitbucket.entities.projectEntities.SelfEntity;

import javax.persistence.MappedSuperclass;
import java.util.List;

@MappedSuperclass
public abstract class CommonLinksEntity {
    private List<SelfEntity> self;

    public CommonLinksEntity() {
    }

    public CommonLinksEntity(List<SelfEntity> self) {
        this.self = self;
    }

    public List<SelfEntity> getSelf() {
        return self;
    }

    public void setSelf(List<SelfEntity> self) {
        this.self = self;
    }
}
