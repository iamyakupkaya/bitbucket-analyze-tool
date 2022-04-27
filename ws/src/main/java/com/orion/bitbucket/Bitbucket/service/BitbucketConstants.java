
package com.orion.bitbucket.Bitbucket.service;

public final class BitbucketConstants {

	private BitbucketConstants() {
	}

	/*
	 * Bitbucket REST API end points and tokens. In order to get the GIT statistics
	 * from Bitbucket we used "bitbucket-read-only-user" bearer token. In case of a
	 * token change please modify TOKEN attribute.
	 */

	public static final class EndPoints {
		private EndPoints() {
		}

		public static final String BASE_URL = "http://bitbucket.rbbn.com/rest/api/1.0/projects/";
		public static final String PROJECT = "ASRV/repos/mcp_core_root/";
		public static final String ALL_PRS = BASE_URL + PROJECT + "pull-requests?state=ALL&limit=100&start=";
		public static final String OPEN_PRS = BASE_URL + PROJECT + "pull-requests?state=OPEN&limit=100&start=";
		public static final String MERGED_PRS = BASE_URL + PROJECT + "pull-requests?state=MERGED&limit=100&start=";
		public static final String DECLINED_PRS = BASE_URL + PROJECT + "pull-requests?state=DECLINED&limit=100&start=";
		public static final String MERGED_PRS_WITH_TIME_INTERVAL = BASE_URL + "<...>";
		public static final String OPEN_PRS_WITG_TIME_INTERVAL = BASE_URL + "<...>";
		public static final String COMMITS_IN_PR = BASE_URL + "<...>";
		public static final String INSPECTORS_OF_PR = BASE_URL + PROJECT + "pull-requests/";

	}

	public static final class JsonPaths {
		private JsonPaths() {
		}

		public static final String PR_OWNERS = "$.values.*.author.user.displayName";
		public static final String ALL_REVIEWERS = "$.values.*.reviewers.*.user.displayName";
		public static final String PR_REVIEWERS = "$.reviewers.*.user.displayName";
		public static final String PR_APPROVAL = "$.values.*.reviewers.*.approved";
		public static final String IS_LAST_PAGE = "$.isLastPage";
		public static final String COUNT_OPEN_BY_USERNAME = "$..[?(@.state == 'OPEN')].author.user.displayName";
		public static final String COUNT_MERGED_BY_USERNAME = "$..[?(@.state == 'MERGED')].author.user.displayName";
		public static final String COUNT_DECLINED_BY_USERNAME = "$..[?(@.state == 'DECLINED')].author.user.displayName";

		//public static final String FILTER_AUTHOR_PRS_TITLE = "$..[?(@.author.user.displayName == 'Shih, David')].title";
		
		
		
	}

	public static final class Bearer {
		private Bearer() {
		}

		public static final String TOKEN = "NzA0NjI1MDc4NTgxOtaTzOS/slQKsSRTcPEbHYsezwx+";
	}
}
