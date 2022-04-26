
package com.orion.bitbucket.Bitbucket.service;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.orion.bitbucket.Bitbucket.model.PullRequestDO;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.jayway.jsonpath.JsonPath;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.orion.bitbucket.Bitbucket.model.PullRequest;
import com.orion.bitbucket.Bitbucket.model.UserPrDetails;

@Service
public class ServiceImpl implements ServiceIF {

	@Autowired
	private JsonResponse response;
	private JsonResponse responseMerge;
	private PullRequest  pullRequest;
	private UserPrDetails UserPrDetails;
	PullRequest pr = null;
	private Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private JsonElement jsonElement = null;
	
	private String jsonInString = null;
	private boolean isLastPage = false;
	
	private List<String> partList = null;
	private List<String> partList2 = null;
	private List<Integer> partListPrId = null;
	private List<Long> partListPrDate= null;
	
	private List<String> mergedList = new ArrayList<String>();
	private List<Boolean> isApproved = null;
	private List<String> list = new ArrayList<String>();
	private List<String> authorPRTitle = new ArrayList<String>();
	private List<String> authorPRState = new ArrayList<String>();
	private List<String> authorPRId = new ArrayList<String>();
	private List<String> authorPRDate= new ArrayList<String>();
	private int start = 0;
	private List<String> PRnums = null;
	private List<String> mergedNums=null;
	private String mergeControl = "MERGED";
	private String openControl = "OPEN";
	private String declinedControl = "DECLINED";
	
	
	/*TEST FİELD*/
	
	private List<String> authorGetForHash = new ArrayList<String>();
	private List<String> totalMergesPRsTest = new ArrayList<String>();
	private List<String> totalOpenPRsTest  = new ArrayList<String>();
	private List<String> totalDeclinedPRsTest  = new ArrayList<String>();
	private JsonElement jsonElementOpen = null;
	private JsonElement jsonElementMerged = null;
	private JsonElement jsonElementDeclined = null;
	private String jsonInStringOpen = null;
	private String jsonInStringMerged = null;
	private String jsonInStringDeclined = null;


	ArrayList<PullRequestDO> openPRList;
	ArrayList<PullRequestDO> mergedPRList;
	ArrayList<PullRequestDO> declinedPRList;

	public ServiceImpl() throws UnirestException {
		openPRList = getPullRequestData("OPEN");
		mergedPRList = getPullRequestData("MERGED");
		declinedPRList = getPullRequestData("DECLINED");
	}
	
	// TODO: will be deleted
	public String getPullRequests(String status, String jsonPath) throws JsonSyntaxException, UnirestException {
		list.clear();
		while (!isLastPage) {
			jsonElement = gson.fromJson(
					response.getResponse(status + start, BitbucketConstants.Bearer.TOKEN).getBody().toString(),
					JsonElement.class);
			jsonInString = gson.toJson(jsonElement);
			partList = JsonPath.read(jsonInString, jsonPath);

			for (int iter = 0; iter < partList.size(); iter++) {

				list.add(partList.get(iter));
				//System.out.println(partList.get(iter));
			}
			start += 100;
			isLastPage = JsonPath.read(gson.toJson(jsonElement), BitbucketConstants.JsonPaths.IS_LAST_PAGE);
		}
		start = 0;
		isLastPage = false;
		return count(list).toString();
	}

	public String getApprovals(String status, String reviewerPath, String approvePath)
			throws JsonSyntaxException, UnirestException {
		while (!isLastPage) {
			jsonElement = gson.fromJson(
					response.getResponse(status + start, BitbucketConstants.Bearer.TOKEN).getBody().toString(),
					JsonElement.class);
			jsonInString = gson.toJson(jsonElement);
			partList = JsonPath.read(jsonInString, reviewerPath);
			isApproved = JsonPath.read(jsonInString, approvePath);
			for (int iter = 0; iter < partList.size(); iter++) {
				if (isApproved.get(iter)) {
					list.add(partList.get(iter));
				}
				
			}
			start += 100;
			isLastPage = JsonPath.read(gson.toJson(jsonElement), BitbucketConstants.JsonPaths.IS_LAST_PAGE);
		}
		start = 0;
		isLastPage = false;
		return count(list).toString();
	}
	@SuppressWarnings({ "unchecked", "unlikely-arg-type" })
	public List<PullRequest> getAll(String status, String jsonPath) throws JsonSyntaxException, UnirestException {
		//oguzhan();
		List<PullRequest> array = new ArrayList<PullRequest>();
		list.clear();
		while (!isLastPage) {
			jsonElement = gson.fromJson(
					response.getResponse(status + start, BitbucketConstants.Bearer.TOKEN).getBody().toString(),
					JsonElement.class);
			jsonInString = gson.toJson(jsonElement);
			partList = JsonPath.read(jsonInString, jsonPath);

			List<String> open = JsonPath.read(jsonInString, BitbucketConstants.JsonPaths.COUNT_OPEN_BY_USERNAME);
			List<String> declined = JsonPath.read(jsonInString,BitbucketConstants.JsonPaths.COUNT_DECLINED_BY_USERNAME);
			List<String> merged = JsonPath.read(jsonInString, BitbucketConstants.JsonPaths.COUNT_MERGED_BY_USERNAME);
			for (int iter = 0; iter < partList.size(); iter++) {

				list.add(partList.get(iter));
			}
			for (int itertwo = 0; itertwo < merged.size(); itertwo++) {

				mergedList.add(merged.get(itertwo));
			}
			start += 100;
			isLastPage = JsonPath.read(gson.toJson(jsonElement), BitbucketConstants.JsonPaths.IS_LAST_PAGE);
		}
		start = 0;
		isLastPage = false;
		PRnums = splitInteger(count(list).toString());
		int x = 0;
		if((mergedList != null) && mergedList.size() > 0) {
			mergedNums = splitInteger(count(mergedList).toString());
		}
		
		
		for (int i = 0; i < PRnums.size(); i++) {
			
			PullRequest.getInstance().setAuthorName(count(list).keySet().toArray()[i].toString());
			PullRequest.getInstance().setTotal(Integer.valueOf(PRnums.get(i)));
			
			if(mergedList.toString().contains(count(list).keySet().toArray()[i].toString())) {
				
				PullRequest.getInstance().setMergedPRs(Integer.valueOf(mergedNums.get(x)));
				x++;
			}else {
				PullRequest.getInstance().setMergedPRs(0);
			}
			
			array.add(new PullRequest(PullRequest.getInstance().getAuthorName(),PullRequest.getInstance().getTotal(),PullRequest.getInstance().getMergedPRs(),0,0));
		}

		return array;
	}

	// takes a state input
	public ArrayList<PullRequestDO> getPullRequestData(String state) throws UnirestException {

		// move followings
		String BASE_URL = "http://bitbucket.rbbn.com/rest/api/1.0/projects/";
		String PROJECT = "ASRV/repos/mcp_core_root/";
		String END_POINT = BASE_URL + PROJECT + "pull-requests?state=" + state + "&limit=100&start=";

		ArrayList<PullRequestDO> list = new ArrayList<PullRequestDO>();

		int start = 0;
		boolean isLastPage = false;
		while (!isLastPage) {
			HttpResponse<JsonNode> httpResponse = response.getResponse(END_POINT + start, BitbucketConstants.Bearer.TOKEN);
			JSONObject body = httpResponse.getBody().getObject();
			Object values = body.get("values");
			JSONArray array = (JSONArray) values;
			JSONObject object = null;
			for (int i=0; i< array.length(); i++) {
				object = array.getJSONObject(i);
				list.add(commonDataParser(object)); // if you want change object with array.getjsonobject...
			}
			isLastPage = (boolean) body.get("isLastPage");
			start += 100;
		}
		return list;
	}

	public PullRequestDO commonDataParser(JSONObject object) {
		String title = (String) object.get("title");
		String state = (String) object.get("state");
		boolean closed = (boolean) object.get("closed");
		// String description = (String) object.get("description");
		long updatedDate = (long) object.get("updatedDate");
		long createdDate = (long) object.get("createdDate");
		long closedDate = 0;
		if (closed) {
			closedDate = (long) object.get("closedDate");
		}
		JSONObject author = object.getJSONObject("author");
		JSONObject user = author.getJSONObject("user");
		// String emailAddress = (String) user.get("emailAddress");
		String displayName = (String) user.get("displayName");
		String slug = (String) user.get("slug");
		// Add to somewhere. Get just important data from object.
		return new PullRequestDO(title, state, closed, updatedDate, createdDate, closedDate, displayName, slug);
	}

	public int getMergedPRCount() {
		return this.mergedPRList.size();
	}

	public void getOpenPRCount() {
		// return openPrList.size;
	}

	public void getDeclinedPRCount() {
		// return declinedPrList.size;
	}

	public ArrayList<PullRequestDO> getMergedPRList() {
		return this.mergedPRList;
	}

	public void getOpenPRList() {
		// return openPrList
	}

	public void getDeclinedPRList() {
		// return declinedPrList
	}

	public ArrayList<PullRequestDO> getMergedPRListByUsername(String username) {
		ArrayList<PullRequestDO> list = new ArrayList<PullRequestDO>();
		for (int i=0; i<this.mergedPRList.size(); i++) {
			if (mergedPRList.get(i).getSlug().equals(username)) {
				list.add(mergedPRList.get(i));
			}
		}
		return list;
	}

	public void getOpenPRListByUsername(String username) {
		// for openList
		// if username = input olan username
		// temp listeye ekle
		// return temp liste
	}

	public void getDeclinedPRListByUsername(String username) {
		// for openList
		// if username = input olan username
		// temp listeye ekle
		// return temp liste
	}

	public int getMergedPRCountByUsername(String username) {
		return getMergedPRListByUsername(username).size();
	}

	public void getOpenPRCountByUsername(String username) {
		// return getOpenPRListByUsername(username).size()
	}

	public void getDeclinedPRCountByUsername(String username) {
		// return getDeclinedPRListByUsername(username).size()
	}







	
	@SuppressWarnings({ "unchecked", "unlikely-arg-type" })
	public List<PullRequest> getAllAuthor(String status, String jsonPath) throws JsonSyntaxException, UnirestException {
		List<PullRequest> array = new ArrayList<PullRequest>();
		HashSet<String> hasSetAuthorName = new HashSet<String>();
		
		
		while (!isLastPage) {
			jsonElement = gson.fromJson(
					response.getResponse(BitbucketConstants.EndPoints.ALL_PRS + start, BitbucketConstants.Bearer.TOKEN).getBody().toString(),
					JsonElement.class);
			jsonInString = gson.toJson(jsonElement);
			authorGetForHash = JsonPath.read(jsonInString, BitbucketConstants.JsonPaths.PR_OWNERS);
			for (int iter = 0; iter < authorGetForHash.size(); iter++) {
				
				hasSetAuthorName.add(authorGetForHash.get(iter));
			}
			start += 100;
			isLastPage = JsonPath.read(gson.toJson(jsonElement), BitbucketConstants.JsonPaths.IS_LAST_PAGE);
		}
		start = 0;
		isLastPage = false;
		authorGetForHash = new ArrayList<String>(hasSetAuthorName);
		
		for (int iter = 0; iter < authorGetForHash.size(); iter++) {
			
			System.out.println(authorGetForHash.get(iter));
			
		
		String jsonPathStatusOpen = "$..[?(@.author.user.displayName =="+"'"+authorGetForHash.get(iter)+"' && @.state == 'OPEN')] ";
		String jsonPathStatusMerge = "$..[?(@.author.user.displayName =="+"'"+authorGetForHash.get(iter)+"' && @.state == 'MERGED')] ";
		String jsonPathStatusDecline = "$..[?(@.author.user.displayName =="+"'"+authorGetForHash.get(iter)+"' && @.state == 'DECLINED')] ";
		System.out.println("girdi");
		
		int mergeTotalTest = countPRVariable(BitbucketConstants.EndPoints.MERGED_PRS, jsonPathStatusMerge);
		int decTotalTest = countPRVariable(BitbucketConstants.EndPoints.DECLINED_PRS, jsonPathStatusDecline);
		int openTotalTest = countPRVariable(BitbucketConstants.EndPoints.OPEN_PRS, jsonPathStatusOpen);
		System.out.println("çıktı");
		
		

		System.out.println("NAme : "+authorGetForHash.get(iter)+"MERGE : " + mergeTotalTest + " , DEC : " + decTotalTest+ " , OPEN:" + openTotalTest);
		
//		PullRequest.getInstance().setAuthorName();
//		PullRequest.getInstance().setTotal();
//		array.add(new PullRequest(PullRequest.getInstance().getAuthorName(),PullRequest.getInstance().getTotal(),0,0,0));
		}
		//String jsonPathStatus = "$..[?(@.author.user.displayName =="+"'"+name+"')].state";
		
		
			
		
		return array;
	} 
	
	
	@SuppressWarnings({"unchecked","unlikely-arg-type"})
	public List<UserPrDetails> getTestPr(String name) throws JsonSyntaxException, UnirestException {
		List<UserPrDetails> array = new ArrayList<UserPrDetails>();
		String jsonPathTitle = "$..[?(@.author.user.displayName =="+"'"+name+"')].title";
		String jsonPathStatus = "$..[?(@.author.user.displayName =="+"'"+name+"')].state";
		String jsonPathPrId = "$..[?(@.author.user.displayName =="+"'"+name+"')].id";
		String jsonPathPrDate = "$..[?(@.author.user.displayName =="+"'"+name+"')].createdDate";
		
		
			while (!isLastPage) {
				String pattern = "dd.MM.yyyy";
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
				
				jsonElement = gson.fromJson(
						response.getResponse(BitbucketConstants.EndPoints.ALL_PRS + start, BitbucketConstants.Bearer.TOKEN).getBody().toString(),
						JsonElement.class);
				jsonInString = gson.toJson(jsonElement);
				partList = JsonPath.read(jsonInString, jsonPathTitle);
				partList2 = JsonPath.read(jsonInString, jsonPathStatus);
				partListPrId = JsonPath.read(jsonInString, jsonPathPrId);
				partListPrDate= JsonPath.read(jsonInString,jsonPathPrDate);
				for (int iter = 0; iter < partList.size(); iter++) {
					authorPRTitle.add(partList.get(iter));
					authorPRState.add(partList2.get(iter));
					authorPRId.add(String.valueOf(partListPrId.get(iter)));
					authorPRDate.add(convertDate(partListPrDate.get(iter)));
					
					
				}
				start += 100;
				isLastPage = JsonPath.read(gson.toJson(jsonElement), BitbucketConstants.JsonPaths.IS_LAST_PAGE);
			}
				start = 0;
				isLastPage = false;
			 	 int mergeTotal = 0;
				 int openTotal = 0;
				 int declinedTotal=0;
				 Integer totalPRs=0;
			
			for(int i = 0 ; i<authorPRState.size(); i++) {
				if(authorPRState.get(i).equals(mergeControl)) {
					mergeTotal++;
				}
				else if(authorPRState.get(i).equals(openControl)) {
					openTotal++;
				}
				else if(authorPRState.get(i).equals(declinedControl)) {
					declinedTotal++;
				}
				
			}
			
			System.out.println(mergeTotal);
			totalPRs =mergeTotal+ openTotal+ declinedTotal;
			System.out.println(openTotal);
			System.out.println(declinedTotal);
			System.out.println(totalPRs);
			
			for(int i = 0 ; i < authorPRTitle.size() ; i++) {
				UserPrDetails.getInstance().setTitle(authorPRTitle.toArray()[i].toString());
				UserPrDetails.getInstance().setStatus(authorPRState.toArray()[i].toString());
				UserPrDetails.getInstance().setPrId(Integer.valueOf(authorPRId.get(i)));
				UserPrDetails.getInstance().setDate(authorPRDate.toArray()[i].toString());
				//dizinin ilk elemanına ekle diğerlerini pas geç if yaz
				UserPrDetails.getInstance().setTotalPRs(totalPRs);
				UserPrDetails.getInstance().setTotalMerges(mergeTotal);
				UserPrDetails.getInstance().setTotalDecline(declinedTotal);
				UserPrDetails.getInstance().setTotalOpen(openTotal);
				array.add(new UserPrDetails(UserPrDetails.getInstance().getTitle(), UserPrDetails.getInstance().getStatus(),
											UserPrDetails.getInstance().getPrId() , UserPrDetails.getInstance().getDate(),
											UserPrDetails.getInstance().getTotalPRs(),UserPrDetails.getInstance().getTotalMerges(),
											UserPrDetails.getInstance().getTotalOpen(),UserPrDetails.getInstance().getTotalDecline()));
			}
		return array;
		
	}
	
	
	@SuppressWarnings({"unchecked","unlikely-arg-type"})
	public List<UserPrDetails> getTestPrWithDate(String name) throws JsonSyntaxException, UnirestException {
		List<UserPrDetails> array = new ArrayList<UserPrDetails>();
		String jsonPathTitle = "$..[?(@.author.user.displayName =="+"'"+name+"')].title";
		String jsonPathStatus = "$..[?(@.author.user.displayName =="+"'"+name+"')].state";
		String jsonPathPrId = "$..[?(@.author.user.displayName =="+"'"+name+"')].id";
		String jsonPathPrDate = "$..[?(@.author.user.displayName =="+"'"+name+"')].createdDate";
		
		
			while (!isLastPage) {
				String pattern = "dd.MM.yyyy";
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
				
				jsonElement = gson.fromJson(
						response.getResponse(BitbucketConstants.EndPoints.ALL_PRS + start, BitbucketConstants.Bearer.TOKEN).getBody().toString(),
						JsonElement.class);
				jsonInString = gson.toJson(jsonElement);
				partList = JsonPath.read(jsonInString, jsonPathTitle);
				partList2 = JsonPath.read(jsonInString, jsonPathStatus);
				partListPrId = JsonPath.read(jsonInString, jsonPathPrId);
				partListPrDate= JsonPath.read(jsonInString,jsonPathPrDate);
				for (int iter = 0; iter < partList.size(); iter++) {
					authorPRTitle.add(partList.get(iter));
					authorPRState.add(partList2.get(iter));
					authorPRId.add(String.valueOf(partListPrId.get(iter)));
					authorPRDate.add(convertDate(partListPrDate.get(iter)));
					
					
				}
				start += 100;
				isLastPage = JsonPath.read(gson.toJson(jsonElement), BitbucketConstants.JsonPaths.IS_LAST_PAGE);
			}
				start = 0;
				isLastPage = false;
			 	 int mergeTotal = 0;
				 int openTotal = 0;
				 int declinedTotal=0;
				 Integer totalPRs=0;
			
			for(int i = 0 ; i<authorPRState.size(); i++) {
				if(authorPRState.get(i).equals(mergeControl)) {
					mergeTotal++;
				}
				else if(authorPRState.get(i).equals(openControl)) {
					openTotal++;
				}
				else if(authorPRState.get(i).equals(declinedControl)) {
					declinedTotal++;
				}
				
			}
			
			System.out.println(mergeTotal);
			totalPRs =mergeTotal+ openTotal+ declinedTotal;
			System.out.println(openTotal);
			System.out.println(declinedTotal);
			System.out.println(totalPRs);
			
			for(int i = 0 ; i < authorPRTitle.size() ; i++) {
				UserPrDetails.getInstance().setTitle(authorPRTitle.toArray()[i].toString());
				UserPrDetails.getInstance().setStatus(authorPRState.toArray()[i].toString());
				UserPrDetails.getInstance().setPrId(Integer.valueOf(authorPRId.get(i)));
				UserPrDetails.getInstance().setDate(authorPRDate.toArray()[i].toString());
				//dizinin ilk elemanına ekle diğerlerini pas geç if yaz
				UserPrDetails.getInstance().setTotalPRs(totalPRs);
				UserPrDetails.getInstance().setTotalMerges(mergeTotal);
				UserPrDetails.getInstance().setTotalDecline(declinedTotal);
				UserPrDetails.getInstance().setTotalOpen(openTotal);
				array.add(new UserPrDetails(UserPrDetails.getInstance().getTitle(), UserPrDetails.getInstance().getStatus(),
											UserPrDetails.getInstance().getPrId() , UserPrDetails.getInstance().getDate(),
											UserPrDetails.getInstance().getTotalPRs(),UserPrDetails.getInstance().getTotalMerges(),
											UserPrDetails.getInstance().getTotalOpen(),UserPrDetails.getInstance().getTotalDecline()));
			}
		return array;
		
	}
	

	private int countPRVariable(String endPoints, String jsonPath) {
		 String jsonInString = null;
		  JsonElement jsonElement = null;
		  List<String> partList = null;
		int countVariable = 0;
		while (!isLastPage) {
			try {
				jsonElement = gson.fromJson(
						response.getResponse(endPoints+start, BitbucketConstants.Bearer.TOKEN).getBody().toString(),
						JsonElement.class);
				jsonInString = gson.toJson(jsonElement);
	
				partList = JsonPath.read(jsonInString, jsonPath);

				countVariable += partList.size();

				start += 100;
	
				isLastPage = JsonPath.read(gson.toJson(jsonElement), BitbucketConstants.JsonPaths.IS_LAST_PAGE);
	
			}catch(Exception e){
				isLastPage = true;
				countVariable = 0;
				System.out.println("Variable is not a find.");
			}
		}
		isLastPage = false;
		start = 0 ;
		System.out.println("test");



		return countVariable;
	}

	




	


	private List<String> splitInteger(String searchStr) {
		Pattern integerPattern = Pattern.compile("-?\\d+");
		Matcher matcher = integerPattern.matcher(searchStr);

		List<String> integerList = new ArrayList<String>();
		while (matcher.find()) {
			integerList.add(matcher.group());
		}
		return integerList;
	}

	private String convertDate(Long date) {
		String pattern = "dd.MM.yyyy";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String newDate = simpleDateFormat.format(date);
		return newDate;
		
	}

	@SuppressWarnings("unchecked")
	public <T> Map<T, Long> count(List<T> inputList) {
		return (Map<T, Long>) inputList.stream().collect(Collectors.groupingBy(new Function<Object, Object>() {
			public Object apply(Object k) {
				return k;
			}
		}, Collectors.counting()));
	}
	@SuppressWarnings("unchecked")
	public <T> Map<T, Long> prStateCount(List<T> prState) {
		return (Map<T, Long>) prState.stream().collect(Collectors.groupingBy(new Function<Object, Object>() {
			public Object apply(Object a) {
				return a;
			}
		}, Collectors.counting()));
	}
	
}
