
package com.orion.bitbucket.Bitbucket.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonSyntaxException;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.orion.bitbucket.Bitbucket.model.PullRequest;
import com.orion.bitbucket.Bitbucket.model.UserPrDetails;

@Service
public class PullRequestService {
   // bu service base servisi (serviceimpl) i extend etsin.
   // burayla ilgili olabilecek metotlar burada tanımlanson.
   // mesela:
   // public ArrayList<PullRequestDO> getMergedPRList() {
   //		return this.mergedPRList;
   //	}
   // base classs da ise yalnızca common şeyler olsun. mesela data çekme şeyleri. getData gibi olanlar.
   @Autowired
   private ServiceImpl service;
//asd
   public String all() throws JsonSyntaxException, UnirestException {
      return service.getPullRequests(BitbucketConstants.EndPoints.ALL_PRS, BitbucketConstants.JsonPaths.PR_OWNERS);
   }

   public String open() throws JsonSyntaxException, UnirestException {
      return service.getPullRequests(BitbucketConstants.EndPoints.OPEN_PRS, BitbucketConstants.JsonPaths.PR_OWNERS);
   }

   public String merged() throws JsonSyntaxException, UnirestException {
      return service.getPullRequests(BitbucketConstants.EndPoints.MERGED_PRS, BitbucketConstants.JsonPaths.PR_OWNERS);
   }

   public String declined() throws JsonSyntaxException, UnirestException {
      return service.getPullRequests(BitbucketConstants.EndPoints.DECLINED_PRS, BitbucketConstants.JsonPaths.PR_OWNERS);
   }
   
   public List<PullRequest> reportAll() throws JsonSyntaxException, UnirestException {
      return service.getAll(BitbucketConstants.EndPoints.ALL_PRS, BitbucketConstants.JsonPaths.PR_OWNERS);
   }


   
   public List<PullRequest> reportAllAuthorTest() throws JsonSyntaxException, UnirestException {
	      return service.getAllAuthor(BitbucketConstants.EndPoints.ALL_PRS, BitbucketConstants.JsonPaths.PR_OWNERS);
	   }
   
   public List<UserPrDetails> filterPRsByName(String name) throws JsonSyntaxException, UnirestException {
	      return service.getTestPr(name);
	   }
   
   public void getData() {
      service.getData();
   }
   
}
