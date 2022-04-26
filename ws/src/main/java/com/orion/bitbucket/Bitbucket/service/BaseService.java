package com.orion.bitbucket.Bitbucket.service;
import java.text.SimpleDateFormat;
import java.util.*;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.orion.bitbucket.Bitbucket.model.PullRequestDO;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mashape.unirest.http.exceptions.UnirestException;


@Service
public class BaseService {

	@Autowired
	private JsonResponse response;
	ArrayList<PullRequestDO> openPRList;
	ArrayList<PullRequestDO> mergedPRList;
	ArrayList<PullRequestDO> declinedPRList;

	public void getData() {
			try{
				this.openPRList = getPullRequestData(BitbucketConstants.EndPoints.OPEN_PRS);
				this.mergedPRList = getPullRequestData(BitbucketConstants.EndPoints.MERGED_PRS);
				this.declinedPRList = getPullRequestData(BitbucketConstants.EndPoints.DECLINED_PRS);
			}
			catch(Exception e){
			System.out.println(e);
			}
	}
	
	// takes a state input
	public ArrayList<PullRequestDO> getPullRequestData(String state) throws UnirestException {
		ArrayList<PullRequestDO> list = new ArrayList<PullRequestDO>();

		int start = 0;
		boolean isLastPage = false;
		while (!isLastPage) {
			HttpResponse<JsonNode> httpResponse = response.getResponse(state + start, BitbucketConstants.Bearer.TOKEN);
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

	public int getOpenPRCount() {
		 return openPRList.size();
	}

	public int getDeclinedPRCount() {
		 return declinedPRList.size();
	}

	public ArrayList<PullRequestDO> getMergedPRList() {
		return this.mergedPRList;
	}

	public ArrayList<PullRequestDO> getOpenPRList() {
		 return this.openPRList;
	}

	public ArrayList<PullRequestDO>  getDeclinedPRList() {
		 return this.declinedPRList;
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

	public ArrayList<PullRequestDO> getOpenPRListByUsername(String username) {
		ArrayList<PullRequestDO> list = new ArrayList<PullRequestDO>();
		for (int i=0; i<this.openPRList.size(); i++) {
			if (openPRList.get(i).getSlug().equals(username)) {
				list.add(openPRList.get(i));
			}
		}
		return list;
	}

	public ArrayList<PullRequestDO> getDeclinedPRListByUsername(String username) {
		ArrayList<PullRequestDO> list = new ArrayList<PullRequestDO>();
		for (int i=0; i<this.declinedPRList.size(); i++) {
			if (declinedPRList.get(i).getSlug().equals(username)) {
				list.add(declinedPRList.get(i));
			}
		}
		return list;
	}

	public int getMergedPRCountByUsername(String username) {
		return getMergedPRListByUsername(username).size();
	}

	public int getOpenPRCountByUsername(String username) {
		 return getOpenPRListByUsername(username).size();
	}

	public int getDeclinedPRCountByUsername(String username) {
		return getDeclinedPRListByUsername(username).size();
	}


	private String convertDate(Long date) {
		String pattern = "dd.MM.yyyy";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String newDate = simpleDateFormat.format(date);
		return newDate;
		
	}

	
	
}
