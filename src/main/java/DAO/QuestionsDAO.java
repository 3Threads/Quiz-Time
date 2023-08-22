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


    /**
     * Constructor for the QuestionsDAO class.
     *
     * @param dataSource The data source used for database connections and operations.
     */
    public QuestionsDAO(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }


    /**
     * Adds a question to the database for a specific quiz.
     * This method establishes a database connection using the provided data source.
     * It inserts a new question into the QUESTIONS table with the provided question details.
     * The question's type, associated quiz ID, question text, and generated answers are inserted.
     * If any SQLException occurs during the database operations, the exception is caught, and the stack trace is printed.
     * The database connection is properly closed in the finally block to ensure resource release, even if an exception occurs.
     *
     * @param question The Question object containing the details of the question.
     * @param quizID The ID of the quiz to which the question belongs.
     */
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


    /**
     * Retrieves a list of questions associated with a specific quiz from the database.
     * This method establishes a database connection using the provided data source.
     * It queries the QUESTIONS table to retrieve questions with the given quiz ID.
     * The retrieved data is then used to create Question objects of various types based on the category.
     * The questions are added to an ArrayList and returned.
     * If any SQLException occurs during the database operations, the exception is caught, and the stack trace is printed.
     * The database connection is properly closed in the finally block to ensure resource release, even if an exception occurs.
     *
     * @param quizId The ID of the quiz for which to retrieve the questions.
     * @return An ArrayList containing Question objects associated with the specified quiz.
     */
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

