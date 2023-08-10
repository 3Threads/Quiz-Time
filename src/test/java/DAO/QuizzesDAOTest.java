package DAO;

import Types.Quiz;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Time;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class QuizzesDAOTest {
    private static QuizzesDAO quizzes;
    //Creating users and quizzes
    @BeforeAll
    public static void setup() {
        BasicDataSource dataSource = DataSource.getDataSource(true);

        UsersDAO uConnect;
        RatingsDAO ratings;
        quizzes = new QuizzesDAO(dataSource);
        uConnect = new UsersDAO(dataSource);
        ratings = new RatingsDAO(dataSource);
        uConnect.addUser("1", "1");
        uConnect.addUser("2", "2");
        uConnect.addUser("3", "1");
        uConnect.addUser("4", "2");
        uConnect.addUser("5", "1");
        quizzes.addQuiz("quiz1", "new quiz", 1, new Time(0, 2, 3), "Sports,History");
        quizzes.addQuiz("quiz2", "new quiz", 3, new Time(1, 2, 3), "Music,Art");
        quizzes.addQuiz("quiz3", "new quiz", 2, new Time(3, 12, 0), "Science,History");
        quizzes.addQuiz("quiz4", "my quiz", 1, new Time(4, 0, 3), "Economy,Politics,History");
        quizzes.addQuiz("quiz5", "new quiz", 2, new Time(3, 2, 3), "Geography,History,Other");
        quizzes.addQuiz("quiz6", "my quiz", 1, new Time(0, 0, 0), "Entertainment");

        ratings.addRatingToQuiz(1, 1, 2, "aa");
        ratings.addRatingToQuiz(2, 1, 4, "aa");
        ratings.addRatingToQuiz(3, 2, 1, "aa");
        ratings.addRatingToQuiz(4, 5, 4, "aa");
        ratings.addRatingToQuiz(5, 1, 2, "aa");
        ratings.addRatingToQuiz(1, 3, 3, "aa");
    }

    /*
        Testing with 2 tests that addQuiz method works correctly
        by using getQuizInfo method and checking returned info.
     */
    @Test
    public void testAddQuizzes1() {
        Quiz quiz1 = quizzes.getQuizInfo(1);
        int quizID = 1;
        String quizName = "quiz1";

        assertEquals(quizID, quiz1.getQuizId());
        assertEquals("quiz1", quiz1.getQuizName());
        assertEquals("new quiz", quiz1.getQuizDescription());
        assertEquals(1, quiz1.getCreatorID());

        //test for getQuizByName();
        assertEquals(quizID, quizzes.getQuizByName(quizName).getQuizId());
    }

    @Test
    public void testAddQuizzes2() {
        Quiz quiz2 = quizzes.getQuizInfo(2);
        int quiz2ID = 2;
        String quiz2Name = "quiz2";

        assertEquals(quiz2ID, quiz2.getQuizId());
        assertEquals(quiz2Name, quiz2.getQuizName());
        assertEquals("new quiz", quiz2.getQuizDescription());
        assertEquals(3, quiz2.getCreatorID());
        //test for getQuizByName();
        assertEquals(quiz2ID, quizzes.getQuizByName(quiz2Name).getQuizId());

        Quiz quiz3 = quizzes.getQuizInfo(3);
        int quiz3ID = 3;
        String quiz3Name = "quiz3";

        assertEquals(quiz3ID, quiz3.getQuizId());
        assertEquals(quiz3Name, quiz3.getQuizName());
        assertEquals("new quiz", quiz3.getQuizDescription());
        assertEquals(2, quiz3.getCreatorID());
        //test for getQuizByName();
        assertEquals(quiz3ID, quizzes.getQuizByName(quiz3Name).getQuizId());

        Quiz quiz4 = quizzes.getQuizInfo(4);
        int quiz4ID = 4;
        String quiz4Name = "quiz4";

        assertEquals(4, quiz4.getQuizId());
        assertEquals("quiz4", quiz4.getQuizName());
        assertEquals("my quiz", quiz4.getQuizDescription());
        assertEquals(1, quiz4.getCreatorID());
        //test for getQuizByName();
        assertEquals(quiz4ID, quizzes.getQuizByName(quiz4Name).getQuizId());
    }

    /*
        Testing quiz delete method and with getQuizInfo checking that this quiz does not exist in database
     */
    @Test
    public void testDeleteQuiz() {
        quizzes.deleteQuiz(5);
        assertNull(quizzes.getQuizInfo(5));
        quizzes.deleteQuiz(6);
        assertNull(quizzes.getQuizInfo(6));
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
            quizzes.completeQuiz(2);
        }
        Quiz quiz = quizzes.getQuizInfo(2);
        assertEquals(5, quiz.getCompleted());
        for (int i = 0; i < 10; i++) {
            quizzes.completeQuiz(1);
        }
        quiz = quizzes.getQuizInfo(1);
        assertEquals(10, quiz.getCompleted());
        for (int i = 0; i < 15; i++) {
            quizzes.completeQuiz(3);
        }
        quiz = quizzes.getQuizInfo(3);
        assertEquals(15, quiz.getCompleted());

        ArrayList<Quiz> arr = quizzes.getPopularQuizzes();
        quiz = arr.get(0);
        assertEquals(quiz.getCompleted(), 15);
        assertEquals(quiz.getQuizId(), 3);

        arr = quizzes.getPopularQuizzes();
        quiz = arr.get(0);
        assertEquals(quiz.getCompleted(), 15);
        assertEquals(quiz.getQuizId(), 3);
        quiz = arr.get(1);
        assertEquals(quiz.getCompleted(), 10);
        assertEquals(quiz.getQuizId(), 1);

        arr = quizzes.getPopularQuizzes();
        quiz = arr.get(0);
        assertEquals(quiz.getCompleted(), 15);
        assertEquals(quiz.getQuizId(), 3);
        quiz = arr.get(1);
        assertEquals(quiz.getCompleted(), 10);
        assertEquals(quiz.getQuizId(), 1);
        quiz = arr.get(2);
        assertEquals(quiz.getCompleted(), 5);
        assertEquals(quiz.getQuizId(), 2);
    }

    @Test
    public void testLastDayQuizzes() {
        ArrayList<Quiz> quizzes1 = quizzes.getLastDayQuizzes();
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
        assertEquals(quiz1.getQuizName(), "quiz1");
        assertEquals(quiz1.getQuizId(), 1);
        Quiz quiz2 = quizzes.getQuizByName("quiz2");
        assertEquals(quiz2.getQuizName(), "quiz2");
        assertEquals(quiz2.getQuizId(), 2);
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
}
