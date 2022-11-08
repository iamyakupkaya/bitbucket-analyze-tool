package com.orion.bitbucket.entity.pull_request.common;

import com.orion.bitbucket.entity.common.CommonLinksEntity;
import com.orion.bitbucket.entity.project.ProjectSelfEntity;

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