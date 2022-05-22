package com.orion.bitbucket.Bitbucket.service;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.orion.bitbucket.Bitbucket.dbc.TransactionManager;
import com.orion.bitbucket.Bitbucket.model.PullRequestDO;
import com.orion.bitbucket.Bitbucket.model.ReviewDO;
import com.orion.bitbucket.Bitbucket.model.ReviewerDO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService extends BaseService implements ReviewServiceIF {

    @Autowired
    private PullRequestServiceIF pullRequestServiceIF;

    private final String SQL_GET_ALL_REVIEW_COUNT = "select count(*) from review;";
    private final String SQL_GET_REVIEWS_BY_USERNAME = "select * from review where display_name=?;";
    private final String SQL_GET_REVIEWS_BY_ID = "select * from review where id=?;";
    private final String SQL_GET_PULL_REQUEST_ID_FROM_RELATION_TABLE = "select pull_request_id from pullrequestreviewrelation where review_id=?;";
    private final String SQL_GET_REVIEW_ID_BY_USERNAME = "select id from review where display_name=? order by id desc limit 1;";

    // TODO burada olmamali
    public ArrayList<String> getAllReviewer() {
        ArrayList<String> reviewerList = new ArrayList<String>();
        for (int i = 0; i < getAllReview().size(); i++) {
            if (!reviewerList.contains(getAllReview().get(i).getDisplayName())) {
                reviewerList.add(getAllReview().get(i).getDisplayName());
            }
        }
        return reviewerList;
    }

    // TODO burada olmamali
    public int getAllReviewerCount() {
        return getAllReviewer().size();
    }

    public ArrayList<ReviewDO> getAllReview() {
        ArrayList<ReviewDO> allReviewer = new ArrayList<ReviewDO>();
        allReviewer.addAll(getMergedPRReviewList());
        allReviewer.addAll(getOpenPRReviewList());
        allReviewer.addAll(getDeclinedPRReviewList());
        return allReviewer;
    }

    public int getReviewCount() throws SQLException {
        Connection connection = TransactionManager.getConnection();
        int count = 0;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL_GET_ALL_REVIEW_COUNT);
        while (resultSet.next()) {
            count = resultSet.getInt("count");
        }
        resultSet.close();
        statement.close();
        connection.close();
        return count;
    }

    public ArrayList<ReviewDO.PullRequestReviewRelation> getReviewsByUsername(String username) throws SQLException {
        ArrayList<ReviewDO> list = new ArrayList<>();
        Connection connection = TransactionManager.getConnection();
        PreparedStatement preparedStmt = null;
        preparedStmt = connection.prepareStatement(SQL_GET_REVIEWS_BY_USERNAME);
        preparedStmt.setString(1, username);
        ResultSet resultSet = preparedStmt.executeQuery();
        connection.commit();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String displayName = resultSet.getString("display_name");
            String emailAddress = resultSet.getString("email_address");
            boolean approved = resultSet.getBoolean("approved");
            String status = resultSet.getString("status");
            list.add(new ReviewDO(id, displayName, emailAddress, status, approved));
        }
        resultSet.close();
        preparedStmt.close();
        connection.close();

        ArrayList<ReviewDO.PullRequestReviewRelation> pullRequestReviewRelations = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            connection = TransactionManager.getConnection();
            int pullRequestId = 0;
            preparedStmt = null;
            preparedStmt = connection.prepareStatement(SQL_GET_PULL_REQUEST_ID_FROM_RELATION_TABLE);
            preparedStmt.setInt(1, list.get(i).getId());
            resultSet = preparedStmt.executeQuery();
            connection.commit();
            while (resultSet.next()) {
                pullRequestId = resultSet.getInt("pull_request_id");
            }
            resultSet.close();
            preparedStmt.close();
            connection.close();

            PullRequestDO pullRequest = pullRequestServiceIF.getPullRequestById(pullRequestId);
            pullRequestReviewRelations.add(new ReviewDO.PullRequestReviewRelation(pullRequest, list.get(i)));

        }
        return pullRequestReviewRelations;
    }

    public int getReviewIdByUsername(String username) throws SQLException {
        Connection connection = TransactionManager.getConnection();
        int id = 0;
        PreparedStatement preparedStmt = null;
        preparedStmt = connection.prepareStatement(SQL_GET_REVIEW_ID_BY_USERNAME);
        preparedStmt.setString(1, username);
        ResultSet resultSet = preparedStmt.executeQuery();
        connection.commit();
        while (resultSet.next()) {
            id = resultSet.getInt("id");
        }
        resultSet.close();
        preparedStmt.close();
        connection.close();
        return id;
    }

    public ArrayList<ReviewDO.PullRequestReviewRelation> getReviewsWithPullRequestState(String state)
            throws SQLException {
        ArrayList<PullRequestDO> pullRequestList = pullRequestServiceIF.getPRListByState(state);
        ArrayList<ReviewDO.PullRequestReviewRelation> pullRequestReviewRelations = new ArrayList<>();

        String SQL_GET_REWIEW_ID_FROM_RELATION_TABLE = "select review_id from pullrequestreviewrelation where pull_request_id=?;";
        for (int i = 0; i < pullRequestList.size(); i++) {
            PullRequestDO pullRequest = pullRequestServiceIF.getPullRequestById(pullRequestList.get(i).getPrId());
            Connection connection = TransactionManager.getConnection();
            PreparedStatement preparedStmt = null;
            preparedStmt = connection.prepareStatement(SQL_GET_REWIEW_ID_FROM_RELATION_TABLE);
            preparedStmt.setInt(1, pullRequestList.get(i).getPrId());
            ResultSet resultSet = preparedStmt.executeQuery();
            connection.commit();
            while (resultSet.next()) {
                int review_id = resultSet.getInt("review_id");
                PreparedStatement preparedStmtTwo = null;
                preparedStmtTwo = connection.prepareStatement(SQL_GET_REVIEWS_BY_ID);
                preparedStmtTwo.setInt(1, review_id);
                ResultSet resultSetTwo = preparedStmtTwo.executeQuery();
                connection.commit();
                while (resultSetTwo.next()) {
                    int id = resultSetTwo.getInt("id");
                    String displayName = resultSetTwo.getString("display_name");
                    String emailAddress = resultSetTwo.getString("email_address");
                    boolean approved = resultSetTwo.getBoolean("approved");
                    String status = resultSetTwo.getString("status");
                    pullRequestReviewRelations.add(new ReviewDO.PullRequestReviewRelation(pullRequest,
                            new ReviewDO(id, displayName, emailAddress, status, approved)));
                }
                resultSetTwo.close();
                preparedStmtTwo.close();
            }
            resultSet.close();
            preparedStmt.close();
            connection.close();
        }

        return pullRequestReviewRelations;
    }

   

    // En fazla review edilen pull request
    // En fazla review edilen 5 pull request (gereksiz olabilir bu)
    // En az review yapan kisi
    // Returns a list that is consists of pull request reviewed by username
    public ArrayList<PullRequestDO> getMergedPRListReviewedByUsername(String username) {
        ArrayList<PullRequestDO> list = new ArrayList<PullRequestDO>();
        ArrayList<PullRequestDO> mergedPRList = this.mergedPRList;
        for (int i = 0; i < mergedPRList.size(); i++) {
            ArrayList<ReviewDO> reviewers = mergedPRList.get(i).getReviewerList();
            for (int j = 0; j < reviewers.size(); j++) {
                if (reviewers.get(j).getDisplayName().equals(username)) {
                    list.add(mergedPRList.get(i));
                }
            }
        }
        return list;
    }

    public int getReviewerApproveCount(String username) {
        ArrayList<PullRequestDO> reviewerAllPRReview = new ArrayList<PullRequestDO>();
        reviewerAllPRReview.addAll(getMergedPRListReviewedByUsername(username));
        reviewerAllPRReview.addAll(getOpenPRListReviewedByUsername(username));
        reviewerAllPRReview.addAll(getDeclinedPRListReviewedByUsername(username));

        int totalApprove = 0;
        String a = "APPROVED";
        for (int i = 0; i < reviewerAllPRReview.size(); i++) {
            if (reviewerAllPRReview.get(i).getReviewerList().get(i).getStatus().toString().equals(a)) {
                totalApprove++;
            } else {
                System.out.println(reviewerAllPRReview.get(i).getReviewerList().get(i).getStatus());
                System.out.println("Değer boş");
            }
        }

        return totalApprove;
    }

    public int getReviewerUnApproveCount(String username) {
        ArrayList<ReviewerDO> mergedReviewer = new ArrayList<ReviewerDO>();

        ArrayList<PullRequestDO> reviewerAllPRReview = new ArrayList<PullRequestDO>();
        reviewerAllPRReview.addAll(getMergedPRListReviewedByUsername(username));
        reviewerAllPRReview.addAll(getOpenPRListReviewedByUsername(username));
        reviewerAllPRReview.addAll(getDeclinedPRListReviewedByUsername(username));

        int totalUnApprove = 0;
        String a = "UNAPPROVED";
        for (int i = 0; i < reviewerAllPRReview.size(); i++) {
            if (reviewerAllPRReview.get(i).getReviewerList().get(i).getStatus().equals(a)) {
                totalUnApprove++;
            } else {
                System.out.println(reviewerAllPRReview.get(i).getReviewerList().get(i).getStatus());
                System.out.println("Değer boş");
            }
        }

        return totalUnApprove;
    }

    public Map<String, Long> getTopReviewer() {
        ArrayList<ReviewDO> allReviewer = getAllReview();
        ArrayList<String> allReviewerDisplayName = new ArrayList<String>();
        for (int i = 0; i < allReviewer.size(); i++) {
            allReviewerDisplayName.add(allReviewer.get(i).getDisplayName());
        }
        String topReviewerDisplayName = Helper.count(allReviewerDisplayName).entrySet().stream()
                .max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();
        long topReviewerCount = Helper.count(allReviewerDisplayName).entrySet().stream()
                .max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getValue();
        Map<String, Long> topReviewer = new HashMap<>();
        topReviewer.put(topReviewerDisplayName, topReviewerCount);
        return topReviewer;
    }

    public ArrayList<PullRequestDO> getOpenPRListReviewedByUsername(String username) {
        ArrayList<PullRequestDO> list = new ArrayList<PullRequestDO>();
        ArrayList<PullRequestDO> openPRList = this.openPRList;
        for (int i = 0; i < openPRList.size(); i++) {
            ArrayList<ReviewDO> reviewers = openPRList.get(i).getReviewerList();
            for (int j = 0; j < reviewers.size(); j++) {
                if (reviewers.get(j).getDisplayName().equals(username)) {
                    list.add(openPRList.get(i));
                }
            }
        }
        return list;
    }

    public ArrayList<PullRequestDO> getDeclinedPRListReviewedByUsername(String username) {
        ArrayList<PullRequestDO> list = new ArrayList<PullRequestDO>();
        ArrayList<PullRequestDO> declinedPRList = this.declinedPRList;
        for (int i = 0; i < declinedPRList.size(); i++) {
            ArrayList<ReviewDO> reviewers = declinedPRList.get(i).getReviewerList();
            for (int j = 0; j < reviewers.size(); j++) {
                if (reviewers.get(j).getDisplayName().equals(username)) {
                    list.add(declinedPRList.get(i));
                }
            }
        }
        return list;
    }

    // Returns a number that indicates a specific user has reviewed pull requests
    // that are merged
    public int getMergedPRCountReviewedByUsername(String username) {
        return this.getMergedPRListReviewedByUsername(username).size();
    }

    public int getOpenPRCountReviewedByUsername(String username) {
        return this.getOpenPRListReviewedByUsername(username).size();
    }

    public int getDeclinedPRCountReviewedByUsername(String username) {
        return this.getDeclinedPRListReviewedByUsername(username).size();
    }

    public ArrayList<ReviewDO> getReviewersByPRId(int id) {
        ArrayList<PullRequestDO> allPR = this.allPRList;
        ArrayList<ReviewDO> getReviewerWithPrId = new ArrayList<ReviewDO>();
        for (int i = 0; i < allPR.size(); i++) {
            if (allPR.get(i).getPrId() == id) {
                getReviewerWithPrId.addAll(allPR.get(i).getReviewerList());
                break;
            }
        }
        return getReviewerWithPrId;

    }

}
