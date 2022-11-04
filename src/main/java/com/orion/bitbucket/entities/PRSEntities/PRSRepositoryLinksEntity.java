package com.orion.bitbucket.entities.PRSEntities;

import com.orion.bitbucket.entities.projectEntities.ProjectSelfEntity;
import com.orion.bitbucket.entities.common.CommonLinksEntity;

import java.util.List;

public class PRSRepositoryLinksEntity extends CommonLinksEntity {
    private List<PRSRepositoryLinksCloneEntity> clone;

    public PRSRepositoryLinksEntity(){};

    public PRSRepositoryLinksEntity(List<ProjectSelfEntity> self, List<PRSRepositoryLinksCloneEntity> clone) {
        super(self);
        this.clone = clone;
    }

    public List<PRSRepositoryLinksCloneEntity> getClone() {
        return clone;
    }

    public void setClone(List<PRSRepositoryLinksCloneEntity> clone) {
        this.clone = clone;
    }
}
