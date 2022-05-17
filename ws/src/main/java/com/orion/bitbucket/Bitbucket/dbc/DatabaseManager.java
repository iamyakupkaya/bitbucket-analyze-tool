package com.orion.bitbucket.Bitbucket.dbc;
import java.sql.*;
import static com.orion.bitbucket.Bitbucket.dbc.DBConstants.getCreateTableQueries;

public class DatabaseManager {

    public static void run() {
        Connection connection = TransactionManager.getConnection();;
        try {
            Statement statement = null;
            for (int i=0; i<getCreateTableQueries().size(); i++) {
                statement = connection.createStatement();
                String query = getCreateTableQueries().get(i);
                statement.executeUpdate(query);
                statement.close();
                System.out.println("Table created successfully");
            }
            connection.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
}
