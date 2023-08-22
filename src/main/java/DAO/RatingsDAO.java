package DAO;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Types.*;

import static java.lang.Math.round;

public class RatingsDAO {

    private final BasicDataSource dataSource;


    /**
     * Constructs a RatingsDAO instance with the provided data source for database access.
     *
     * @param dataSource The data source used for establishing database connections.
     */
    public RatingsDAO(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }


    /**
     * Retrieves the average rating of a quiz from the database.
     * This method establishes a database connection using the provided data source.
     * It queries the RATINGS table to retrieve ratings for the specified quiz ID.
     * The retrieved ratings are used to calculate the average rating of the quiz.
     * The calculated average rating is rounded to the nearest integer and returned.
     * If no ratings are found for the quiz, the method returns 0.
     * If any SQLException occurs during the database operations, the exception is caught, and the stack trace is printed.
     * The database connection is properly closed in the finally block to ensure resource release, even if an exception occurs.
     *
     * @param quizId The ID of the quiz for which to retrieve the average rating.
     * @return The average rating of the specified quiz, rounded to the nearest integer, or 0 if no ratings are found.
     */
    public int getAvgRatingOfQuiz(int quizId) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String str = "SELECT * FROM RATINGS WHERE QUIZ_ID=?";
            PreparedStatement statement = connect.prepareStatement(str);
            statement.setInt(1, quizId);
            ResultSet resultSet = statement.executeQuery();
            int num = 0;
            int sum = 0;
            while (resultSet.next()) {
                num++;
                sum += resultSet.getInt("RATING");
            }
            if (num != 0) {
                double avg = (double) sum / num;
                return (int) round(avg);
            }
            return 0;
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
        return 0;
    }


    /**
     * Retrieves a list of ratings for a specific quiz from the database.
     * This method establishes a database connection using the provided data source.
     * It queries the RATINGS table to retrieve ratings associated with the specified quiz ID.
     * The method provides an option to order the ratings by either newest or oldest rated date.
     * The retrieved data is used to create Rating objects, which are added to an ArrayList.
     * The ArrayList of Rating objects is then returned.
     * If any SQLException occurs during the database operations, the exception is caught, and the stack trace is printed.
     * The database connection is properly closed in the finally block to ensure resource release, even if an exception occurs.
     *
     * @param quizId The ID of the quiz for which to retrieve the ratings.
     * @param lst    The ordering option for the ratings (can be "newest" or "oldest").
     * @return An ArrayList containing Rating objects associated with the specified quiz and ordered according to the chosen option.
     */
    public ArrayList<Rating> getQuizRatings(int quizId, String lst) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String str;
            if (lst.equals("newest")) {
                str = "SELECT * FROM RATINGS WHERE QUIZ_ID=? ORDER BY RATED_DATE DESC";
            } else str = "SELECT * FROM RATINGS WHERE QUIZ_ID=? ORDER BY RATED_DATE";
            PreparedStatement statement = connect.prepareStatement(str);
            statement.setInt(1, quizId);
            ResultSet resultSet = statement.executeQuery();
            ArrayList<Rating> ratings = new ArrayList<>();
            while (resultSet.next()) {
                ratings.add(new Rating(resultSet.getInt("USER_ID"), resultSet.getInt("RATING"),
                        resultSet.getString("COMMENT"), resultSet.getDate("RATED_DATE")));
            }
            return ratings;
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
     * Adds a rating and comment to a specific quiz in the database.
     * This method establishes a database connection using the provided data source.
     * It inserts a new record into the RATINGS table with the provided user ID, quiz ID, rating, and comment.
     * The "RATED_DATE" column is automatically set to the current date and time.
     * If any SQLException occurs during the database operations, the exception is caught, and the stack trace is printed.
     * The database connection is properly closed in the finally block to ensure resource release, even if an exception occurs.
     *
     * @param userId   The ID of the user submitting the rating.
     * @param quizId   The ID of the quiz to which the rating is added.
     * @param rating   The rating value assigned to the quiz.
     * @param comment  The comment provided with the rating.
     */
    public void addRatingToQuiz(int userId, int quizId, int rating, String comment) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String str = "INSERT INTO RATINGS VALUES(default,?,?,?,?,default)";
            PreparedStatement statement = connect.prepareStatement(str);
            statement.setInt(1, userId);
            statement.setInt(2, quizId);
            statement.setInt(3, rating);
            statement.setString(4, comment);
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
     * Deletes a user's rating for a specific quiz from the database.
     * This method establishes a database connection using the provided data source.
     * It deletes the rating record associated with the provided user ID and quiz ID from the RATINGS table.
     * If any SQLException occurs during the database operations, the exception is caught, and the stack trace is printed.
     * The database connection is properly closed in the finally block to ensure resource release, even if an exception occurs.
     *
     * @param userId The ID of the user whose rating is to be deleted.
     * @param quizId The ID of the quiz for which the rating is to be deleted.
     */
    public void deleteRating(int userId, int quizId) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String str = "DELETE FROM RATINGS WHERE USER_ID=? AND QUIZ_ID=?";
            PreparedStatement statement = connect.prepareStatement(str);
            statement.setInt(1, userId);
            statement.setInt(2, quizId);
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
     * Checks whether a user has already rated a specific quiz in the database.
     * This method establishes a database connection using the provided data source.
     * It queries the RATINGS table to check if a rating record exists for the provided user ID and quiz ID.
     * If a matching record is found, the method returns true; otherwise, it returns false.
     * If any SQLException occurs during the database operations, the exception is caught, and the stack trace is printed.
     * The database connection is properly closed in the finally block to ensure resource release, even if an exception occurs.
     *
     * @param userId The ID of the user whose rating presence is to be checked.
     * @param quizId The ID of the quiz for which the rating presence is to be checked.
     * @return True if the user has already rated the quiz, false otherwise.
     */
    public Boolean haveAlreadyRated(int userId, int quizId) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String str = "SELECT * FROM RATINGS WHERE USER_ID=? AND QUIZ_ID=?";
            PreparedStatement statement = connect.prepareStatement(str);
            statement.setInt(1, userId);
            statement.setInt(2, quizId);
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
