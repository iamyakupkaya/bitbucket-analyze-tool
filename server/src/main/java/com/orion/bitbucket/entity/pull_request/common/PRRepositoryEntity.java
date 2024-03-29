package com.orion.bitbucket.entity.pull_request.common;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

//This class is used in values/fromRef/repository and values/toRef/repository
public class PRRepositoryEntity {
    @Field("id")
    private int id =-1;
    private String slug="Unknown";
    private String name="Unknown";
    private String description="Unknown";
    private String hierarchyId="Unknown";
    private String scmId="Unknown";
    private String state="Unknown";
    private String statusMessage="Unknown";
    private boolean forkable=false;
    @Field("public")
    private boolean Public=false;
    private PRRepositoryProjectEntity project;
    private PRRepositoryLinksEntity links;

    public PRRepositoryEntity() {
    }

    public PRRepositoryEntity(int id, String slug, String name, String description, String hierarchyId, String scmId, String state, String statusMessage, boolean forkable, boolean aPublic, PRRepositoryProjectEntity project, PRRepositoryLinksEntity links) {
        this.id = id;
        this.slug = slug;
        this.name = name;
        this.description = description;
        this.hierarchyId = hierarchyId;
        this.scmId = scmId;
        this.state = state;
        this.statusMessage = statusMessage;
        this.forkable = forkable;
        Public = aPublic;
        this.project = project;
        this.links = links;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHierarchyId() {
        return hierarchyId;
    }

    public void setHierarchyId(String hierarchyId) {
        this.hierarchyId = hierarchyId;
    }

    public String getScmId() {
        return scmId;
    }

    public void setScmId(String scmId) {
        this.scmId = scmId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public boolean isForkable() {
        return forkable;
    }

    public void setForkable(boolean forkable) {
        this.forkable = forkable;
    }

    public boolean isPublic() {
        return Public;
    }

    public void setPublic(boolean aPublic) {
        Public = aPublic;
    }

    public PRRepositoryProjectEntity getProject() {
        return project;
    }

    public void setProject(PRRepositoryProjectEntity project) {
        this.project = project;
    }

    public PRRepositoryLinksEntity getLinks() {
        return links;
    }

    public void setLinks(PRRepositoryLinksEntity links) {
        this.links = links;
    }
}
/*
             "slug": "mcp_core_root",
                    "id": 1656,
                    "name": "mcp_core_root",
                    "description": "Contains AS Core",
                    "hierarchyId": "e2150936fcc6fa501892",
                    "scmId": "git",
                    "state": "AVAILABLE",
                    "statusMessage": "Available",
                    "forkable": true,
 */
