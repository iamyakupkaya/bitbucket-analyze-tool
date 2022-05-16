package com.orion.bitbucket.Bitbucket.service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.orion.bitbucket.Bitbucket.model.BranchDO;
import com.orion.bitbucket.Bitbucket.model.PullRequestDO;
import com.orion.bitbucket.Bitbucket.model.ReviewDO;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

@Service
public class BaseService implements BaseServiceIF {

    @Autowired
    private JsonResponse response;
    static ArrayList<PullRequestDO> openPRList;
    static ArrayList<PullRequestDO> mergedPRList;
    static ArrayList<PullRequestDO> declinedPRList;
    static ArrayList<PullRequestDO> allPRList;
    static ArrayList<BranchDO> branchList;

    // Call this method while application is running at first time
    // TODO At future, we have to apply multithreading in below
    public void getData() {
        try {
            Instant start = Instant.now();
            this.openPRList = getPullRequestData(BitbucketConstants.EndPoints.OPEN_PRS);
            this.mergedPRList = getPullRequestData(BitbucketConstants.EndPoints.MERGED_PRS);
            this.declinedPRList = getPullRequestData(BitbucketConstants.EndPoints.DECLINED_PRS);
            // this.branchList = getBranchData();
            Instant finish = Instant.now();
            Duration timeElapsed = Duration.between(start, finish);
            System.out.println("Response time to retrieve all merge, open and declined PRs: " + timeElapsed.toSeconds() + " seconds.");
            createAllPRList();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void createAllPRList() {
        this.allPRList = new ArrayList<PullRequestDO>();
        allPRList.addAll(this.openPRList);
        allPRList.addAll(this.mergedPRList);
        allPRList.addAll(this.declinedPRList);
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
            for (int i = 0; i < array.length(); i++) {
                list.add(commonPullRequestDataParser(array.getJSONObject(i)));
            }
            isLastPage = (boolean) body.get("isLastPage");
            start += 100;
        }
        return list;
    }

    public PullRequestDO commonPullRequestDataParser(JSONObject object) {
        int prId = (int) object.get("id");
        String title = (String) object.get("title");
        String state = (String) object.get("state");
        boolean closed = (boolean) object.get("closed");
        String description = object.optString("description");
        long updatedDate = (long) object.get("updatedDate");
        long createdDate = (long) object.get("createdDate");
        long closedDate = object.optLong("closedDate");
        
        // Author Information
        JSONObject author = object.getJSONObject("author");
        JSONObject user = author.getJSONObject("user");
        String emailAddress = user.optString("emailAddress");
        String displayName = (String) user.get("displayName");
        String slug = (String) user.get("slug");
        
        // Reviewer Information
        ArrayList<ReviewDO> reviewerList = new ArrayList<ReviewDO>();
        JSONArray reviewers = object.getJSONArray("reviewers");
        for (int i = 0; i < reviewers.length(); i++) {
            JSONObject reviewer = reviewers.getJSONObject(i);
            user = reviewer.getJSONObject("user");
            String reviewerDisplayName = (String) user.get("displayName");
            String reviewerEmailAddress = user.optString("emailAddress");
            String status = reviewer.optString("status");
            boolean reviewerApproved = reviewer.optBoolean("approved");
            reviewerList.add(new ReviewDO(reviewerDisplayName, reviewerEmailAddress, status, reviewerApproved));
        }
        return new PullRequestDO(prId, title, state, closed, description, updatedDate, convertDate(createdDate), convertDate(closedDate), emailAddress, displayName, slug, reviewerList);
    }

    // TODO Branch url will be added onto constants, then we can call it while getting data
    public ArrayList<BranchDO> getBranchData(String url) throws UnirestException {
        ArrayList<BranchDO> list = new ArrayList<BranchDO>();

        int start = 0;
        boolean isLastPage = false;
        while (!isLastPage) {
            HttpResponse<JsonNode> httpResponse = response.getResponse(url + start, BitbucketConstants.Bearer.TOKEN);
            JSONObject body = httpResponse.getBody().getObject();
            Object values = body.get("values");
            JSONArray array = (JSONArray) values;
            for (int i = 0; i < array.length(); i++) {
                //list.add(commonBranchDataParser(array.getJSONObject(i)));
            }
            isLastPage = (boolean) body.get("isLastPage");
            start += 100;
        }
        return list;
    }

    // TODO Below method will be modified for branch data
    public void commonBranchDataParser(JSONObject object) {
    }

    private String convertDate(Long date) {
        String pattern = "dd.MM.yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String newDate = simpleDateFormat.format(date);
        return newDate;
    }
}