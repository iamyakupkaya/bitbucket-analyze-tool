package com.orion.bitbucket.entities.PRSEntities;

public class AuthorEntity {
    private UserEntity user;
    private String role;
    private boolean approved;
    private String status;

    public AuthorEntity() {
    }

    public AuthorEntity(UserEntity user, String role, boolean approved, String status) {
        this.user = user;
        this.role = role;
        this.approved = approved;
        this.status = status;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
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
