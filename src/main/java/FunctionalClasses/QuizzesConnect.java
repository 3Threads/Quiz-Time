package FunctionalClasses;

import java.sql.*;
import java.util.ArrayList;

import Types.Quiz;

public class QuizzesConnect extends SQLConnect {
    private final String tableName = "QUIZZES";

    public QuizzesConnect(boolean isTesting) {
        super(isTesting);
    }

    public void addQuiz(String quizName, String description, int creatorID, String questions) throws SQLException {
        String str = "INSERT INTO " + tableName + " VALUES(default,?,?,0,default,?,?)";
        PreparedStatement preparedStatement = connect.prepareStatement(str);
        preparedStatement.setString(1, quizName);
        preparedStatement.setString(2, description);
        preparedStatement.setString(3, String.valueOf(creatorID));
        preparedStatement.setString(4, questions);
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
            int quizId = result.getInt("ID");
            String quizName = result.getString("QUIZ_NAME");
            String description = result.getString("DESCRIPTION");
            int creatorID = result.getInt("CREATOR_ID");
            Date creatingDate = result.getDate("CREATION_TIME");
            int completed = result.getInt("COMPLETED");
            String questions = result.getString("QUESTIONS");
            Quiz quiz = new Quiz(quizId,quizName, description, creatingDate, creatorID, questions, completed);
            quizzes.add(quiz);
        }
        return quizzes;
    }

    public ArrayList<Quiz> getLastDayQuizzes() throws SQLException {
        ArrayList<Quiz> quizzes = new ArrayList<>();
        Statement stmt = connect.createStatement();
        String getQuizzes = "SELECT * FROM " + tableName + " WHERE CREATION_TIME > DATE_SUB(CURDATE(),INTERVAL 1 DAY)" +
                                                                                                "ORDER BY CREATION_TIME;";
        return getQuizzes(quizzes, stmt, getQuizzes);
    }
    public Quiz getQuizInfo(int quizID) throws SQLException {
        Statement stmt = connect.createStatement();
        String getQuiz = "SELECT * FROM QUIZZES WHERE ID = " + quizID + ";";
        ArrayList<Quiz> quizzes = new ArrayList<>();
        getQuizzes(quizzes, stmt, getQuiz);
        if(quizzes.isEmpty()) return null;
        else return quizzes.get(0);
    }
    public void completeQuiz(int quizId) throws SQLException {
        Statement stmt = connect.createStatement();
        String updateCompletedNum = "UPDATE QUIZZES SET COMPLETED = COMPLETED + 1 WHERE ID = " + quizId + ";";
        stmt.execute(updateCompletedNum);
    }
    public void deleteQuiz(int quizId) throws SQLException {
        Statement stmt = connect.createStatement();
        String deleteQuiz = "DELETE FROM QUIZZES WHERE ID = " +quizId+ ";";
        stmt.execute(deleteQuiz);
    }

}
