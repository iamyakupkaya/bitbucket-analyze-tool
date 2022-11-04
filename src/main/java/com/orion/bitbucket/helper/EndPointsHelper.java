package com.orion.bitbucket.helper;

public final class EndPointsHelper {


    /*
     * Bitbucket REST API end points and tokens. In order to get the GIT statistics
     * from Bitbucket we used "bitbucket-read-only-user" bearer token. In case of a
     * token change please modify TOKEN attribute.
     */
    private EndPointsHelper(){}

    public static final String BASE_URL ="http://bitbucket.rbbn.com/rest/api/1.0/projects/";

    public static final String PROJECT = "ASRV/repos/mcp_core_root/";
    public static final String ALL_PRS = BASE_URL + PROJECT + "pull-requests?state=ALL&limit=100&start=";
    public static final class Bearer {

        private Bearer() {
        }

        public static final String TOKEN="MTQxMzk1MzI2ODU2Op++eZ5VAzEajkbttdvgexCGZqox"; //"please paste your TOKEN to here.!";
    }

}




