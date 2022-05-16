package com.orion.bitbucket.Bitbucket.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.orion.bitbucket.Bitbucket.model.AuthorDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class AuthorService extends BaseService implements AuthorServiceIF {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
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
                String sql = "INSERT INTO review (name)" +" VALUES ('"+this.allPRList.get(i).getDisplayName()+"')";
                int rows = jdbcTemplate.update(sql);
               
                if(rows > 0) {
                	System.out.println("Succes.");
                }
            }
        }
        
        
       
        return authorList;
    }
//    String jdbcURL = "jdbc:postgresql://localhost:5432/bitbucket";
//    String username = "postgres";
//    String password = "12345";
//    
//    try {
//    	Connection connection = DriverManager.getConnection(jdbcURL, username, password);
//    	System.out.println("Succes to db.");
//    	String sql = "INSERT INTO Author (author_name, total_prs, total_merged"
//    			+ ", total_open, total_declined)" +" VALUES ('"+this.allPRList.get(i).getDisplayName()+"'"+",4,1,1,1)";
//    	Statement statement = connection.createStatement();
//    	int rows = statement.executeUpdate(sql);
//    	if(rows > 0) {
//    		System.out.println("Başarılı");
//    	}
//    	connection.close();
//    	
//    	
//    }catch (SQLException e) {
//    	System.out.println("Başarısız.");
//    	e.printStackTrace();
//    }
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

    public ArrayList<AuthorDO> getCountOfPrStatesWithDisplayName(String name) {
        ArrayList<AuthorDO> authorDOList = getCountOfPrStatesOfAllAuthor();
        ArrayList<AuthorDO> authorDO = new ArrayList<AuthorDO>();
        for (int i = 0; i < authorDOList.size(); i++) {
            if (authorDOList.get(i).getName().toString().equals(name)) {
                authorDO.add(new AuthorDO(authorDOList.get(i).getName(),authorDOList.get(i).getTotalPRs(),authorDOList.get(i).getTotalMergedPRs(),authorDOList.get(i).getTotalOpenPRs(),authorDOList.get(i).getTotalDeclinedPRs()));
            }
        }
        return authorDO;
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
