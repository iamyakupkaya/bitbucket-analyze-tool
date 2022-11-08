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

    // BASE URL FOR A SPECIFIC PROJECT
    public static final String ASRV_PROJECT = "ASRV/repos/";

    // REPOS FOR ASRV PROJECT
    public static final String ASRV_MCP_CORE_ROOT_REPO="mcp_core_root";

    public static final String ASRV_AS_RAF_CORE_REPO="as_raf_core";

    // END POINT URL
    public static final String ASRV_MCP_CORE_ROOT_URL = BASE_URL + ASRV_PROJECT + ASRV_MCP_CORE_ROOT_REPO + "/pull-requests?state=ALL&limit=100&start=";

    public static final String ASRV_AS_RAF_CORE__URL = BASE_URL + ASRV_PROJECT + ASRV_AS_RAF_CORE_REPO + "/pull-requests?state=ALL&limit=100&start=";


    public static final class Bearer {

        private Bearer() {
        }

        public static final String TOKEN="MTQxMzk1MzI2ODU2Op++eZ5VAzEajkbttdvgexCGZqox"; //"please paste your TOKEN to here.!";
    }

}




