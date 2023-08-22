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
            statement.setString(1, String.valueOf(question.getType()));
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
                    question = new QuestionFillInTheBlank(text1, text2, allAnswers);
                }
                if (type.equals("textResponse")) {
                    question = new QuestionTextResponse(questionText, allAnswers);
                }
                if (type.equals("pictureResponse")) {
                    String[] questionTexts = questionText.split(String.valueOf((char) 0));
                    question = new QuestionPictureResponse(questionTexts[0], questionTexts[1], allAnswers);
                }
                if (type.equals("multipleChoices")) {
                    String[] allAnsws = answers.split(String.valueOf((char) 0) + (char) 0);
                    ArrayList<String> correctAnswers = new ArrayList<>(List.of((allAnsws[0].split(String.valueOf((char) 0)))));
                    ArrayList<String> allPossibleAnswers = new ArrayList<>(List.of(allAnsws[1].split(String.valueOf((char) 0))));
                    allPossibleAnswers.addAll(correctAnswers);
                    question = new QuestionMultipleChoices(questionText, correctAnswers, allPossibleAnswers);
                }
                if (type.equals("multipleChoicesWithMultipleAnswers")) {
                    String[] allAnsws = answers.split(String.valueOf((char) 0) + (char) 0);
                    ArrayList<String> correctAnswers = new ArrayList<>(List.of((allAnsws[0].split(String.valueOf((char) 0)))));
                    ArrayList<String> allPossibleAnswers = new ArrayList<>();
                    if(allAnsws.length>=2){
                        allPossibleAnswers.addAll(List.of(allAnsws[1].split(String.valueOf((char) 0))));
                    }
                    allPossibleAnswers.addAll(correctAnswers);
                    question = new QuestionMultipleChoicesWithMultipleAnswers(questionText, correctAnswers, allPossibleAnswers);
                }
                if (type.equals("matching")) {
                    ArrayList<String> allPairs = new ArrayList<>(List.of(answers.split(String.valueOf((char) 0))));
                    HashMap<String, String> pairs = new HashMap<>();
                    for (int i = 0; i < allPairs.size(); i += 2) {
                        pairs.put(allPairs.get(i), allPairs.get(i + 1));
                    }
                    question = new QuestionMatching(questionText, pairs);
                }
                if (type.equals("multiAnswers")) {
                    question = new QuestionMultiAnswers(questionText, allAnswers);
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

