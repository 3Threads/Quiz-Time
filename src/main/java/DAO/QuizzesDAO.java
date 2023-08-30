package DAO;

import java.sql.*;
import java.util.ArrayList;

import Types.Quiz;
import org.apache.commons.dbcp2.BasicDataSource;

public class QuizzesDAO {

    private final BasicDataSource dataSource;


    /**
     * Constructs a QuizzesDAO instance with a provided data source.
     * Initializes the data source for database connectivity.
     *
     * @param dataSource The data source to be used for database connections and operations.
     */
    public QuizzesDAO(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }


    /**
     * Adds a new quiz to the database with the provided information.
     * This method establishes a connection to the database using the provided data source.
     * It inserts a new row into the QUIZZES table with the specified quiz name, description, creator ID, time limit, and categories.
     * If a SQLException occurs during the database operations, the exception is caught, and the stack trace is printed.
     * The database connection is properly closed in the finally block to ensure resource release, even if an exception occurs.
     *
     * @param quizName   The name of the quiz to be added.
     * @param description The description of the quiz.
     * @param creatorID   The ID of the user who is creating the quiz.
     * @param timeLimit   The time limit for completing the quiz.
     * @param categories  The categories associated with the quiz.
     */
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


    /**
     * Retrieves a list of popular quizzes from the database.
     * This method establishes a connection to the database using the provided data source.
     * It queries the QUIZZES table to retrieve quizzes ordered by completion count in descending order.
     * The retrieved quizzes are returned as an ArrayList.
     * If a SQLException occurs during the database operations, the exception is caught, and the stack trace is printed.
     * The database connection is properly closed in the finally block to ensure resource release, even if an exception occurs.
     *
     * @return An ArrayList containing popular Quiz objects based on completion count.
     */
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


    /**
     * Retrieves a list of Quiz objects based on the result of a PreparedStatement query.
     * This method takes a PreparedStatement as input, which is expected to be a query for retrieving quiz data from the database.
     * It executes the query and processes the ResultSet to create Quiz objects based on the retrieved data.
     * The created Quiz objects are added to an ArrayList, which is then returned.
     * If a SQLException occurs during the database operations, the exception is thrown to be caught by the calling method.
     *
     * @param statement The PreparedStatement containing the query for retrieving quiz data.
     * @return An ArrayList containing Quiz objects based on the result of the database query.
     * @throws SQLException If a database access error occurs.
     */
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


    /**
     * Retrieves a list of Quiz objects created within the last 24 hours.
     * This method establishes a database connection using the provided data source.
     * It queries the QUIZZES table to retrieve quizzes created within the last day.
     * The retrieved data is then used to create Quiz objects.
     * The quizzes are added to an ArrayList and returned.
     * If any SQLException occurs during the database operations, the exception is caught, and the stack trace is printed.
     * The database connection is properly closed in the finally block to ensure resource release, even if an exception occurs.
     *
     * @return An ArrayList containing Quiz objects created within the last 24 hours.
     */
    public ArrayList<Quiz> getRecentQuizzes() {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String getQuizzes = "SELECT * FROM QUIZZES " +
                    "ORDER BY CREATION_TIME DESC;";
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


    /**
     * Retrieves a list of Quiz objects created by a specific user.
     * This method establishes a database connection using the provided data source.
     * It queries the QUIZZES table to retrieve quizzes created by the user with the given ID.
     * The retrieved data is then used to create Quiz objects.
     * The quizzes are added to an ArrayList and returned.
     *
     * @param id The ID of the user whose created quizzes are to be retrieved.
     * @return An ArrayList containing Quiz objects created by the specified user.
     */
    public ArrayList<Quiz> getMyCreatedQuizzes(int id) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String getQuizzes = "SELECT * FROM QUIZZES WHERE CREATOR_ID = ? order by CREATION_TIME desc";
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


    /**
     * Retrieves information about a specific quiz from the database.
     * This method establishes a database connection using the provided data source.
     * It queries the QUIZZES table to retrieve information about the quiz with the given ID.
     * The retrieved data is used to create a Quiz object, which is then returned.
     *
     * @param quizID The ID of the quiz for which information is to be retrieved.
     * @return A Quiz object containing information about the specified quiz, or null if no such quiz is found.
     */
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


    /**
     * Marks a specific quiz as completed in the database.
     * This method establishes a database connection using the provided data source.
     * It updates the COMPLETED field of the QUIZZES table by incrementing it for the quiz with the given ID.
     *
     * @param quizId The ID of the quiz to mark as completed.
     */
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


    /**
     * Deletes a specific quiz from the database.
     * This method establishes a database connection using the provided data source.
     * It deletes the entry associated with the given quiz ID from the QUIZZES table.
     *
     * @param quizId The ID of the quiz to be deleted.
     */
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


    /**
     * Retrieves information about a specific quiz based on its name from the database.
     * This method establishes a database connection using the provided data source.
     * It queries the QUIZZES table to retrieve information about the quiz with the given name.
     * The retrieved data is used to create a Quiz object, which is returned.
     * If the quiz with the specified name is not found, null is returned.
     *
     * @param quizName The name of the quiz for which to retrieve information.
     * @return A Quiz object containing information about the specified quiz, or null if not found.
     */
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


    /**
     * Checks if a quiz name is already in use in the database.
     * This method establishes a database connection using the provided data source.
     * It queries the QUIZZES table to check if a quiz with the given name already exists.
     * If a quiz with the specified name is found, it returns false, indicating that the name is in use.
     * If no quiz with the specified name is found, it returns true, indicating that the name is available.
     *
     * @param quizName The name of the quiz to check.
     * @return true if the quiz name is available, false if it is already in use.
     */
    public boolean checkQuizName(String quizName) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String getQuiz = "SELECT * FROM QUIZZES  WHERE QUIZ_NAME = ?";
            PreparedStatement statement = connect.prepareStatement(getQuiz);
            statement.setString(1, quizName);
            ArrayList<Quiz> quizzes = getQuizzes(statement);
            return quizzes.isEmpty();
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


    /**
     * Searches for quizzes in the database that match the given search string.
     * This method establishes a database connection using the provided data source.
     * It queries the QUIZZES table and the USERS table to find quizzes whose names or creators' usernames match the search string.
     * The search string is used for partial matching with both quiz names and creator usernames.
     * The retrieved quizzes are returned as an ArrayList.
     *
     * @param searchString The search string to match against quiz names and creator usernames.
     * @return An ArrayList of Quiz objects that match the search criteria.
     */
    public ArrayList<Quiz> searchQuizzes(String searchString) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String getQuiz = "SELECT * FROM QUIZZES WHERE QUIZ_NAME LIKE ? OR (SELECT USERNAME FROM USERS WHERE ID = CREATOR_ID) LIKE ?;";
            PreparedStatement statement = connect.prepareStatement(getQuiz);
            statement.setString(1, "%" + searchString + "%");
            statement.setString(2, "%" + searchString + "%");
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


    /**
     * Performs a deep search for quizzes in the database based on name, minimum rating, and category.
     * This method establishes a database connection using the provided data source.
     * It queries the QUIZZES and RATINGS tables to find quizzes that match the specified criteria.
     * The search string 'name' is used for partial matching against quiz names.
     * Quizzes are filtered based on their average rating being greater than or equal to 'minRate'.
     * The 'category' string is used for partial matching with quiz categories.
     * The retrieved quizzes that meet the search criteria are returned as an ArrayList.
     *
     * @param name     The search string to match against quiz names.
     * @param minRate  The minimum average rating a quiz must have to be included in the results.
     * @param category The search string to match against quiz categories.
     * @return An ArrayList of Quiz objects that meet the specified search criteria.
     */
    public ArrayList<Quiz> deepSearchQuizzes(String name, int minRate, String category) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String getQuiz = "SELECT * FROM QUIZZES Q WHERE Q.QUIZ_NAME LIKE ? AND (SELECT IFNULL(FLOOR(AVG(RATING)),0) FROM RATINGS R WHERE R.QUIZ_ID = Q.ID) >= ? AND CATEGORIES LIKE ?";
            PreparedStatement statement = connect.prepareStatement(getQuiz);
            statement.setString(1, "%" + name + "%");
            statement.setInt(2, minRate);
            statement.setString(3, "%" + category + "%");
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
