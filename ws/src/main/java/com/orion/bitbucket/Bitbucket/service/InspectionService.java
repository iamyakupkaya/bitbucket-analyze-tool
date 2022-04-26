
package com.orion.bitbucket.Bitbucket.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.mashape.unirest.http.exceptions.UnirestException;

@Service
public class InspectionService {

   private Gson         gson         = new GsonBuilder().setPrettyPrinting().create();
   private JsonElement  jsonElement  = null;
   private String       jsonInString = null;
   
   private JsonResponse response;
   private ServiceImpl service;
   
   @Autowired
   public InspectionService(JsonResponse response, ServiceImpl service) {
      this.response = response;
      this.service = service;
   }

   public String getPRreviewers(int prId) throws JsonSyntaxException, UnirestException {
      List<String> list = null;
      try {
         jsonElement = gson.fromJson(response.getResponse(BitbucketConstants.EndPoints.INSPECTORS_OF_PR + prId, BitbucketConstants.Bearer.TOKEN)
                                             .getBody()
                                             .toString(),
                                     JsonElement.class);
         jsonInString = gson.toJson(jsonElement);
         list = JsonPath.read(jsonInString, BitbucketConstants.JsonPaths.PR_REVIEWERS);
      }
      catch (PathNotFoundException e) {
         return "Pull request not found.";
      }

      return (list.size() != 0 ) ? list.toString() : "No reviewers found.";
   }
   
   public String getReviewCount() throws JsonSyntaxException, UnirestException {
      return service.getPullRequests(BitbucketConstants.EndPoints.ALL_PRS, BitbucketConstants.JsonPaths.ALL_REVIEWERS); 
   }
   
   public String getApprovalCounts() throws JsonSyntaxException, UnirestException {
      return service.getApprovals(BitbucketConstants.EndPoints.MERGED_PRS, BitbucketConstants.JsonPaths.ALL_REVIEWERS, BitbucketConstants.JsonPaths.PR_APPROVAL);
   }
  
   
}
