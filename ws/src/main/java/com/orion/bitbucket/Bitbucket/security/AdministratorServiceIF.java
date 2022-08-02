package com.orion.bitbucket.Bitbucket.security;

import java.sql.SQLException;

public interface AdministratorServiceIF {
    public void setAdmin() throws SQLException;
    public boolean checkAdmin() throws SQLException;
}
