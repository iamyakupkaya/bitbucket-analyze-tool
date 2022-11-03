package com.orion.bitbucket.entities.projectEntities;

import com.orion.bitbucket.entities.common.CommonValuesEntity;

public class ValuesEntity extends CommonValuesEntity {
    private String description;
    private LinksEntity links;

  public ValuesEntity(){};

    public ValuesEntity(String key, String id, String name, boolean aPublic, String type, String description, LinksEntity links) {
        super(key, id, name, aPublic, type);
        this.description = description;
        this.links=links;

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LinksEntity getLinks() {
        return links;
    }

    public void setLinks(LinksEntity links) {
        this.links = links;
    }
}
