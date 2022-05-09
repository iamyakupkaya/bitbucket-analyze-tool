package com.orion.bitbucket.Bitbucket.service;

import java.util.ArrayList;

import com.orion.bitbucket.Bitbucket.model.AuthorDO;
import com.orion.bitbucket.Bitbucket.model.PullRequestDO;
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
        //ArrayList<PullRequestDO> allPRs = this.allPRList;
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
}
