package com.orion.bitbucket.entity.project;

import com.orion.bitbucket.entity.common.CommonValuesEntity;

public class ProjectValuesEntity extends CommonValuesEntity {
    private String description ="Unknown";
    private ProjectLinksEntity links;

  public ProjectValuesEntity(){};

    public ProjectValuesEntity(String key, String id, String name, boolean aPublic, String type, String description, ProjectLinksEntity links) {
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

    public ProjectLinksEntity getLinks() {
        return links;
    }

    public void setLinks(ProjectLinksEntity links) {
        this.links = links;
    }
}
