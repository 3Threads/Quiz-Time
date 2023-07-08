package FunctionalClasses;

import Types.Challange;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ChallangesConnect extends SQLConnect {
    private final String tableName;

    public ChallangesConnect(String tableName) {
        super();
        this.tableName = tableName;
    }

    public ArrayList<Challange> getChallenges(int userId) throws SQLException {
        ArrayList<Challange> challenges = new ArrayList<>();
        Statement stmt = connect.createStatement();
        String foundChallanges = "SELECT USER1ID, QUIZID FROM " + tableName + " WHERE USER2ID = " + userId + ";";
        ResultSet result = stmt.executeQuery(foundChallanges);
        while (result.next()) {
            int id = result.getInt("USER1ID");
            int quizId = result.getInt("QUIZID");
            Challange ch = new Challange(id, quizId);
            challenges.add(ch);
        }
        return challenges;
    }

    public void sendChallenge(int fromUserId, int toUserId, int quizId) throws SQLException {
        String sendChall = "INSERT INTO " + tableName + " VALUES(?, ?, ?);";
        PreparedStatement preparedStatement = connect.prepareStatement(sendChall);
        preparedStatement.setString(1, String.valueOf(fromUserId));
        preparedStatement.setString(2, String.valueOf(toUserId));
        preparedStatement.setString(3, String.valueOf(quizId));
        preparedStatement.executeUpdate();
    }
}
