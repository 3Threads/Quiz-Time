package DAO;

import BusinessLogic.ListToString;
import Types.Question;
import Types.QuestionResponse;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.ArrayList;

public class QuestionsDAO {
    private final BasicDataSource dataSource;

    public QuestionsDAO(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void addQuestion(String questionText, int quizId, String type, ArrayList<String> answers) {
        Question question = new QuestionResponse(questionText, type, answers);
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String str = "INSERT INTO QUESTIONS VALUES(default, ?, ?, ?, ?);";
            PreparedStatement statement = connect.prepareStatement(str);
            String categoryName = question.getType();  /// ra sachiroa es tu types gadmoacemineb metodisTvis isedac
            statement.setString(1, type);
            statement.setInt(2, quizId);
            statement.setString(3, questionText);
            ListToString lts = new ListToString();
            String res = lts.generateString(answers);
            statement.setString(4, res);
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

