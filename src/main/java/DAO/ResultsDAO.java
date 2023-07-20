package DAO;

import Types.Result;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.ArrayList;

public class ResultsDAO {
    private final String tableName = "COMPLETED_QUIZZES";
    private final BasicDataSource dataSource;

    public ResultsDAO(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void addResult(int userId, int quizId, int score, Time spentTime) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String addResult = "INSERT INTO " + tableName + " VALUES(default,?,?,?,?,default)";
            PreparedStatement preparedStatement = connect.prepareStatement(addResult);
            preparedStatement.setString(1, String.valueOf(userId));
            preparedStatement.setString(2, String.valueOf(quizId));
            preparedStatement.setString(3, String.valueOf(score));
            preparedStatement.setString(4, String.valueOf(spentTime));
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

    public ArrayList<Result> getUserResults(int userId) {
        Connection connect = null;
        try {
            ArrayList<Result> results = new ArrayList<>();
            connect = dataSource.getConnection();
            Statement stmt = connect.createStatement();
            String getResults = "SELECT * FROM " + tableName + " WHERE USER_ID = " + userId + " ORDER BY SCORE DESC;";

            return getResults(results, stmt, getResults);
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

    private ArrayList<Result> getResults(ArrayList<Result> results, Statement stmt, String getResults) throws SQLException {

        ResultSet result = stmt.executeQuery(getResults);
        while (result.next()) {
            Result rs = new Result(result.getInt("USER_ID"), result.getInt("QUIZ_ID"), result.getInt("SCORE"),
                    result.getTime("SPENT_TIME"), result.getDate("WRITE_TIME"));
            results.add(rs);
        }
        return results;

    }

    public ArrayList<Result> getQuizResults(int quizId) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            ArrayList<Result> results = new ArrayList<>();
            Statement stmt = connect.createStatement();
            String getResults = "SELECT * FROM " + tableName + " WHERE QUIZ_ID = " + quizId + " ORDER BY SCORE DESC;";
            return getResults(results, stmt, getResults);
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

    public ArrayList<Result> getUserResultsOnQuiz(int userId, int quizId) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            ArrayList<Result> results = new ArrayList<>();
            Statement stmt = connect.createStatement();
            String getResults = "SELECT * FROM " + tableName + " WHERE USER_ID = " + userId + " AND QUIZ_ID = " +
                    quizId + " ORDER BY SCORE DESC;";
            return getResults(results, stmt, getResults);
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
}
