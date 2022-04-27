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
	ArrayList<PullRequestDO> allPRList;

	// TODO: BaseService için de bir interface olsun, yalnızca bu metot olsun.
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

	public ArrayList<PullRequestDO> getPullRequestData(String url) throws UnirestException {
		ArrayList<PullRequestDO> list = new ArrayList<PullRequestDO>();

		int start = 0;
		boolean isLastPage = false;
		while (!isLastPage) {
			HttpResponse<JsonNode> httpResponse = response.getResponse(url + start, BitbucketConstants.Bearer.TOKEN);
			JSONObject body = httpResponse.getBody().getObject();
			Object values = body.get("values");
			JSONArray array = (JSONArray) values;
			for (int i=0; i< array.length(); i++) {
				list.add(commonDataParser(array.getJSONObject(i)));
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

	private String convertDate(Long date) {
		String pattern = "dd.MM.yyyy";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String newDate = simpleDateFormat.format(date);
		return newDate;
		
	}
}