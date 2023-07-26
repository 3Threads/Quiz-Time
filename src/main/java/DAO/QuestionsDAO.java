package DAO;

import Types.*;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuestionsDAO {
    private final BasicDataSource dataSource;

    public QuestionsDAO(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void addQuestion(Question question, int quizID) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String str = "INSERT INTO QUESTIONS VALUES(default, ?, ?, ?, ?);";
            PreparedStatement statement = connect.prepareStatement(str);
            statement.setString(1, question.getType());
            statement.setInt(2, quizID);
            statement.setString(3, question.generateQuestionText());
            statement.setString(4, question.generateAnswers());
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
    public String getQuestion(int id) {
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

    public ArrayList<Question> getQuestions(int quizId) {
        Connection connect = null;
        try {
            ArrayList<Question> questions = new ArrayList<>();
            connect = dataSource.getConnection();
            String str = "SELECT * FROM QUESTIONS WHERE QUIZ_ID = ?;";
            PreparedStatement statement = connect.prepareStatement(str);
            statement.setInt(1, quizId);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                Question question = null;
                String type = result.getString("CATEGORY_NAME");
                String questionText = result.getString("QUESTION_TEXT");
                String answers = result.getString("ANSWERS");
                ArrayList<String> allAnswers = new ArrayList<>(List.of(answers.split(String.valueOf((char) 0))));
                if (type.equals("fillInTheBlank")) {
                    String[] questionTexts = questionText.split(String.valueOf((char) 0));
                    String text1 = questionTexts[0];
                    String text2 = questionTexts[1];
                    question = new FillInTheBlank(text1, text2, "fillInTheBlank", allAnswers);
                }
                if (type.equals("questionResponse")) {
                    question = new QuestionResponse(questionText, "questionResponse", allAnswers);
                }
                if (type.equals("pictureResponse")) {
                    String[] questionTexts = questionText.split(String.valueOf((char) 0));
                    question = new PictureResponse(questionTexts[0], "pictureResponse", questionTexts[1], allAnswers);
                }
                if (type.equals("multipleChoice") || type.equals("multipleChoiceWithMultipleAnswers")) {
                    String[] allAnsws = answers.split(String.valueOf((char) 0) + (char) 0);
                    ArrayList<String> correctAnswers = new ArrayList<>(List.of((allAnsws[0].split(String.valueOf((char) 0)))));
                    ArrayList<String> allPossibleAnswers = new ArrayList<>(List.of(allAnsws[1].split(String.valueOf((char) 0))));
                    allPossibleAnswers.addAll(correctAnswers);
                    question = new MultipleChoice(questionText, type, correctAnswers, allPossibleAnswers);
                }
                if (type.equals("matching")) {
                    ArrayList<String> allPairs = new ArrayList<>(List.of(answers.split(String.valueOf((char) 0))));
                    HashMap<String, String> pairs = new HashMap<>();
                    for (int i = 0; i < allPairs.size(); i += 2) {
                        pairs.put(allPairs.get(i), allPairs.get(i + 1));
                    }
                    question = new Matching(questionText, "matching", pairs);
                }
                if (type.equals("multiAnswer")) {
                    question = new MultiAnswer(questionText, "multiAnswer", allAnswers);
                }
                questions.add(question);
            }
            return questions;
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

