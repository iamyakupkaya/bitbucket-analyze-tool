package com.orion.bitbucket.entities.PREntities.common;

import com.orion.bitbucket.entities.common.CommonLinksEntity;
import com.orion.bitbucket.entities.projectEntities.ProjectSelfEntity;

import java.util.List;

//this class is used in values/fromRef/repository/links and values/toRef/repository/links as a common class
public class PRRepositoryLinksEntity extends CommonLinksEntity {
    private List<PRRepositoryLinksCloneEntity> clone;

    public PRRepositoryLinksEntity(){};

    public PRRepositoryLinksEntity(List<ProjectSelfEntity> self, List<PRRepositoryLinksCloneEntity> clone) {
        super(self);
        this.clone = clone;
    }

    public List<PRRepositoryLinksCloneEntity> getClone() {
        return clone;
    }

    public void setClone(List<PRRepositoryLinksCloneEntity> clone) {
        this.clone = clone;
    }
}