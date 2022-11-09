package com.orion.bitbucket.helper;

public class DatabaseHelper {

    public static final String PROJECTS="projects";

    // DB INFO
    public final static String DATABASE_NAME="bitbucket";
    public final static String DATABASE_URL="mongodb://localhost:27017";

    // PROJECTS NAME
    public static final String ASRV_PROJECT_NAME="ASRV";
    public static final String IAC_PROJECT_NAME="IAC";

    //ASRV REPOS NAME
    public static final String ASRV_REPO_MCP_CORE_ROOT="mcp_core_root";

    public static final String ASRV_REPO_AS_RAF_CORE="as_raf_core";

    // IAC REPOS NAME
    public static final String IAC_REPO_IAC="iac";

    // DB COLLECTION NAME FOR ASRV PROJECT
    public static final String COLLECTION_NAME_ASRV_MCP_CORE_ROOT=ASRV_PROJECT_NAME + "_" + ASRV_REPO_MCP_CORE_ROOT;

    public static final String COLLECTION_NAME_ASRV_AS_RAF_CORE=ASRV_PROJECT_NAME + "_" + ASRV_REPO_AS_RAF_CORE;

    // DB COLLECTION NAME FOR IAC PROJECT
    public static final String COLLECTION_NAME_IAC_IAC=IAC_PROJECT_NAME + "_" + IAC_REPO_IAC;
}
