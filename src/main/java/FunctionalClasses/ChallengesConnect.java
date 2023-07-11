package FunctionalClasses;

import Types.Challange;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ChallengesConnect extends SQLConnect {
    private final String tableName = "CHALLENGES";

    public ChallengesConnect(boolean isTesting) {
        super(isTesting);
    }

    public ArrayList<Challange> getChallenges(int userId) throws SQLException {
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
    }

    public void sendChallenge(int fromUserId, int toUserId, int quizId) throws SQLException {
        String sendChallenge = "INSERT INTO " + tableName + "(USER1_ID, USER2_ID, QUIZ_ID) VALUES(?, ?, ?);";
        PreparedStatement preparedStatement = connect.prepareStatement(sendChallenge);
        preparedStatement.setString(1, String.valueOf(fromUserId));
        preparedStatement.setString(2, String.valueOf(toUserId));
        preparedStatement.setString(3, String.valueOf(quizId));
        preparedStatement.executeUpdate();
    }
}
