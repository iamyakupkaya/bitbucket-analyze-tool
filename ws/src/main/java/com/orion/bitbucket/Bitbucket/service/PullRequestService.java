package com.orion.bitbucket.Bitbucket.service;
import java.util.ArrayList;
import java.util.List;
import com.orion.bitbucket.Bitbucket.model.Authors;
import com.orion.bitbucket.Bitbucket.model.PullRequestDO;
import org.springframework.stereotype.Service;

@Service
public class PullRequestService extends BaseService implements PullRequestServiceIF {

   public int getMergedPRCount() {
      return this.mergedPRList.size();
   }

   public int getOpenPRCount() {
      return this.openPRList.size();
   }

   public int getDeclinedPRCount() {
      return this.declinedPRList.size();
   }

   public ArrayList<PullRequestDO> getMergedPRList() {
      return this.mergedPRList;
   }

   public ArrayList<PullRequestDO> getOpenPRList() {
      return this.openPRList;
   }

   public ArrayList<PullRequestDO> getDeclinedPRList() {
      return this.declinedPRList;
   }

   // Not sure how it works :)))
   //
   // TODO: Discussion yapabiliriz bu metodun yaptigi burada mi olmali yoksa
   // getData icerisinde mi yapilmali.
   public ArrayList<PullRequestDO> getAllPRList() {
      this.allPRList = new ArrayList<PullRequestDO>();
      allPRList.addAll(this.openPRList);
      allPRList.addAll(this.mergedPRList);
      allPRList.addAll(this.declinedPRList);
      return this.allPRList;
   }

   public ArrayList<PullRequestDO> getMergedPRListByUsername(String username) {
      ArrayList<PullRequestDO> list = new ArrayList<PullRequestDO>();
      for (int i = 0; i < this.mergedPRList.size(); i++) {
         if (mergedPRList.get(i).getDisplayName().equals(username)) {
            list.add(mergedPRList.get(i));
         }
      }
      return list;
   }

   public ArrayList<PullRequestDO> getOpenPRListByUsername(String username) {
      ArrayList<PullRequestDO> list = new ArrayList<PullRequestDO>();
      for (int i = 0; i < this.openPRList.size(); i++) {
         if (openPRList.get(i).getDisplayName().equals(username)) {
            list.add(openPRList.get(i));
         }
      }
      return list;
   }

   public ArrayList<PullRequestDO> getDeclinedPRListByUsername(String username) {
      ArrayList<PullRequestDO> list = new ArrayList<PullRequestDO>();
      for (int i = 0; i < this.declinedPRList.size(); i++) {
         if (declinedPRList.get(i).getDisplayName().equals(username)) {
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

   public List<Authors> getCountOfPrStatesOfAllAuthor() {
      List<Authors> array = new ArrayList<Authors>();
      ArrayList<String> authorList = new ArrayList<String>();
      ArrayList<PullRequestDO> allPRs = getAllPRList();
      for (int i = 0; i < allPRs.size(); i++) {
         if (!authorList.contains(allPRs.get(i).getDisplayName())) {
            authorList.add(allPRs.get(i).getDisplayName());
         }
      }
      int total;
      for (int i = 0; i < authorList.size(); i++) {
         total = getMergedPRCountByUsername(authorList.get(i)) + getOpenPRCountByUsername(authorList.get(i)) + getDeclinedPRCountByUsername(authorList.get(i));
         array.add(new Authors(authorList.get(i), total, getMergedPRCountByUsername(authorList.get(i)), getOpenPRCountByUsername(authorList.get(i)), getDeclinedPRCountByUsername(authorList.get(i))));
      }
      return array;
   }
   
}