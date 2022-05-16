package com.orion.bitbucket.Bitbucket.dbc;

import java.util.ArrayList;

public class DBConstants {

    // DO NOT CHANGE!
    private static final String DB_CONN_URL = "jdbc:postgresql://localhost:5432/bitbucket";
    private static final String DB_CONN_USERNAME = "postgres";
    private static final String DB_CONN_PASSWORD = "123";

    public static String getConnectionURL() {
        return DB_CONN_URL;
    }

    public static String getDBUsername() {
        return DB_CONN_USERNAME;
    }

    public static String getDBPassword() {
        return DB_CONN_PASSWORD;
    }

    // ADD A NEW STRING VARIABLE THAT CREATES A NEW TABLE
    private static final String CREATE_AUTHOR_TABLE = "CREATE TABLE IF NOT EXISTS YUSUF (ID INT PRIMARY KEY NOT NULL, NAME TEXT NOT NULL, AGE INT NOT NULL, ADDRESS CHAR(50), SALARY REAL)";
    private static final String CREATE_PULL_REQUEST_TABLE = "CREATE TABLE IF NOT EXISTS ridvan (ID INT PRIMARY KEY NOT NULL, NAME TEXT NOT NULL, AGE INT NOT NULL, ADDRESS CHAR(50), SALARY REAL)";

    // ADD CREATE*TABLE VARIABLE INTO LIST
    public static ArrayList<String> getCreateTableQueries() {
        ArrayList<String> list = new ArrayList<>();
        list.add(CREATE_AUTHOR_TABLE);
        list.add(CREATE_PULL_REQUEST_TABLE);
        return list;
    }
}
