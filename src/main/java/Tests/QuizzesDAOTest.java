//package Tests;
//
//import DAO.DataSource;
//import DAO.QuizzesDAO;
//import DAO.UsersDAO;
//import Types.Quiz;
//import org.apache.commons.dbcp2.BasicDataSource;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//
//import java.util.ArrayList;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNull;
//
//public class QuizzesDAOTest {
//    private static QuizzesDAO quizzes;
//
//    //Creating users and quizzes
//    @BeforeAll
//    public static void setup() {
//        BasicDataSource dataSource = DataSource.getDataSource(true);
//
//        UsersDAO uConnect;
//        quizzes = new QuizzesDAO(dataSource);
//        uConnect = new UsersDAO(dataSource);
//        uConnect.addUser("1", "1");
//        uConnect.addUser("2", "2");
//        uConnect.addUser("3", "1");
//        uConnect.addUser("4", "2");
//        uConnect.addUser("5", "1");
//        quizzes.addQuiz("quiz1", "new quiz", 1, null);
//        quizzes.addQuiz("quiz2", "new quiz", 3, null);
//        quizzes.addQuiz("quiz3", "new quiz", 2, null);
//        quizzes.addQuiz("quiz4", "my quiz", 1, null);
//        quizzes.addQuiz("quiz5", "new quiz", 2, null);
//        quizzes.addQuiz("quiz6", "my quiz", 1, null);
//    }
//
//    /*
//        Testing with 2 tests that addQuiz method works correctly
//        by using getQuizInfo method and checking returned info.
//     */
//    @Test
//    public void testAddQuizzes1() {
//        Quiz quiz1 = quizzes.getQuizInfo(1);
//        int quizID = 1;
//        String quizName = "quiz1";
//
//        assertEquals(quizID, quiz1.getQuizId());
//        assertEquals("quiz1", quiz1.getQuizName());
//        assertEquals("new quiz", quiz1.getQuizDescription());
//        assertEquals(1, quiz1.getCreatorID());
//
//        //test for getQuizByName();
//        assertEquals(quizID, quizzes.getQuizByName(quizName).getQuizId());
//    }
//
//    @Test
//    public void testAddQuizzes2() {
//        Quiz quiz2 = quizzes.getQuizInfo(2);
//        int quiz2ID = 2;
//        String quiz2Name = "quiz2";
//
//        assertEquals(quiz2ID, quiz2.getQuizId());
//        assertEquals(quiz2Name, quiz2.getQuizName());
//        assertEquals("new quiz", quiz2.getQuizDescription());
//        assertEquals(3, quiz2.getCreatorID());
//        //test for getQuizByName();
//        assertEquals(quiz2ID, quizzes.getQuizByName(quiz2Name).getQuizId());
//
//        Quiz quiz3 = quizzes.getQuizInfo(3);
//        int quiz3ID = 3;
//        String quiz3Name = "quiz3";
//
//        assertEquals(quiz3ID, quiz3.getQuizId());
//        assertEquals(quiz3Name, quiz3.getQuizName());
//        assertEquals("new quiz", quiz3.getQuizDescription());
//        assertEquals(2, quiz3.getCreatorID());
//        //test for getQuizByName();
//        assertEquals(quiz3ID, quizzes.getQuizByName(quiz3Name).getQuizId());
//
//        Quiz quiz4 = quizzes.getQuizInfo(4);
//        int quiz4ID = 4;
//        String quiz4Name = "quiz4";
//
//        assertEquals(4, quiz4.getQuizId());
//        assertEquals("quiz4", quiz4.getQuizName());
//        assertEquals("my quiz", quiz4.getQuizDescription());
//        assertEquals(1, quiz4.getCreatorID());
//        //test for getQuizByName();
//        assertEquals(quiz4ID, quizzes.getQuizByName(quiz4Name).getQuizId());
//    }
//
//    /*
//        Testing quiz delete method and with getQuizInfo checking that this quiz does not exist in database
//     */
//    @Test
//    public void testDeleteQuiz() {
//        quizzes.deleteQuiz(5);
//        assertNull(quizzes.getQuizInfo(5));
//        quizzes.deleteQuiz(6);
//        assertNull(quizzes.getQuizInfo(6));
//    }
//
//    /*
//        Testing completedQuiz method, increase completed num to 5, 10 and 15 from 2, 1 and 3 ID quizzes
//        and checking that everything correctly added
//        than testing getPopularQuizzes method, which returns list of popular quizzes by completed num desc
//        reference to the method the limit of list size and checking that returns correctly list
//     */
//    @Test
//    public void testCompletedQuizAndPopularQuizzes() {
//        for (int i = 0; i < 5; i++) {
//            quizzes.completeQuiz(2);
//        }
//        Quiz quiz = quizzes.getQuizInfo(2);
//        assertEquals(5, quiz.getCompleted());
//        for (int i = 0; i < 10; i++) {
//            quizzes.completeQuiz(1);
//        }
//        quiz = quizzes.getQuizInfo(1);
//        assertEquals(10, quiz.getCompleted());
//        for (int i = 0; i < 15; i++) {
//            quizzes.completeQuiz(3);
//        }
//        quiz = quizzes.getQuizInfo(3);
//        assertEquals(15, quiz.getCompleted());
//
//        ArrayList<Quiz> arr = quizzes.getPopularQuizzes();
//        quiz = arr.get(0);
//        assertEquals(quiz.getCompleted(), 15);
//        assertEquals(quiz.getQuizId(), 3);
//
//        arr = quizzes.getPopularQuizzes();
//        quiz = arr.get(0);
//        assertEquals(quiz.getCompleted(), 15);
//        assertEquals(quiz.getQuizId(), 3);
//        quiz = arr.get(1);
//        assertEquals(quiz.getCompleted(), 10);
//        assertEquals(quiz.getQuizId(), 1);
//
//        arr = quizzes.getPopularQuizzes();
//        quiz = arr.get(0);
//        assertEquals(quiz.getCompleted(), 15);
//        assertEquals(quiz.getQuizId(), 3);
//        quiz = arr.get(1);
//        assertEquals(quiz.getCompleted(), 10);
//        assertEquals(quiz.getQuizId(), 1);
//        quiz = arr.get(2);
//        assertEquals(quiz.getCompleted(), 5);
//        assertEquals(quiz.getQuizId(), 2);
//    }
//
//    @Test
//    public void testLastDayQuizzes() {
//        ArrayList<Quiz> quizzes1 = quizzes.getLastDayQuizzes();
//        assertEquals(6, quizzes1.size());
//    }
//}
