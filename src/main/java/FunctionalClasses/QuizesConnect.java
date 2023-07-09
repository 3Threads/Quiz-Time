package FunctionalClasses;

import java.sql.*;
import java.util.ArrayList;

import Types.Quiz;

public class QuizesConnect extends SQLConnect {
    private final String tableName = "QUIZZES";

    public QuizesConnect(boolean isTesting) {
        super(isTesting);
    }

    public void addQuiz(String quizName, String description, int creatorID, Date creatingDate, String questions) throws SQLException {
        Statement stmt = connect.createStatement();
        String str = "INSERT INTO " + tableName + "VALUES(?,?,?,?,?,?)";
        PreparedStatement preparedStatement = connect.prepareStatement(str);
        preparedStatement.setString(1, quizName);
        preparedStatement.setString(2, description);
        preparedStatement.setString(3, "0");
        preparedStatement.setString(4, String.valueOf(creatingDate));
        preparedStatement.setString(5, String.valueOf(creatorID));
        preparedStatement.setString(6, questions);
        preparedStatement.executeUpdate();

    }
    public ArrayList<Quiz> getPopularQuizzes(int limit) throws SQLException {
        ArrayList<Quiz> quizzes = new ArrayList<>();
        Statement stmt = connect.createStatement();
        String getQuizzes = "SELECT * FROM " + tableName + " ORDER BY COMPLETED DESC LIMIT " + limit + ";";
        return getQuizzes(quizzes, stmt, getQuizzes);
    }

    private ArrayList<Quiz> getQuizzes(ArrayList<Quiz> quizzes, Statement stmt, String getQuizzes) throws SQLException {
        ResultSet result = stmt.executeQuery(getQuizzes);
        while (result.next()) {
            String quizName = result.getString("QUIZNAME");
            String description = result.getString("DESCRIPT");
            int creatorID = result.getInt("CREATORID");
            Date creatingDate = result.getDate("CREATINGTIME");
            int completed = result.getInt("COMPLETED");
            String questions = result.getString("QUESTIONS");
            Quiz quiz = new Quiz(quizName, description, creatingDate, creatorID, questions, completed);
            quizzes.add(quiz);
        }
        return quizzes;
    }

    public ArrayList<Quiz> getLastDayQuizzes() throws SQLException {
        ArrayList<Quiz> quizzes = new ArrayList<>();
        Statement stmt = connect.createStatement();
        String getQuizzes = "SELECT * FROM " + tableName + " WHERE CREATIONTIME > DATE_SUB(CURDATE(),INTERVAL 1 DAY);";
        return getQuizzes(quizzes, stmt, getQuizzes);
    }

}
