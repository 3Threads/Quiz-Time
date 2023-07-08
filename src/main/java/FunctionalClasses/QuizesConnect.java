package FunctionalClasses;

import java.sql.*;
import java.util.ArrayList;
import Types.Quiz;
public class QuizesConnect extends SQLconnect{
    private final String tableName;

    public QuizesConnect(String tableName) {
        super();
        this.tableName = tableName;
    }

    public ArrayList<Quiz> getPopularQuizzes(int limit) throws SQLException {
        ArrayList<Quiz> quizzes = new ArrayList<>();
        Statement stmt = connect.createStatement();
        stmt.execute("USE " + database);
        String getQuizzes = "SELECT * FROM "+tableName+" ORDER BY COMPLETED DESC LIMIT " + limit + ";";
        ResultSet result = stmt.executeQuery(getQuizzes);
        while(result.next()) {
            String quizname = result.getString("QUIZNAME");
            String description = result.getString("DESCRIPT");
            int creatorID = result.getInt("CREATORID");
            Date creatingDate = result.getDate("CREATINGTIME");
            int completed = result.getInt("COMPLETED");
            String questions = result.getString("QUESTIONS");
            Quiz quiz = new Quiz(quizname, description, creatingDate, creatorID, questions, completed);
            quizzes.add(quiz);
        }
        return quizzes;
    }
    public ArrayList<Quiz> getLastDayQuizzes() throws SQLException {
        ArrayList<Quiz> quizzes = new ArrayList<>();
        Statement stmt = connect.createStatement();
        stmt.execute("USE " + database);
        String getQuizes = "SELECT * FROM "+tableName+" WHERE CREATIONTIME > DATE_SUB(CURDATE(),INTERVAL 1 DAY);";
        ResultSet result = stmt.executeQuery(getQuizes);
        while(result.next()) {
            String quizname = result.getString("QUIZNAME");
            String description = result.getString("DESCRIPT");
            int creatorID = result.getInt("CREATORID");
            Date creatingDate = result.getDate("CREATINGTIME");
            int completed = result.getInt("COMPLETED");
            String questions = result.getString("QUESTIONS");
            Quiz quiz = new Quiz(quizname, description, creatingDate, creatorID, questions, completed);
            quizzes.add(quiz);
        }
        return quizzes;
    }

}
