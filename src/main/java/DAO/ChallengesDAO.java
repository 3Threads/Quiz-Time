package DAO;

import Types.Challenge;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.ArrayList;

public class ChallengesDAO {

    private final BasicDataSource dataSource;


    /**
     * Constructor for ChallengesDAO.
     * Initializes the data source used for database connectivity.
     *
     * @param dataSource The BasicDataSource object used for database connections.
     */
    public ChallengesDAO(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }


    /**
     * Retrieves a list of challenges associated with the specified user ID.
     * This method establishes a database connection using the provided data source,
     * and queries the database to retrieve challenges where the given user is the recipient (USER2_ID).
     * The retrieved challenges are processed and converted into Challenge objects,
     * which are then added to an ArrayList that is returned.
     * If any SQLException occurs during the database operations, the exception is caught,
     * and the stack trace is printed. The database connection is properly closed in the finally block
     * to ensure resource release, even if an exception occurs.
     *
     * @param userId The ID of the user for whom challenges are being retrieved.
     * @return An ArrayList of Challenge objects retrieved from the database, or null if an exception occurs.
     */
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


    /**
     * Sends a challenge from one user to another user with a specified quiz ID.
     * This method establishes a database connection using the provided data source,
     * and inserts a new challenge record into the CHALLENGES table with the given sender's ID,
     * recipient's ID, and quiz ID. The challenge details are set as parameters in a prepared statement
     * and executed for insertion. If any SQLException occurs during the database operations,
     * the exception is caught, and the stack trace is printed. The database connection is properly closed
     * in the finally block to ensure resource release, even if an exception occurs.
     *
     * @param fromUserId The ID of the user sending the challenge.
     * @param toUserId   The ID of the user receiving the challenge.
     * @param quizId     The ID of the quiz associated with the challenge.
     */
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


    /**
     * Removes a challenge between two users with a specified quiz ID.
     * This method establishes a database connection using the provided data source,
     * and deletes the challenge record from the CHALLENGES table where the sender's ID,
     * recipient's ID, and quiz ID match the provided values. The deletion is performed
     * using a prepared statement with appropriate parameters. If any SQLException occurs
     * during the database operations, the exception is caught, and the stack trace is printed.
     * The database connection is properly closed in the finally block to ensure resource release,
     * even if an exception occurs.
     *
     * @param fromUserId The ID of the user who sent the challenge.
     * @param toUserId   The ID of the user who received the challenge.
     * @param quizId     The ID of the quiz associated with the challenge.
     */
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


    /**
     * Checks if a challenge already exists between two users with a specified quiz ID.
     * This method establishes a database connection using the provided data source,
     * and queries the CHALLENGES table to check if a challenge record exists where the sender's ID,
     * recipient's ID, and quiz ID match the provided values. The query is executed using a prepared statement,
     * and the result set is checked to determine if any matching records are found.
     * If any SQLException occurs during the database operations, the exception is caught,
     * and the stack trace is printed. The database connection is properly closed in the finally block
     * to ensure resource release, even if an exception occurs.
     *
     * @param byUser        The ID of the user who initiated the challenge.
     * @param challengedUser The ID of the user who received the challenge.
     * @param quizId         The ID of the quiz associated with the challenge.
     * @return True if a challenge record exists with the provided details, otherwise false.
     */
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
