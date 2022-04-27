
package com.orion.bitbucket.Bitbucket.service;
import java.util.ArrayList;

import com.orion.bitbucket.Bitbucket.model.PullRequestDO;
import org.springframework.stereotype.Service;


@Service
public class PullRequestService extends BaseService{
   // bu service base servisi (serviceimpl) i extend etsin.
   // burayla ilgili olabilecek metotlar burada tanımlanson.
   // mesela:
   // public ArrayList<PullRequestDO> getMergedPRList() {
   //		return this.mergedPRList;
   //	}
   // base classs da ise yalnızca common şeyler olsun. mesela data çekme şeyleri. getData gibi olanlar.
  

   public ArrayList<PullRequestDO> declinedPRList(){
      return this.getDeclinedPRList();
   }

   public ArrayList<PullRequestDO> mergedPRList(){
      return this.mergedPRList;
    }

   public ArrayList<PullRequestDO> openPRList(){
     return this.getOpenPRList();
   }

   public int getMergedPRCount() {
		return getMergedPRList().size();
	}

	public int getOpenPRCount() {
		 return getOpenPRList().size();
	}

	public int getDeclinedPRCount() {
		 return getDeclinedPRList().size();
	}

   public void getDataAll() {
      this.getData();
   }
   
}
