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

    private static final String CREATE_PULL_REQUEST_TABLE = "CREATE TABLE IF NOT EXISTS pullRequest (ID INT PRIMARY KEY NOT NULL, TITLE TEXT NOT NULL, STATE TEXT NOT NULL, CLOSED BOOLEAN, DESCRIPTION TEXT NOT NULL,UPDATE_DATE TEXT NOT NULL ,CREATED_DATE TEXT NOT NULL, CLOSED_DATE TEXT NOT NULL, EMAIL_ADDRESS TEXT NOT NULL, DISPLAY_NAME TEXT NOT NULL, SLUG TEXT NOT NULL)";
    private static final String CREATE_AUTHOR_TABLE = "CREATE TABLE IF NOT EXISTS author (ID INT PRIMARY KEY NOT NULL, NAME TEXT NOT NULL, TOTAL_PRS INT NOT NULL, TOTAL_MERGED_PRS INT NOT NULL, TOTAL_OPEN_PRS INT NOT NULL, TOTAL_DECLINED_PRS INT NOT NULL)";
    private static final String CREATE_REVIEW_TABLE = "CREATE TABLE IF NOT EXISTS review (ID INT PRIMARY KEY NOT NULL, DISPLAY_NAME TEXT NOT NULL, EMAIL_ADDRESS TEXT NOT NULL, APPROVED BOOLEAN NOT NULL, STATUS TEXT NOT NULL)";
    private static final String CREATE_REVIEWER_TABLE = "CREATE TABLE IF NOT EXISTS reviewer (ID INT PRIMARY KEY NOT NULL, NAME TEXT NOT NULL, TOTAL_REVIEW INT NOT NULL, TOTAL_APPROVE INT NOT NULL, TOTAL_UNAPPROVE INT NOT NULL)";   
    // ADD CREATE*TABLE VARIABLE INTO LIST
    public static ArrayList<String> getCreateTableQueries() {
        ArrayList<String> list = new ArrayList<>();
        list.add(CREATE_PULL_REQUEST_TABLE);
        list.add(CREATE_AUTHOR_TABLE);
        list.add(CREATE_REVIEW_TABLE);
        list.add(CREATE_REVIEWER_TABLE);
        return list;
    }
}


