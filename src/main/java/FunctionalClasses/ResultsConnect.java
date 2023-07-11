package FunctionalClasses;

import Types.Result;

import java.sql.*;
import java.util.ArrayList;

public class ResultsConnect extends SQLConnect {
    private final String tableName = "COMPLETED_QUIZZES";

    public ResultsConnect(boolean isTesting) {
        super(isTesting);
    }

    public void addResult(int userId, int quizId, int score, Time spentTime) throws SQLException {
        String addResult = "INSERT INTO "+tableName+ " VALUES(default,?,?,?,?,default)";
        PreparedStatement preparedStatement = connect.prepareStatement(addResult);
        preparedStatement.setString(1, String.valueOf(userId));
        preparedStatement.setString(2, String.valueOf(quizId));
        preparedStatement.setString(3, String.valueOf(score));
        preparedStatement.setString(4, String.valueOf(spentTime));
        preparedStatement.executeUpdate();
    }
    public ArrayList<Result> getUserResults(int userId) throws SQLException {
        ArrayList<Result> results = new ArrayList<>();
        Statement stmt = connect.createStatement();
        String getResults = "SELECT * FROM " + tableName + " WHERE USER_ID = " + userId + " ORDER BY SCORE DESC;";
        return getResults(results, stmt, getResults);
    }

    private ArrayList<Result> getResults(ArrayList<Result> results, Statement stmt, String getResults) throws SQLException {
        ResultSet result = stmt.executeQuery(getResults);
        while (result.next()) {
            Result rs = new Result(result.getInt("USER_ID"), result.getInt("QUIZ_ID"), result.getInt("SCORE"),
                    result.getTime("SPENT_TIME"), result.getDate("WRITE_TIME"));
            results.add(rs);
        }
        return results;
    }

    public ArrayList<Result> getQuizResults(int quizId) throws SQLException {
        ArrayList<Result> results = new ArrayList<>();
        Statement stmt = connect.createStatement();
        String getResults = "SELECT * FROM " + tableName + " WHERE QUIZ_ID = " + quizId + " ORDER BY SCORE DESC;";
        return getResults(results, stmt, getResults);
    }

    public ArrayList<Result> getUserResultsOnQuiz(int userId, int quizId) throws SQLException {
        ArrayList<Result> results = new ArrayList<>();
        Statement stmt = connect.createStatement();
        String getResults = "SELECT * FROM " + tableName + " WHERE USER_ID = " + userId + " AND QUIZ_ID = " +
                quizId + " ORDER BY SCORE DESC;";
        return getResults(results, stmt, getResults);
    }
}
