package com.orion.bitbucket.entities.PREntities.common;

import com.orion.bitbucket.entities.projectEntities.ProjectLinksEntity;
import com.orion.bitbucket.entities.common.CommonValuesEntity;


//this class is used in values/fromRef/repository/project and values/toRef/repository/project
public class PRRepositoryProjectEntity extends CommonValuesEntity {
    private ProjectLinksEntity links;
    public PRRepositoryProjectEntity() {
    }

    public PRRepositoryProjectEntity(String key, String id, String name, boolean aPublic, String type, ProjectLinksEntity links) {
        super(key, id, name, aPublic, type);
        this.links = links;
    }

    public ProjectLinksEntity getLinks() {
        return links;
    }

    public void setLinks(ProjectLinksEntity links) {
        this.links = links;
    }
}
