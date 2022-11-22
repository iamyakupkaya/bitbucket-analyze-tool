package com.orion.bitbucket.entity.pull_request.common;

public class PRRepositoryLinksCloneEntity {
    private String href="Unknown";
    private String name="Unknown";

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
