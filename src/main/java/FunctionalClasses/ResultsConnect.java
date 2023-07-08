package FunctionalClasses;

import Types.Result;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ResultsConnect extends SQLConnect {
    private final String tableName;

    public ResultsConnect(String tableName) {
        super();
        this.tableName = tableName;
    }

    public ArrayList<Result> getUserResults(int userId) throws SQLException {
        ArrayList<Result> results = new ArrayList<>();
        Statement stmt = connect.createStatement();
        String getResults = "SELECT * FROM " + tableName + " WHERE USERID = " + userId + ";";
        return getResults(results, stmt, getResults);
    }

    private ArrayList<Result> getResults(ArrayList<Result> results, Statement stmt, String getResults) throws SQLException {
        ResultSet result = stmt.executeQuery(getResults);
        while (result.next()) {
            Result rs = new Result(result.getInt("USERID"), result.getInt("QUIZID"), result.getInt("SCORE"),
                    result.getTime("SPENTTIME"), result.getDate("WRITETIME"));
            results.add(rs);
        }
        return results;
    }

    public ArrayList<Result> getQuizResults(int quizId) throws SQLException {
        ArrayList<Result> results = new ArrayList<>();
        Statement stmt = connect.createStatement();
        String getResults = "SELECT * FROM " + tableName + " WHERE QUIZID = " + quizId + " ORDER BY SCORE DESC;";
        return getResults(results, stmt, getResults);
    }

    public ArrayList<Result> getUserResultsOnQuiz(int userId, int quizId) throws SQLException {
        ArrayList<Result> results = new ArrayList<>();
        Statement stmt = connect.createStatement();
        String getResults = "SELECT * FROM " + tableName + " WHERE USERID = " + userId + " AND QUIZID = " +
                quizId + " ORDER BY SCORE DESC;";
        return getResults(results, stmt, getResults);
    }
}
