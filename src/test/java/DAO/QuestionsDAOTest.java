package DAO;

import Types.*;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.testng.AssertJUnit.assertEquals;

public class QuestionsDAOTest {

    private static QuestionsDAO questionsDAO;
    private static BasicDataSource dataSource;
    private static final String[] TABLE_NAMES = new String[]{"QUESTIONS", "QUIZZES", "USERS"};
    private static final int QUIZZES_NUM = 3;
    private static final int[] QUIZZES_ID = new int[QUIZZES_NUM];

    @BeforeAll
    public static void setup() {
        dataSource = DataSource.getDataSource(true);

        UsersDAO usersDAO = new UsersDAO(dataSource);
        QuizzesDAO quizzesDAO = new QuizzesDAO(dataSource);
        questionsDAO = new QuestionsDAO(dataSource);

        String user = "user0";
        usersDAO.addUser(user, "password");
        int creatorId = usersDAO.getUserId(user);
        for (int i = 1; i <= QUIZZES_NUM; i++) {
            String quizName = "quiz" + i;
            quizzesDAO.addQuiz(quizName, "new quiz", creatorId, null, "Sports");
            QUIZZES_ID[i - 1] = quizzesDAO.getQuizByName(quizName).getQuizId();
        }
    }


    @Test
    public void testAddQuestion() {
        assertEquals(0, questionsDAO.getQuestions(QUIZZES_ID[0]).size());
        assertEquals(0, questionsDAO.getQuestions(QUIZZES_ID[1]).size());
        assertEquals(0, questionsDAO.getQuestions(QUIZZES_ID[2]).size());

        for (int i = 0; i < 6; i++) {
            Question question = new QuestionTextResponse("Which is the best country?", new ArrayList<>(Arrays.asList("Georgia", "geo")));
            questionsDAO.addQuestion(question, QUIZZES_ID[0]);
        }
        Question question = new QuestionTextResponse("Is other quiz?", new ArrayList<>(Arrays.asList("Yes", "yes")));
        questionsDAO.addQuestion(question, QUIZZES_ID[1]);

        assertEquals(6, questionsDAO.getQuestions(QUIZZES_ID[0]).size());
        assertEquals(1, questionsDAO.getQuestions(QUIZZES_ID[1]).size());
        assertEquals(0, questionsDAO.getQuestions(QUIZZES_ID[2]).size());
    }

    @Test
    public void testGetQuestions() {
        assertEquals(0, questionsDAO.getQuestions(QUIZZES_ID[0]).size());

        //text response
        String questionText1 = "Which is the best country?";
        ArrayList<String> answers1 = new ArrayList<>(Arrays.asList("Georgia", "geo"));
        Question question1 = new QuestionTextResponse(questionText1, answers1);
        questionsDAO.addQuestion(question1, QUIZZES_ID[0]);

        //fill in the blank
        String questionText2 = "This is the";
        String questionTextSecond2 = "project";
        ArrayList<String> answers2 = new ArrayList<>(Arrays.asList("best", "geo"));
        Question question2 = new QuestionFillInTheBlank(questionText2, questionTextSecond2, answers2);
        questionsDAO.addQuestion(question2, QUIZZES_ID[0]);

        //matching
        String questionText3 = "Which is the best country?";
        HashMap<String, String> answers3 = new HashMap<>();
        answers3.put("Akaki", "Gurgenidze");
        answers3.put("Niko", "Khetsuriani");
        answers3.put("Dachi", "Goshadze");
        answers3.put("Lasha", "Kuprashvili");
        Question question3 = new QuestionMatching(questionText3, answers3);
        questionsDAO.addQuestion(question3, QUIZZES_ID[0]);

        //multi answers
        String questionText4 = "Count from 1 to 3";
        ArrayList<String> answers4 = new ArrayList<>(Arrays.asList("1", "2", "3"));
        Question question4 = new QuestionMultiAnswers(questionText4, answers4);
        questionsDAO.addQuestion(question4, QUIZZES_ID[0]);

        //multiple choices
        String questionText5 = "First letter?";
        ArrayList<String> answers5 = new ArrayList<>(Arrays.asList("B", "A"));
        ArrayList<String> correctAnswers5 = new ArrayList<>(List.of("A"));
        Question question5 = new QuestionMultipleChoices(questionText5, correctAnswers5, answers5);
        questionsDAO.addQuestion(question5, QUIZZES_ID[0]);

        //multiple choices with multiple answers
        String questionText6 = "First two letter?";
        ArrayList<String> answers6 = new ArrayList<>(Arrays.asList("B", "A", "C"));
        ArrayList<String> correctAnswers6 = new ArrayList<>(Arrays.asList("B", "A"));
        Question question6 = new QuestionMultipleChoicesWithMultipleAnswers(questionText6, correctAnswers6, answers6);
        questionsDAO.addQuestion(question6, QUIZZES_ID[0]);

        //picture response
        String questionText7 = "Flag belongs to";
        String pictureURL = "https://cdn.britannica.com/17/4717-004-6F48198E/Flag-Republic-of-Georgia.jpg";
        ArrayList<String> answers7 = new ArrayList<>(Arrays.asList("Georgia", "geo"));
        Question question7 = new QuestionPictureResponse(questionText7, pictureURL, answers7);
        questionsDAO.addQuestion(question7, QUIZZES_ID[0]);

        ArrayList<Question> questions = questionsDAO.getQuestions(QUIZZES_ID[0]);
        assertEquals(7, questions.size());

        QuestionTextResponse resultQuestion1 = (QuestionTextResponse) questions.get(0);
        assertEquals(QuestionTypes.textResponse, resultQuestion1.getType());
        assertEquals(questionText1, resultQuestion1.getQuestionText());
        assertEquals(answers1, resultQuestion1.getAnswers());

        QuestionFillInTheBlank resultQuestion2 = (QuestionFillInTheBlank) questions.get(1);
        assertEquals(QuestionTypes.fillInTheBlank, resultQuestion2.getType());
        assertEquals(questionText2, resultQuestion2.getQuestionText());
        assertEquals(questionTextSecond2, resultQuestion2.getQuestionText2());
        assertEquals(answers2, resultQuestion2.getAnswers());

        QuestionMatching resultQuestion3 = (QuestionMatching) questions.get(2);
        assertEquals(QuestionTypes.matching, resultQuestion3.getType());
        assertEquals(questionText3, resultQuestion3.getQuestionText());
        assertEquals(answers3, resultQuestion3.getMatches());

        QuestionMultiAnswers resultQuestion4 = (QuestionMultiAnswers) questions.get(3);
        assertEquals(QuestionTypes.multiAnswers, resultQuestion4.getType());
        assertEquals(questionText4, resultQuestion4.getQuestionText());
        assertEquals(answers4, resultQuestion4.getAnswers());

        QuestionMultipleChoices resultQuestion5 = (QuestionMultipleChoices) questions.get(4);
        assertEquals(QuestionTypes.multipleChoices, resultQuestion5.getType());
        assertEquals(questionText5, resultQuestion5.getQuestionText());
        assertEquals(correctAnswers5, resultQuestion5.getAnswers());
        Collections.sort(answers5);
        ArrayList<String> resultAllAnswers5 = resultQuestion5.getAllAnswers();
        Collections.sort(resultAllAnswers5);
        assertEquals(answers5, resultAllAnswers5);

        QuestionMultipleChoicesWithMultipleAnswers resultQuestion6 = (QuestionMultipleChoicesWithMultipleAnswers) questions.get(5);
        assertEquals(QuestionTypes.multipleChoicesWithMultipleAnswers, resultQuestion6.getType());
        assertEquals(questionText6, resultQuestion6.getQuestionText());
        Collections.sort(correctAnswers6);
        ArrayList<String> resulCorrectAnswers6 = resultQuestion6.getAnswers();
        Collections.sort(resulCorrectAnswers6);
        assertEquals(correctAnswers6, resulCorrectAnswers6);
        Collections.sort(answers6);
        ArrayList<String> resultAllAnswers6 = resultQuestion6.getAllAnswers();
        Collections.sort(resultAllAnswers6);
        assertEquals(answers6, resultAllAnswers6);

        QuestionPictureResponse resultQuestion7 = (QuestionPictureResponse) questions.get(6);
        assertEquals(QuestionTypes.pictureResponse, resultQuestion7.getType());
        assertEquals(questionText7, resultQuestion7.getQuestionText());
        assertEquals(pictureURL, resultQuestion7.getPictureUrl());
        assertEquals(answers7, resultQuestion7.getAnswers());

    }

    @AfterEach
    public void finishUnitTest() {
        DataSource.clearTables(dataSource, new String[]{TABLE_NAMES[0]});
    }

    @AfterAll
    public static void finish() {
        DataSource.clearTables(dataSource, TABLE_NAMES);
    }
}
