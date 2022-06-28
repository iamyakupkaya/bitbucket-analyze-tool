package com.orion.bitbucket.Bitbucket.model;

public class UserDO {
    private int id;
    private String username;
    private String first_name;
    private String last_name;
    private String password;
    private String email;
    private String team_Code;
    private String role;

    public UserDO(int id, String username, String first_name, String last_name,
                  String password, String email, String team_Code, String role) {
        this.id = id;
        this.username = username;
        this.first_name = first_name;
        this.last_name = last_name;
        this.password = password;
        this.email = email;
        this.team_Code = team_Code;
        this.role = role;
    }
    public UserDO(){}
    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getTeam_Code() {
        return team_Code;
    }

    public String getRole() {
        return role;
    }
    @Override
    public String toString() {
        return "UserDO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", team_Code='" + team_Code + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}