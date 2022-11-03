package com.orion.bitbucket.entities.PRSEntities;

import com.orion.bitbucket.entities.projectEntities.LinksEntity;
import com.orion.bitbucket.entities.common.CommonValuesEntity;


public class ProjectEntity extends CommonValuesEntity {
    private LinksEntity links;
    public ProjectEntity() {
    }

    public ProjectEntity(String key, String id, String name, boolean aPublic, String type, LinksEntity links) {
        super(key, id, name, aPublic, type);
        this.links = links;
    }

    public LinksEntity getLinks() {
        return links;
    }

    public void setLinks(LinksEntity links) {
        this.links = links;
    }
}
