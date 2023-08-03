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
    public RatingsDAO(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }

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
            while(resultSet.next()) {
                num++;
                sum += resultSet.getInt("RATING");
            }
            double avg = sum / num;
            if(num != 0) return (int) round(avg);
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
    public ArrayList<Rating> getQuizRatings(int quizId) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String str = "SELECT * FROM RATINGS WHERE QUIZ_ID=? ORDER BY RATED_DATE DESC";
            PreparedStatement statement = connect.prepareStatement(str);
            statement.setInt(1, quizId);
            ResultSet resultSet = statement.executeQuery();
            ArrayList<Rating> ratings = new ArrayList<>();
            while(resultSet.next()) {
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
    public Boolean haveAlreadyRated(int userId, int quizId) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String str = "SELECT * FROM RATINGS WHERE USER_ID=? AND QUIZ_ID=?";
            PreparedStatement statement = connect.prepareStatement(str);
            statement.setInt(1, userId);
            statement.setInt(2, quizId);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) return true;
            return false;
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
