package DAO;

import Types.Result;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.ArrayList;

public class ResultsDAO {

    private final BasicDataSource dataSource;


    /**
     * Constructs a new ResultsDAO instance with the provided data source.
     * This constructor initializes the data source used to manage database connections for result-related operations.
     *
     * @param dataSource The data source used to manage database connections.
     */
    public ResultsDAO(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }


    /**
     * Adds a completed quiz result to the database.
     * This method establishes a database connection using the provided data source.
     * It inserts a new row into the COMPLETED_QUIZZES table with the user ID, quiz ID, score, spent time, and completion timestamp.
     * If any SQLException occurs during the database operations, the exception is caught, and the stack trace is printed.
     * The database connection is properly closed in the finally block to ensure resource release, even if an exception occurs.
     *
     * @param userId    The ID of the user who completed the quiz.
     * @param quizId    The ID of the quiz that was completed.
     * @param score     The score achieved by the user in the completed quiz.
     * @param spentTime The time spent by the user to complete the quiz, in milliseconds.
     */
    public void addResult(int userId, int quizId, int score, long spentTime) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String addResult = "INSERT INTO COMPLETED_QUIZZES VALUES(default,?,?,?,?,default)";
            PreparedStatement preparedStatement = connect.prepareStatement(addResult);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, quizId);
            preparedStatement.setInt(3, score);
            preparedStatement.setLong(4, spentTime);
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


    /**
     * Retrieves a list of completed quiz results for a specific user from the database.
     * This method establishes a database connection using the provided data source.
     * It queries the COMPLETED_QUIZZES table to retrieve results with the given user ID.
     * The retrieved data is used to create Result objects representing the completed quiz results.
     * The results are added to an ArrayList and returned, sorted by score in descending order.
     * If any SQLException occurs during the database operations, the exception is caught, and the stack trace is printed.
     * The database connection is properly closed in the finally block to ensure resource release, even if an exception occurs.
     *
     * @param userId The ID of the user for which to retrieve completed quiz results.
     * @return An ArrayList containing Result objects representing completed quiz results for the specified user.
     */

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


    /**
     * Retrieves a list of completed quiz results based on the provided PreparedStatement.
     * This method executes the given PreparedStatement to retrieve results from the database.
     * The retrieved data is used to create Result objects representing the completed quiz results.
     * The results are added to an ArrayList and returned.
     * If any SQLException occurs during the database operations, the exception is thrown up the stack.
     *
     * @param statement The PreparedStatement to execute for retrieving completed quiz results.
     * @return An ArrayList containing Result objects representing completed quiz results.
     * @throws SQLException If a database access error occurs while executing the PreparedStatement.
     */
    private ArrayList<Result> getResults(PreparedStatement statement) throws SQLException {
        ArrayList<Result> results = new ArrayList<>();
        ResultSet result = statement.executeQuery();
        while (result.next()) {
            Result rs = new Result(result.getInt("USER_ID"), result.getInt("QUIZ_ID"), result.getInt("SCORE"),
                    result.getLong("SPENT_TIME"), result.getDate("WRITE_TIME"));
            results.add(rs);
        }
        return results;
    }


    /**
     * Retrieves a list of completed quiz results for a specific quiz based on the provided quiz ID.
     * This method establishes a database connection using the provided data source.
     * It executes a PreparedStatement to retrieve completed quiz results with the given quiz ID from the database.
     * The retrieved data is used to create Result objects representing the completed quiz results.
     * The results are added to an ArrayList and returned, ordered by score and then spent time.
     * If any SQLException occurs during the database operations, the exception is caught, and the stack trace is printed.
     * The database connection is properly closed in the finally block to ensure resource release, even if an exception occurs.
     *
     * @param quizId The ID of the quiz for which to retrieve completed results.
     * @return An ArrayList containing Result objects representing completed quiz results for the specified quiz.
     */
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


    /**
     * Retrieves a list of completed quiz results for a specific quiz, where the results are from friends of a given user.
     * This method establishes a database connection using the provided data source.
     * It executes a PreparedStatement to retrieve completed quiz results with the given quiz ID and user ID from the database.
     * The retrieved data is used to create Result objects representing the completed quiz results.
     * The results are added to an ArrayList and returned, ordered by score and then spent time.
     * The results are filtered to include only friends of the user, based on the FRIENDS table and ACCEPTED status.
     * If any SQLException occurs during the database operations, the exception is caught, and the stack trace is printed.
     * The database connection is properly closed in the finally block to ensure resource release, even if an exception occurs.
     *
     * @param userId The ID of the user whose friends' results are to be retrieved.
     * @param quizId The ID of the quiz for which to retrieve completed results.
     * @return An ArrayList containing Result objects representing completed quiz results for the specified quiz and user's friends.
     */
    public ArrayList<Result> getUserFriendsResultOnQuiz(int userId, int quizId) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String getResults = "SELECT * FROM COMPLETED_QUIZZES WHERE QUIZ_ID=? AND " +
                    "USER_ID IN ((SELECT USER2_ID FROM FRIENDS where (USER1_ID=? AND ACCEPTED=1) ) " +
                    "union (SELECT USER1_ID FROM FRIENDS  where (USER2_ID=? AND ACCEPTED=1) )) " +
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


    /**
     * Retrieves a list of completed quiz results for a specific quiz and user from the database.
     * This method establishes a database connection using the provided data source.
     * It executes a PreparedStatement to retrieve completed quiz results with the given quiz ID and user ID from the database.
     * The retrieved data is used to create Result objects representing the completed quiz results.
     * The results are added to an ArrayList and returned, ordered by score and then spent time.
     * If any SQLException occurs during the database operations, the exception is caught, and the stack trace is printed.
     * The database connection is properly closed in the finally block to ensure resource release, even if an exception occurs.
     *
     * @param userId The ID of the user whose results are to be retrieved.
     * @param quizId The ID of the quiz for which to retrieve completed results.
     * @return An ArrayList containing Result objects representing completed quiz results for the specified user and quiz.
     */
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
