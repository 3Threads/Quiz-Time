package DAO;

import Types.Challenge;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.ArrayList;

public class ChallengesDAO {
    private final BasicDataSource dataSource;

    public ChallengesDAO(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public ArrayList<Challenge> getChallenges(int userId) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            ArrayList<Challenge> challenges = new ArrayList<>();
            String foundChallenges = "SELECT USER1_ID, QUIZ_ID FROM CHALLENGES WHERE USER2_ID = ?";
            PreparedStatement statement = connect.prepareStatement(foundChallenges);
            statement.setInt(1, userId);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                int id = result.getInt("USER1_ID");
                int quizId = result.getInt("QUIZ_ID");
                Challenge ch = new Challenge(id, quizId);
                challenges.add(ch);
            }
            return challenges;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connect != null) {
                try {
                    connect.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public void sendChallenge(int fromUserId, int toUserId, int quizId) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String sendChallenge = "INSERT INTO CHALLENGES (USER1_ID, USER2_ID, QUIZ_ID) VALUES(?, ?, ?);";
            PreparedStatement statement = connect.prepareStatement(sendChallenge);
            statement.setInt(1, fromUserId);
            statement.setInt(2, toUserId);
            statement.setInt(3, quizId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connect != null) {
                try {
                    connect.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void removeChallenge(int fromUserId, int toUserId, int quizId) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String sendChallenge = "DELETE FROM CHALLENGES WHERE USER1_ID = ? AND USER2_ID = ? AND QUIZ_ID = ?;";
            PreparedStatement statement = connect.prepareStatement(sendChallenge);
            statement.setInt(1, fromUserId);
            statement.setInt(2, toUserId);
            statement.setInt(3, quizId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connect != null) {
                try {
                    connect.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean alreadyChallenged(int byUser, int challengedUser, int quizId) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String sendChallenge = "SELECT * FROM CHALLENGES WHERE USER1_ID = ? AND USER2_ID = ? AND QUIZ_ID = ?;";
            PreparedStatement statement = connect.prepareStatement(sendChallenge);
            statement.setInt(1, byUser);
            statement.setInt(2, challengedUser);
            statement.setInt(3, quizId);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connect != null) {
                try {
                    connect.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

}
