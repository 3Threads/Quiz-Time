package DAO;

import java.sql.*;
import java.util.ArrayList;

import Types.Quiz;
import org.apache.commons.dbcp2.BasicDataSource;

public class QuizzesDAO {
    private final String tableName = "QUIZZES";
    private final BasicDataSource dataSource;

    public QuizzesDAO(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void addQuiz(String quizName, String description, int creatorID, String questions) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String str = "INSERT INTO " + tableName + " VALUES(default,?,?,0,default,?,?)";
            PreparedStatement preparedStatement = connect.prepareStatement(str);
            preparedStatement.setString(1, quizName);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, String.valueOf(creatorID));
            preparedStatement.setString(4, questions);
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

    public ArrayList<Quiz> getPopularQuizzes(int limit) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            ArrayList<Quiz> quizzes = new ArrayList<>();
            Statement stmt = connect.createStatement();
            String getQuizzes = "SELECT * FROM " + tableName + " ORDER BY COMPLETED DESC LIMIT " + limit + ";";
            return getQuizzes(quizzes, stmt, getQuizzes);
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

    private ArrayList<Quiz> getQuizzes(ArrayList<Quiz> quizzes, Statement stmt, String getQuizzes) throws SQLException {
        ResultSet result = stmt.executeQuery(getQuizzes);
        while (result.next()) {
            int quizId = result.getInt("ID");
            String quizName = result.getString("QUIZ_NAME");
            String description = result.getString("DESCRIPTION");
            int creatorID = result.getInt("CREATOR_ID");
            Date creatingDate = result.getDate("CREATION_TIME");
            int completed = result.getInt("COMPLETED");
            String questions = result.getString("QUESTIONS");
            Quiz quiz = new Quiz(quizId, quizName, description, creatingDate, creatorID, questions, completed);
            quizzes.add(quiz);
        }
        return quizzes;
    }

    public ArrayList<Quiz> getLastDayQuizzes() {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            ArrayList<Quiz> quizzes = new ArrayList<>();
            Statement stmt = connect.createStatement();
            String getQuizzes = "SELECT * FROM " + tableName + " WHERE CREATION_TIME > DATE_SUB(CURDATE(),INTERVAL 1 DAY)" +
                    "ORDER BY CREATION_TIME;";
            return getQuizzes(quizzes, stmt, getQuizzes);
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

    public Quiz getQuizInfo(int quizID) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            Statement stmt = connect.createStatement();
            String getQuiz = "SELECT * FROM QUIZZES WHERE ID = " + quizID + ";";
            ArrayList<Quiz> quizzes = new ArrayList<>();
            getQuizzes(quizzes, stmt, getQuiz);
            if (quizzes.isEmpty()) return null;
            else return quizzes.get(0);
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

    public void completeQuiz(int quizId) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            Statement stmt = connect.createStatement();
            String updateCompletedNum = "UPDATE QUIZZES SET COMPLETED = COMPLETED + 1 WHERE ID = " + quizId + ";";
            stmt.execute(updateCompletedNum);
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

    public void deleteQuiz(int quizId) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            Statement stmt = connect.createStatement();
            String deleteQuiz = "DELETE FROM QUIZZES WHERE ID = " + quizId + ";";
            stmt.execute(deleteQuiz);
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

}
