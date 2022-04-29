package com.orion.bitbucket.Bitbucket.service;
import java.util.ArrayList;
import com.orion.bitbucket.Bitbucket.model.AuthorDO;

public class AuthorService extends BaseService implements AuthorServiceIF{

    private PullRequestService pullRequestService;

    public ArrayList<AuthorDO> getCountOfPrStatesOfAllAuthor() {
        ArrayList<String> authorList = pullRequestService.getAllAuthor();
        ArrayList<AuthorDO> authorDO = new ArrayList<AuthorDO>();
        int total;
        int merge = 0;
        int declined = 0;
        int open = 0;
        for (int i = 0; i < authorList.size(); i++) {
            merge =pullRequestService.getMergedPRCountByUsername(authorList.get(i));
            declined = pullRequestService.getDeclinedPRCountByUsername(authorList.get(i));
            open = pullRequestService.getOpenPRCountByUsername(authorList.get(i));
           total =  merge + open + declined;
           authorDO.add(new AuthorDO(authorList.get(i), total, merge, open, declined));
        }
        return authorDO;
     }
}
