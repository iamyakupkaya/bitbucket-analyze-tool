package com.orion.bitbucket.entity.pull_request;

import com.orion.bitbucket.entity.pull_request.common.PRUserEntity;

public class PRAuthorEntity {
    private PRUserEntity user;
    private String role ="Unknown";
    private boolean approved = false;
    private String status="Unknown";

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
