package DAO;

import Types.Question;
import org.apache.commons.dbcp2.BasicDataSource;
import java.sql.*;
import java.util.ArrayList;

public class QuestionsDAO {
    private final BasicDataSource dataSource;

    public QuestionsDAO(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void addQuestion(Question question, int quizID){
        Connection connect = null;
        try{
            connect = dataSource.getConnection();
            String str = "INSERT INTO QUESTIONS VALUES(default, ?, ?, ?, ?);";
            PreparedStatement statement = connect.prepareStatement(str);
            statement.setString(1, question.getType());
            statement.setInt(2, quizID);
            statement.setString(3, question.generateQuestionText());
            statement.setString(4, question.generateAnswers());
            statement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            if(connect != null){
                try{
                    connect.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void removeQuestion(int questionId) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String str = "DELETE FROM QUESTIONS WHERE ID = ?;";
            PreparedStatement statement = connect.prepareStatement(str);
            statement.setInt(1, questionId);
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

    public ArrayList<Integer> getQuestionsIdByQuizId(int quizId) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String str = "SELECT * FROM QUESTIONS WHERE QUIZ_ID = ?;";
            PreparedStatement statement = connect.prepareStatement(str);
            statement.setInt(1, quizId);
            return getQuestions(statement);
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

    private ArrayList<Integer> getQuestions(PreparedStatement statement) throws SQLException {
        ArrayList<Integer> questions = new ArrayList<>();
        ResultSet result = statement.executeQuery();
        while (result.next()) {
            int questionId = result.getInt("ID");
            questions.add(questionId);
        }
        return questions;
    }

    // This may return Question type info.
    public String getQuestion(int id){
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String str = "SELECT * FROM QUESTIONS WHERE ID = ?;";
            PreparedStatement statement = connect.prepareStatement(str);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            result.next();
            return result.getString("QUESTION_TEXT");
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

