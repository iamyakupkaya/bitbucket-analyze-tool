package com.orion.bitbucket.Bitbucket.model;

public class UserDO {
    private int id;
    private String name;
    private String manager;
    private String teamCode;
    private String emailAddress;
    private String slug;

    public UserDO(int id, String name, String manager, String teamCode, String emailAddress, String slug) {
        this.id = id;
        this.name = name;
        this.manager = manager;
        this.teamCode = teamCode;
        this.emailAddress = emailAddress;
        this.slug = slug;
    }
    public UserDO(){

    }
    public int getId() {return id;
    }
    public String getName() {
        return name;
    }

    public String getManagerName() {
        return manager;
    }

    public String getTeamCode() {
        return teamCode;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getSlug() {
        return slug;
    }

    @Override
    public String toString() {
        return "UserDO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", manager='" + manager + '\'' +
                ", teamCode='" + teamCode + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", slug='" + slug + '\'' +
                '}';
    }
}