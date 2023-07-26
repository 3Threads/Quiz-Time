package Tests;

import DAO.DataSource;
import DAO.QuestionsDAO;
import DAO.QuizzesDAO;
import DAO.UsersDAO;
import Types.Question;
import Types.QuestionTextResponse;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.AssertJUnit.assertEquals;

public class QuestionsDAOTest {

    private static QuestionsDAO questions;
    private static QuizzesDAO quizzes;
    private static UsersDAO uConnect;


    @BeforeAll
    public static void setup() {
        BasicDataSource dataSource = DataSource.getDataSource(true);

        uConnect = new UsersDAO(dataSource);
        quizzes = new QuizzesDAO(dataSource);
        questions = new QuestionsDAO(dataSource);

        for (int i = 1; i <= 10; i++) {
            // user ID is value of i
            String user = String.valueOf(i);
            uConnect.addUser(user, "1");

            // quiz ID is value of i
            String quizName = "quiz" + i;
            quizzes.addQuiz(quizName, "new quiz", i);

            // question ID:  1    ,  2    ,  3    ....
            //              "100" , "200" , "300" ....
            String questionText = String.valueOf(100 * i);

            // {(10)}, {(20)}, {(30)}, .....
            ArrayList<String> answers = new ArrayList<>();
            answers.add(String.valueOf(10 * i));

            Question question = new QuestionTextResponse(questionText, answers);

            questions.addQuestion(question, i);
        }

        quiz11();
        quiz12();
        quiz13();
    }


    @Test
    public void testAddQuestion() {
        assertEquals(new ArrayList<>(List.of(new Integer[]{1})), questions.getQuestionsIdByQuizId(1));
        assertEquals("100", questions.getQuestion(1));
        assertEquals(new ArrayList<>(List.of(new Integer[]{2})), questions.getQuestionsIdByQuizId(2));
        assertEquals("200", questions.getQuestion(2));
        assertEquals(new ArrayList<>(List.of(new Integer[]{3})), questions.getQuestionsIdByQuizId(3));
        assertEquals("300", questions.getQuestion(3));
        assertEquals(new ArrayList<>(List.of(new Integer[]{4})), questions.getQuestionsIdByQuizId(4));
        assertEquals("400", questions.getQuestion(4));
        assertEquals(new ArrayList<>(List.of(new Integer[]{5})), questions.getQuestionsIdByQuizId(5));
        assertEquals("500", questions.getQuestion(5));
    }


    private static void quiz11() {
        uConnect.addUser("11", "1");
        quizzes.addQuiz("quiz11", "new quiz", 11);

        // Question #11
        String questionText1 = "are you ok??";
        ArrayList<String> answers1 = new ArrayList<>();
        answers1.add("yes");
        answers1.add("no");
        answers1.add("I am trying");
        Question question1 = new QuestionTextResponse(questionText1, answers1);

        // Question #12
        String questionText2 = "r u sure?";
        ArrayList<String> answers2 = new ArrayList<>();
        answers2.add("I am not sure");
        answers2.add("yes I am sure");
        Question question2 = new QuestionTextResponse(questionText2, answers2);

        questions.addQuestion(question1, 11);
        questions.addQuestion(question2, 11);
    }

    @Test
    // QUIZ #11
    public void testAddQuestionsToQuizz() {
        assertEquals(new ArrayList<>(List.of(new Integer[]{11, 12})), questions.getQuestionsIdByQuizId(11));
        assertEquals("are you ok??", questions.getQuestion(11));
        assertEquals("r u sure?", questions.getQuestion(12));
    }


    private static void quiz12() {
        uConnect.addUser("12", "1");
        quizzes.addQuiz("quiz12", "new quiz", 12);

        // Question #13
        String questionText1 = "are you ok?";
        ArrayList<String> answers1 = new ArrayList<>();
        answers1.add("yes");
        answers1.add("no");
        answers1.add("I am trying");

        Question question1 = new QuestionTextResponse(questionText1, answers1);

        questions.addQuestion(question1, 12);
    }

    @Test
    // QUIZ #12
    public void testRemoveQuestion() {
        assertEquals(new ArrayList<>(List.of(new Integer[]{13})), questions.getQuestionsIdByQuizId(12));

        questions.removeQuestion(13);
        assertEquals(new ArrayList<>(List.of(new Integer[]{})), questions.getQuestionsIdByQuizId(12));
    }


    private static void quiz13() {
        uConnect.addUser("13", "1");
        quizzes.addQuiz("quiz13", "new quiz", 13);

        // Question #14
        String questionText1 = "are you okk?";
        ArrayList<String> answers1 = new ArrayList<>();
        answers1.add("yes");
        answers1.add("no");
        answers1.add("I am trying");
        Question question1 = new QuestionTextResponse(questionText1, answers1);

        // Question #15
        String questionText2 = "for real?";
        ArrayList<String> answers2 = new ArrayList<>();
        answers2.add("I am not sure");
        answers2.add("yes I am sure");
        Question question2 = new QuestionTextResponse(questionText2, answers2);

        // Question #16
        String questionText3 = "how good?";
        ArrayList<String> answers3 = new ArrayList<>();
        answers2.add("very");
        answers2.add("not at all");
        Question question3 = new QuestionTextResponse(questionText3, answers3);

        questions.addQuestion(question1, 13);
        questions.addQuestion(question2, 13);
        questions.addQuestion(question3, 13);
    }

    @Test
    // QUIZ #13
    public void testRemoveQuestions() {
        assertEquals(new ArrayList<>(List.of(new Integer[]{14, 15, 16})), questions.getQuestionsIdByQuizId(13));
        assertEquals("are you okk?", questions.getQuestion(14));
        assertEquals("for real?", questions.getQuestion(15));
        assertEquals("how good?", questions.getQuestion(16));

        questions.removeQuestion(15);
        assertEquals(new ArrayList<>(List.of(new Integer[]{14, 16})), questions.getQuestionsIdByQuizId(13));
        assertEquals("are you okk?", questions.getQuestion(14));
        assertEquals("how good?", questions.getQuestion(16));

        questions.removeQuestion(14);
        questions.removeQuestion(16);
        assertEquals(new ArrayList<>(List.of(new Integer[]{})), questions.getQuestionsIdByQuizId(13));
    }

}
