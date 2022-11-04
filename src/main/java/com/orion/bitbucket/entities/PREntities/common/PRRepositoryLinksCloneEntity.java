package com.orion.bitbucket.entities.PREntities.common;

public class PRRepositoryLinksCloneEntity {
    private String href;
    private String name;

    public PRRepositoryLinksCloneEntity() {
    }

    public PRRepositoryLinksCloneEntity(String href, String name) {
        this.href = href;
        this.name = name;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
