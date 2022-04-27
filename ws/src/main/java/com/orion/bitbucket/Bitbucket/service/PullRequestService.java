package com.orion.bitbucket.Bitbucket.service;
import java.util.ArrayList;

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

   /*
   public void author() {
      ArrayList<String> authorList = new ArrayList<String>();

      yeni liste = mergedPRList+openPRList+declinedPRList
      for (int i=0; i<yeniliste.size() i++) {
         if (!authorlist.contains( yeni liste[i].displayName)) {
            authorliste.add(yeniliste[i])
         }
      }
   }
   */

}