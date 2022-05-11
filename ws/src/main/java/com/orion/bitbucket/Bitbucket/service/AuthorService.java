package com.orion.bitbucket.Bitbucket.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.orion.bitbucket.Bitbucket.model.AuthorDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorService extends BaseService implements AuthorServiceIF {

    @Autowired
    private PullRequestServiceIF pullRequestServiceIF;

    public int getAuthorCount() {
        return getAllAuthor().size();
    }

    public ArrayList<String> getAllAuthor() {
        ArrayList<String> authorList = new ArrayList<String>();
        for (int i = 0; i < this.allPRList.size(); i++) {
            if (!authorList.contains(this.allPRList.get(i).getDisplayName())) {
                authorList.add(this.allPRList.get(i).getDisplayName());
            }
        }
        return authorList;
    }

    public ArrayList<AuthorDO> getCountOfPrStatesOfAllAuthor() {
        ArrayList<String> authorList = getAllAuthor();
        ArrayList<AuthorDO> authorDOList = new ArrayList<AuthorDO>();
        int total;
        int merge = 0;
        int declined = 0;
        int open = 0;
        for (int i = 0; i < authorList.size(); i++) {
            merge = pullRequestServiceIF.getMergedPRCountByUsername(authorList.get(i));
            declined = pullRequestServiceIF.getDeclinedPRCountByUsername(authorList.get(i));
            open = pullRequestServiceIF.getOpenPRCountByUsername(authorList.get(i));
            total = merge + open + declined;
            authorDOList.add(new AuthorDO(authorList.get(i), total, merge, open, declined));
        }
        return authorDOList;
    }


    public Map<String, Long> getTopAuthor() {
        ArrayList<String> allAuthorDisplayName = new ArrayList<String>();
        for (int i = 0; i < this.allPRList.size(); i++) {
            allAuthorDisplayName.add(this.allPRList.get(i).getDisplayName());
        }
     String topAuthorDisplayName = Helper.count(allAuthorDisplayName).entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();
     long topAuthorCount = Helper.count(allAuthorDisplayName).entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getValue();
     Map<String, Long> topAuthor = new HashMap<>();
     topAuthor.put(topAuthorDisplayName, topAuthorCount);
     return topAuthor;
    }

}
