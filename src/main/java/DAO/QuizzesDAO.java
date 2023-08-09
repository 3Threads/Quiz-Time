package DAO;

import java.sql.*;
import java.util.ArrayList;

import Types.Quiz;
import org.apache.commons.dbcp2.BasicDataSource;

public class QuizzesDAO {

    private final BasicDataSource dataSource;

    public QuizzesDAO(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void addQuiz(String quizName, String description, int creatorID, Time timeLimit, String categories) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String str = "INSERT INTO QUIZZES VALUES(default,?,?,0,default,?,?,?)";
            PreparedStatement statement = connect.prepareStatement(str);
            statement.setString(1, quizName);
            statement.setString(2, description);
            statement.setInt(3, creatorID);
            statement.setTime(4, timeLimit);
            statement.setString(5, categories);
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

    public ArrayList<Quiz> getPopularQuizzes() {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String getQuizzes = "SELECT * FROM QUIZZES ORDER BY COMPLETED DESC;";
            PreparedStatement statement = connect.prepareStatement(getQuizzes);
            return getQuizzes(statement);
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

    private ArrayList<Quiz> getQuizzes(PreparedStatement statement) throws SQLException {
        ArrayList<Quiz> quizzes = new ArrayList<>();
        ResultSet result = statement.executeQuery();
        while (result.next()) {
            int quizId = result.getInt("ID");
            String quizName = result.getString("QUIZ_NAME");
            String description = result.getString("DESCRIPTION");
            int creatorID = result.getInt("CREATOR_ID");
            Date creatingDate = result.getDate("CREATION_TIME");
            int completed = result.getInt("COMPLETED");
            Time timeLimit = result.getTime("TIME_LIMIT");
            String categories = result.getString("CATEGORIES");
            Quiz quiz = new Quiz(timeLimit, quizId, quizName, description, creatingDate, creatorID, completed, categories);
            quizzes.add(quiz);
        }
        return quizzes;
    }

    public ArrayList<Quiz> getLastDayQuizzes() {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String getQuizzes = "SELECT * FROM QUIZZES WHERE CREATION_TIME > DATE_SUB(CURDATE(),INTERVAL 1 DAY)" +
                    "ORDER BY CREATION_TIME;";
            PreparedStatement statement = connect.prepareStatement(getQuizzes);
            return getQuizzes(statement);
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

    public ArrayList<Quiz> getMyCreatedQuizzes(int id){
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String getQuizzes = "SELECT * FROM QUIZZES WHERE CREATOR_ID = ?";
            PreparedStatement statement = connect.prepareStatement(getQuizzes);
            statement.setInt(1, id);
            return getQuizzes(statement);
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
            String getQuiz = "SELECT * FROM QUIZZES WHERE ID = ?;";
            PreparedStatement statement = connect.prepareStatement(getQuiz);
            statement.setInt(1, quizID);
            ArrayList<Quiz> quizzes = getQuizzes(statement);
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
            String updateCompletedNum = "UPDATE QUIZZES SET COMPLETED = COMPLETED + 1 WHERE ID = ?;";
            PreparedStatement statement = connect.prepareStatement(updateCompletedNum);
            statement.setInt(1, quizId);
            statement.execute();
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
            String deleteQuiz = "DELETE FROM QUIZZES WHERE ID = ?;";
            PreparedStatement statement = connect.prepareStatement(deleteQuiz);
            statement.setInt(1, quizId);
            statement.execute();
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

    public Quiz getQuizByName(String quizName) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String getQuiz = "SELECT * FROM QUIZZES WHERE QUIZ_NAME = ?;";
            PreparedStatement statement = connect.prepareStatement(getQuiz);
            statement.setString(1, quizName);
            ArrayList<Quiz> quizzes = getQuizzes(statement);
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
    public boolean checkQuizName(String quizName) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String getQuiz = "SELECT * FROM QUIZZES  WHERE QUIZ_NAME = ?";
            PreparedStatement statement = connect.prepareStatement(getQuiz);
            statement.setString(1, quizName);
            ArrayList<Quiz> quizzes = getQuizzes(statement);
            if (quizzes.isEmpty()) return true;
            else return false;
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
    public ArrayList<Quiz> searchQuizzes(String searchString) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String getQuiz = "SELECT * FROM QUIZZES WHERE QUIZ_NAME LIKE ? OR (SELECT USERNAME FROM USERS WHERE ID = CREATOR_ID) LIKE ?;";
            PreparedStatement statement = connect.prepareStatement(getQuiz);
            statement.setString(1, "%"+searchString+"%");
            statement.setString(2, "%"+searchString+"%");
            return getQuizzes(statement);
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
    public ArrayList<Quiz> deepSearchQuizzes(String name, int minRate, String category) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String getQuiz = "SELECT * FROM QUIZZES Q WHERE Q.QUIZ_NAME LIKE ? AND (SELECT IFNULL(FLOOR(AVG(RATING)),0) FROM RATINGS R WHERE R.QUIZ_ID = Q.ID) >= ? AND CATEGORIES LIKE ?";
            PreparedStatement statement = connect.prepareStatement(getQuiz);
            statement.setString(1, "%"+name+"%");
            statement.setInt(2, minRate);
            statement.setString(3, "%"+category+"%");
            return getQuizzes(statement);
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
