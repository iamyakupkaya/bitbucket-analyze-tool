package com.orion.bitbucket.entities.PREntities;

import com.orion.bitbucket.entities.PREntities.common.PRUserEntity;

public class PRAuthorEntity {
    private PRUserEntity user;
    private String role;
    private boolean approved;
    private String status;

    public PRAuthorEntity() {
    }

    public PRAuthorEntity(PRUserEntity user, String role, boolean approved, String status) {
        this.user = user;
        this.role = role;
        this.approved = approved;
        this.status = status;
    }

    public PRUserEntity getUser() {
        return user;
    }

    public void setUser(PRUserEntity user) {
        this.user = user;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
