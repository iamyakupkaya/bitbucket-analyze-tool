package com.orion.bitbucket.entities.PRSEntities;

import com.orion.bitbucket.entities.projectEntities.ProjectLinksEntity;
import com.orion.bitbucket.entities.common.CommonValuesEntity;


public class PRSRepositoryProjectEntity extends CommonValuesEntity {
    private ProjectLinksEntity links;
    public PRSRepositoryProjectEntity() {
    }

    public PRSRepositoryProjectEntity(String key, String id, String name, boolean aPublic, String type, ProjectLinksEntity links) {
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
