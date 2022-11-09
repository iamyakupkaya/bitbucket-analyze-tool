package com.orion.bitbucket.helper;

public final class EndPointsHelper {


    /*
     * Bitbucket REST API end points and tokens. In order to get the GIT statistics
     * from Bitbucket we used "bitbucket-read-only-user" bearer token. In case of a
     * token change please modify TOKEN attribute.
     */
    private EndPointsHelper(){}

    // BASE URL FOR ALL PROJECTS
    public static final String BASE_URL ="http://bitbucket.rbbn.com/rest/api/1.0/projects/";
    public static final String REPOS = "/repos/";


    // END POINTS URL FOR ASRV PROJECT
    public static final String ASRV_MCP_CORE_ROOT_URL = BASE_URL + DatabaseHelper.ASRV_PROJECT_NAME +  REPOS + DatabaseHelper.ASRV_REPO_MCP_CORE_ROOT +"/pull-requests?state=ALL&limit=100&start=";
    public static final String ASRV_AS_RAF_CORE_URL = BASE_URL + DatabaseHelper.ASRV_PROJECT_NAME +  REPOS + DatabaseHelper.ASRV_REPO_AS_RAF_CORE + "/pull-requests?state=ALL&limit=100&start=";

    // END POINTS URL FOR IAC PROJECT
    public static final String IAC_IAC_URL= BASE_URL + DatabaseHelper.IAC_PROJECT_NAME + REPOS + DatabaseHelper.IAC_REPO_IAC + "/pull-requests?state=ALL&limit=100&start=";

    public static final class Bearer {

        private Bearer() {
        }

        public static final String TOKEN="MTQxMzk1MzI2ODU2Op++eZ5VAzEajkbttdvgexCGZqox"; //"please paste your TOKEN to here.!";
    }

}




