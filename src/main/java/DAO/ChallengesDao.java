package DAO;

import Types.Challange;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.ArrayList;

public class ChallengesDao {
    private final String tableName = "CHALLENGES";
    private final BasicDataSource dataSource;

    public ChallengesDao(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public ArrayList<Challange> getChallenges(int userId) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            ArrayList<Challange> challenges = new ArrayList<>();
            Statement stmt = connect.createStatement();
            String foundChallenges = "SELECT USER1_ID, QUIZ_ID FROM " + tableName + " WHERE USER2_ID = " + userId + ";";
            ResultSet result = stmt.executeQuery(foundChallenges);
            while (result.next()) {
                int id = result.getInt("USER1_ID");
                int quizId = result.getInt("QUIZ_ID");
                Challange ch = new Challange(id, quizId);
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
            String sendChallenge = "INSERT INTO " + tableName + "(USER1_ID, USER2_ID, QUIZ_ID) VALUES(?, ?, ?);";
            PreparedStatement preparedStatement = connect.prepareStatement(sendChallenge);
            preparedStatement.setString(1, String.valueOf(fromUserId));
            preparedStatement.setString(2, String.valueOf(toUserId));
            preparedStatement.setString(3, String.valueOf(quizId));
            preparedStatement.executeUpdate();
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
}
