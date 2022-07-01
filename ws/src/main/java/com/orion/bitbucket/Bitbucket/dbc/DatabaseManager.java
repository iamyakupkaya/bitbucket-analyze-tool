package com.orion.bitbucket.Bitbucket.dbc;
import java.sql.*;
import java.util.ArrayList;
import static com.orion.bitbucket.Bitbucket.dbc.DBConstants.getCreateDatabaseQuery;
import static com.orion.bitbucket.Bitbucket.dbc.DBConstants.getCreateTableQueries;

public class DatabaseManager {

    public static void run() {

        Connection connection2 = TransactionManager.getConnectionCreateDatabase();
        ArrayList<String> dbNamesList = new ArrayList<>();
        boolean availableDbName = false;
        try {
            String query = "SELECT datname FROM pg_database;";
            Statement statement = connection2.createStatement();
            ResultSet result1 = statement.executeQuery(query);
            while (result1.next()) {
                String aDBName = result1.getString(1);
                dbNamesList.add(aDBName);
            }
            connection2.close();
        }catch (Exception e){}
        String dbName = "bitbucket";
        if(dbNamesList.contains(dbName)){
            availableDbName = true;
        }

        if(availableDbName){
            System.out.println("database already created");
        }else{
            Connection connection1 = TransactionManager.getConnectionCreateDatabase();
            try{
                Statement statement1 = null;
                for (int i = 0; i<getCreateDatabaseQuery().size(); i++){
                    statement1 = connection1.createStatement();
                    String query = getCreateDatabaseQuery().get(i);
                    statement1.executeUpdate(query);
                    //connection1.commit();
                    statement1.close();
                    System.out.println("Database created successfully");
                }
                connection1.close();
            }catch (Exception e){
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
        }
        Connection connection = TransactionManager.getConnection();;
        try {
            Statement statement = null;
            for (int i=0; i<getCreateTableQueries().size(); i++) {
                statement = connection.createStatement();
                String query = getCreateTableQueries().get(i);
                statement.executeUpdate(query);
                connection.commit();
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
