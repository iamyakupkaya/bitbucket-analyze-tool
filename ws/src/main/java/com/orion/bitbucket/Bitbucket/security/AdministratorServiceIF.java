package com.orion.bitbucket.Bitbucket.security;

import java.sql.SQLException;

public interface AdministratorServiceIF {
    public void setAdmin() throws SQLException;
    public void setAdmin(String username, String password, String role) throws SQLException;
    public void deleteAuthority(String username) throws SQLException;
    public boolean checkAdmin() throws SQLException;
    public boolean checkAdmin(String username) throws SQLException;
}
