package DAO;

import Types.Quiz;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.*;

import java.sql.Time;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class QuizzesDAOTest {
    private static QuizzesDAO quizzes;
    private static UsersDAO uConnect;
    private static BasicDataSource dataSource;
    private static final String[] TABLE_NAMES = new String[]{"RATINGS", "QUIZZES", "USERS"};
    private static final int[] USERS = new int[5];
    //Creating users and quizzes
    @BeforeAll
    public static void setup() {
        dataSource = DataSource.getDataSource(true);

        quizzes = new QuizzesDAO(dataSource);
        uConnect = new UsersDAO(dataSource);
        for(int i = 0; i < 5; i++) {
            uConnect.addUser(String.valueOf(i), String.valueOf(i));
            USERS[i] = uConnect.getUserId(String.valueOf(i));
        }
        quizzes.addQuiz("quiz1", "new quiz", USERS[0], new Time(0, 2, 3), "Sports,History");
        quizzes.addQuiz("quiz2", "new quiz", USERS[2], new Time(1, 2, 3), "Music,Art");
        quizzes.addQuiz("quiz3", "new quiz", USERS[1], new Time(3, 12, 0), "Science,History");
        quizzes.addQuiz("quiz4", "my quiz", USERS[0], new Time(4, 0, 3), "Economy,Politics,History");
        quizzes.addQuiz("quiz5", "new quiz", USERS[1], new Time(3, 2, 3), "Geography,History,Other");
        quizzes.addQuiz("quiz6", "my quiz", USERS[0], new Time(0, 0, 0), "Entertainment");
        RatingsDAO ratings = new RatingsDAO(dataSource);
        ratings.addRatingToQuiz(USERS[0], quizzes.getQuizByName("quiz1").getQuizId(), 2, "aa");
        ratings.addRatingToQuiz(USERS[1], quizzes.getQuizByName("quiz1").getQuizId(), 4, "aa");
        ratings.addRatingToQuiz(USERS[2], quizzes.getQuizByName("quiz2").getQuizId(), 1, "aa");
        ratings.addRatingToQuiz(USERS[3], quizzes.getQuizByName("quiz5").getQuizId(), 4, "aa");
        ratings.addRatingToQuiz(USERS[4], quizzes.getQuizByName("quiz1").getQuizId(), 2, "aa");
        ratings.addRatingToQuiz(USERS[0], quizzes.getQuizByName("quiz3").getQuizId(), 3, "aa");
    }

    /*
        Testing with 2 tests that addQuiz method works correctly
        by using getQuizInfo method and checking returned info.
     */
    @Test
    public void testAddQuizzes1() {
        Quiz quiz1 = quizzes.getQuizInfo(quizzes.getQuizByName("quiz1").getQuizId());
        String quizName = "quiz1";
        assertEquals(quizName, quiz1.getQuizName());
        assertEquals("new quiz", quiz1.getQuizDescription());
    }

    @Test
    public void testAddQuizzes2() {
        Quiz quiz2 = quizzes.getQuizInfo(quizzes.getQuizByName("quiz2").getQuizId());
        String quiz2Name = "quiz2";
        assertEquals(quiz2Name, quiz2.getQuizName());
        assertEquals("new quiz", quiz2.getQuizDescription());

        Quiz quiz3 = quizzes.getQuizInfo(quizzes.getQuizByName("quiz3").getQuizId());
        String quiz3Name = "quiz3";
        assertEquals(quiz3Name, quiz3.getQuizName());
        assertEquals("new quiz", quiz3.getQuizDescription());

        Quiz quiz4 = quizzes.getQuizInfo(quizzes.getQuizByName("quiz4").getQuizId());
        String quiz4Name = "quiz4";
        assertEquals(quiz4Name, quiz4.getQuizName());
        assertEquals("my quiz", quiz4.getQuizDescription());
    }

    /*
        Testing quiz delete method and with getQuizInfo checking that this quiz does not exist in database
     */
    @Test
    public void testDeleteQuiz() {
        quizzes.addQuiz("quiz8", "new quiz", uConnect.getUserId("1"), new Time(0, 2, 3), "Sports,History");
        Quiz quiz = quizzes.getQuizByName("quiz8");
        assertNotNull(quiz);
        quizzes.deleteQuiz(quizzes.getQuizByName("quiz8").getQuizId());
        Quiz quiz1 = quizzes.getQuizByName("quiz8");
        assertNull(quiz1);
    }

    /*
        Testing completedQuiz method, increase completed num to 5, 10 and 15 from 2, 1 and 3 ID quizzes
        and checking that everything correctly added
        than testing getPopularQuizzes method, which returns list of popular quizzes by completed num desc
        reference to the method the limit of list size and checking that returns correctly list
     */
    @Test
    public void testCompletedQuizAndPopularQuizzes() {
        for (int i = 0; i < 5; i++) {
            quizzes.completeQuiz(quizzes.getQuizByName("quiz2").getQuizId());
        }
        Quiz quiz = quizzes.getQuizByName("quiz2");
        assertEquals(5, quiz.getCompleted());
        for (int i = 0; i < 10; i++) {
            quizzes.completeQuiz(quizzes.getQuizByName("quiz1").getQuizId());
        }
        quiz = quizzes.getQuizByName("quiz1");
        assertEquals(10, quiz.getCompleted());
        for (int i = 0; i < 15; i++) {
            quizzes.completeQuiz(quizzes.getQuizByName("quiz3").getQuizId());
        }
        quiz = quizzes.getQuizByName("quiz3");
        assertEquals(15, quiz.getCompleted());

        ArrayList<Quiz> arr = quizzes.getPopularQuizzes();
        quiz = arr.get(0);
        assertEquals(quiz.getCompleted(), 15);

        arr = quizzes.getPopularQuizzes();
        quiz = arr.get(0);
        assertEquals(quiz.getCompleted(), 15);
        quiz = arr.get(1);
        assertEquals(quiz.getCompleted(), 10);

        arr = quizzes.getPopularQuizzes();
        quiz = arr.get(0);
        assertEquals(quiz.getCompleted(), 15);
        quiz = arr.get(1);
        assertEquals(quiz.getCompleted(), 10);
        quiz = arr.get(2);
        assertEquals(quiz.getCompleted(), 5);
    }

    @Test
    public void testLastDayQuizzes() {
        ArrayList<Quiz> quizzes1 = quizzes.getRecentQuizzes();
        assertEquals(6, quizzes1.size());
    }
    @Test
    public void testCheckQuizName() {
        assertEquals(false, quizzes.checkQuizName("quiz1"));
        assertEquals(false, quizzes.checkQuizName("quiz2"));
        assertEquals(true, quizzes.checkQuizName("quiz10"));
        assertEquals(false, quizzes.checkQuizName("quiz3"));
        assertEquals(true, quizzes.checkQuizName("quiz12"));
        assertEquals(false, quizzes.checkQuizName("quiz5"));
    }
    @Test
    public void testGetQuizByName() {
        Quiz quiz1 = quizzes.getQuizByName("quiz1");
        assertEquals(quiz1.getQuizName(), "quiz1");;
        Quiz quiz2 = quizzes.getQuizByName("quiz2");
        assertEquals(quiz2.getQuizName(), "quiz2");
        Quiz quiz12 = quizzes.getQuizByName("quiz12");
        assertEquals(null, quiz12);
    }
    @Test
    public void testSearchQuizzes() {
        ArrayList<Quiz> quizzes1 = quizzes.searchQuizzes("quiz1");
        assertEquals(quizzes1.size(), 1);
        assertEquals(quizzes1.get(0).getQuizName(), "quiz1");
        ArrayList<Quiz> quizzes2 = quizzes.searchQuizzes("quiz");
        assertEquals(quizzes2.size(), 6);
        ArrayList<Quiz> quizzes3 = quizzes.searchQuizzes("newww");
        assertEquals(quizzes3.size(), 0);
    }
    @Test
    public void testDeepSearchQuizzes() {
        ArrayList<Quiz> quizzes1 = quizzes.deepSearchQuizzes("", 0, "History");
        assertEquals(quizzes1.size(), 4);
        ArrayList<Quiz> quizzes2 = quizzes.deepSearchQuizzes("quiz1", 0, "");
        assertEquals(quizzes2.size(), 1);
        assertEquals(quizzes2.get(0).getQuizName(), "quiz1");
        ArrayList<Quiz> quizzes3 = quizzes.deepSearchQuizzes("quiz", 3, "History");
        assertEquals(quizzes3.size(), 2);
        assertEquals(quizzes3.get(0).getQuizName(), "quiz3");
        assertEquals(quizzes3.get(1).getQuizName(), "quiz5");
        ArrayList<Quiz> quizzes4 = quizzes.deepSearchQuizzes("quiz", 3, "Science,History");
        assertEquals(quizzes4.size(), 1);
        assertEquals(quizzes4.get(0).getQuizName(), "quiz3");
        ArrayList<Quiz> quizzes5 = quizzes.deepSearchQuizzes("", 4, "History");
        assertEquals(quizzes5.size(), 1);
        assertEquals(quizzes5.get(0).getQuizName(), "quiz5");
    }
    @Test
    public void testGetMyCreatedQuizzes() {
        ArrayList<Quiz> quizzes1 = quizzes.getMyCreatedQuizzes(USERS[0]);
        assertEquals(quizzes1.size(), 3);
        ArrayList<Quiz> quizzes2 = quizzes.getMyCreatedQuizzes(USERS[1]);
        assertEquals(quizzes2.size(), 2);
        ArrayList<Quiz> quizzes3 = quizzes.getMyCreatedQuizzes(USERS[2]);
        assertEquals(quizzes3.size(), 1);
        ArrayList<Quiz> quizzes4 = quizzes.getMyCreatedQuizzes(USERS[3]);
        assertEquals(quizzes4.size(), 0);
    }
    @AfterAll
    public static void finish() {
        DataSource.clearTables(dataSource, TABLE_NAMES);
    }
}
