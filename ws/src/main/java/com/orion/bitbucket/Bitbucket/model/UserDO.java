package com.orion.bitbucket.Bitbucket.model;

public class UserDO {
    private int id;
    private String username;
    private String firstname;
    private String lastname;
    private String password;
    private String email;
    private String teamCode;
    private String role;

    public UserDO(int id, String username, String firstname, String lastname,
                  String password, String email, String teamCode, String role) {
        this.id = id;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
        this.email = email;
        this.teamCode = teamCode;
        this.role = role;
    }
    public UserDO(){}
    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getTeamCode() {
        return teamCode;
    }

    public String getRole() {
        return role;
    }
    @Override
    public String toString() {
        return "UserDO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", team_Code='" + teamCode + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}