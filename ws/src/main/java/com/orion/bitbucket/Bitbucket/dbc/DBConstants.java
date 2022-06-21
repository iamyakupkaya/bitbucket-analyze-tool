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

    private static final String CREATE_PULL_REQUEST_TABLE = "CREATE TABLE IF NOT EXISTS pullRequest (ID INT PRIMARY KEY NOT NULL, TITLE TEXT NOT NULL, STATE TEXT NOT NULL, CLOSED BOOLEAN, DESCRIPTION TEXT NOT NULL,UPDATE_DATE TEXT NOT NULL ,CREATED_DATE DATE NOT NULL, CLOSED_DATE DATE NOT NULL, EMAIL_ADDRESS TEXT NOT NULL, DISPLAY_NAME TEXT NOT NULL, SLUG TEXT NOT NULL)";
    private static final String CREATE_AUTHOR_TABLE = "CREATE TABLE IF NOT EXISTS author (ID INT PRIMARY KEY NOT NULL, NAME TEXT NOT NULL, TOTAL_PRS INT NOT NULL, TOTAL_MERGED_PRS INT NOT NULL, TOTAL_OPEN_PRS INT NOT NULL, TOTAL_DECLINED_PRS INT NOT NULL)";
    private static final String CREATE_REVIEW_TABLE = "CREATE TABLE IF NOT EXISTS public.review (id integer NOT NULL GENERATED BY DEFAULT AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ), reviewer_id integer NOT NULL, display_name text COLLATE pg_catalog.\"default\" NOT NULL, email_address text COLLATE pg_catalog.\"default\" NOT NULL, approved boolean NOT NULL, status text COLLATE pg_catalog.\"default\" NOT NULL, CONSTRAINT review_pkey PRIMARY KEY (id))\n";
    private static final String CREATE_REVIEWER_TABLE = "CREATE TABLE IF NOT EXISTS reviewer (ID INT PRIMARY KEY NOT NULL, NAME TEXT NOT NULL, TOTAL_REVIEW INT NOT NULL, TOTAL_APPROVE INT NOT NULL, TOTAL_UNAPPROVE INT NOT NULL)";   
    private static final String CREATE_PULL_REQUEST_REVIEW_RELATION = "CREATE TABLE IF NOT EXISTS PullRequestReviewRelation (ID INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY, PULL_REQUEST_ID INT NOT NULL, REVIEW_ID INT NOT NULL)";
    // ADD CREATE*TABLE VARIABLE INTO LIST
    public static ArrayList<String> getCreateTableQueries() {
        ArrayList<String> list = new ArrayList<>();
        list.add(CREATE_PULL_REQUEST_TABLE);
        list.add(CREATE_AUTHOR_TABLE);
        list.add(CREATE_REVIEW_TABLE);
        list.add(CREATE_REVIEWER_TABLE);
        list.add(CREATE_PULL_REQUEST_REVIEW_RELATION);
        return list;
    }

    public static final class Author {
        private Author() {
        }

        public static final String AUTHOR_ID = "id";
        public static final String AUTHOR_NAME = "name";
        public static final String AUTHOR_TOTAL_PRS = "total_prs";
        public static final String AUTHOR_TOTAL_MERGED_PRS = "total_merged_prs";
        public static final String AUTHOR_TOTAL_OPEN_PRS = "total_open_prs";
        public static final String AUTHOR_TOTAL_DECLINED_PRS = "total_declined_prs";
        public static final String AUTHOR_UPDATE_FILTER = "count";

    }

    public static final class PullRequest {
        private PullRequest() {
        }

        public static final String PULL_REQUEST_ID = "id";
        public static final String PULL_REQUEST_TITLE = "title";
        public static final String PULL_REQUEST_STATE = "state";
        public static final String PULL_REQUEST_CLOSED = "closed";
        public static final String PULL_REQUEST_DESCRIPTION = "description";
        public static final String PULL_REQUEST_UPDATE_DATE = "update_date";
        public static final String PULL_REQUEST_CREATED_DATE = "created_date";
        public static final String PULL_REQUEST_CLOSED_DATE = "closed_date";
        public static final String PULL_REQUEST_AUTHOR_EMAIL_ADDRESS = "email_address";
        public static final String PULL_REQUEST_AUTHOR_DISPLAY_NAME = "display_name";
        public static final String PULL_REQUEST_AUTHOR_SLUG = "slug";
        public static final String PULL_REQUEST_JIRA_ID = "AAK-"; 
        public static final String PULL_REQUEST_NO_JIRA_ID = "-";

    }

    public static final class PullRequestState {
        private PullRequestState() {
        }

        public static final String MERGED = "MERGED";
        public static final String OPEN = "OPEN";
        public static final String DECLINED = "DECLINED";

    }

    public static final class PullRequestReviewRelation {
        private PullRequestReviewRelation() {
        }

        public static final String REVIEW_RELATION_PULL_REQUEST_ID = "pull_request_id";
        public static final String REVIEW_RELATION_REVIEW_ID = "review_id";

    }

    public static final class Review {
        private Review() {
        }

        public static final String REVIEW_ID = "id";
        public static final String REVIEW_DISPLAY_NAME = "display_name";
        public static final String REVIEW_EMAIL_ADDRESS = "email_address";
        public static final String REVIEW_APPROVED = "approved";
        public static final String REVIEW_STATUS = "status";
        public static final String REVIEW_STATUS_APPROVED = "APPROVED";
        public static final String REVIEW_STATUS_UNAPPROVED = "UNAPPROVED";

    }

    public static final class Reviewer {
        private Reviewer() {
        }

        public static final String REVIEWER_ID = "id";
        public static final String REVIEWER_NAME = "name";
        public static final String REVIEWER_TOTAL_REVIEW = "total_review";
        public static final String REVIEWER_TOTAL_APPROVE = "total_approve";
        public static final String REVIEWER_TOTAL_UNAPPROVE = "total_unapprove";

    }





}


