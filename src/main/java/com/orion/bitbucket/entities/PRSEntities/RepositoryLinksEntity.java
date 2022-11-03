package com.orion.bitbucket.entities.PRSEntities;

import com.orion.bitbucket.entities.projectEntities.SelfEntity;
import com.orion.bitbucket.entities.common.CommonLinksEntity;

import java.util.List;

public class RepositoryLinksEntity extends CommonLinksEntity {
    private List<RepositoryCloneEntity> clone;

    public RepositoryLinksEntity(){};

    public RepositoryLinksEntity(List<SelfEntity> self, List<RepositoryCloneEntity> clone) {
        super(self);
        this.clone = clone;
    }

    public List<RepositoryCloneEntity> getClone() {
        return clone;
    }

    public void setClone(List<RepositoryCloneEntity> clone) {
        this.clone = clone;
    }
}
