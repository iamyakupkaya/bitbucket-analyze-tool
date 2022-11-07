package com.orion.bitbucket.entity.common;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class CommonSelfEntity {
    private String href;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
