package DAO;

import Types.Result;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.ArrayList;

public class ResultsDAO {
    private final BasicDataSource dataSource;

    public ResultsDAO(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void addResult(int userId, int quizId, int score, Time spentTime) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String addResult = "INSERT INTO COMPLETED_QUIZZES VALUES(default,?,?,?,?,default)";
            PreparedStatement preparedStatement = connect.prepareStatement(addResult);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, quizId);
            preparedStatement.setInt(3, score);
            preparedStatement.setTime(4, spentTime);
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
            connect = dataSource.getConnection();
            String getResults = "SELECT * FROM COMPLETED_QUIZZES WHERE USER_ID = ? ORDER BY SCORE DESC;";
            PreparedStatement statement = connect.prepareStatement(getResults);
            statement.setInt(1, userId);

            return getResults(statement);
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

    private ArrayList<Result> getResults(PreparedStatement statement) throws SQLException {
        ArrayList<Result> results = new ArrayList<>();
        ResultSet result = statement.executeQuery();
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
            String getResults = "SELECT * FROM COMPLETED_QUIZZES WHERE QUIZ_ID = ? ORDER BY SCORE DESC, SPENT_TIME;";
            PreparedStatement statement = connect.prepareStatement(getResults);
            statement.setInt(1, quizId);
            return getResults(statement);
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

    public ArrayList<Result> getUserFriendsResultOnQuiz(int userId, int quizId) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String getResults = "SELECT * FROM completed_quizzes WHERE QUIZ_ID=? AND " +
                    "USER_ID IN ((SELECT USER2_ID FROM friends where (USER1_ID=? AND ACCEPTED=1) ) " +
                    "union (SELECT USER1_ID FROM friends  where (USER2_ID=? AND ACCEPTED=1) )) " +
                    "ORDER BY SCORE DESC, SPENT_TIME;";
            PreparedStatement statement = connect.prepareStatement(getResults);
            statement.setInt(1, quizId);
            statement.setInt(2, userId);
            statement.setInt(3, userId);
            return getResults(statement);
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
            String getResults = "SELECT * FROM COMPLETED_QUIZZES WHERE USER_ID = ? AND QUIZ_ID = ? ORDER BY SCORE DESC, SPENT_TIME;";
            PreparedStatement statement = connect.prepareStatement(getResults);
            statement.setInt(1, userId);
            statement.setInt(2, quizId);
            return getResults(statement);
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
