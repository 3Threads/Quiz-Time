package Tests;
import static org.junit.jupiter.api.Assertions.*;
import FunctionalClasses.QuizesConnect;
import FunctionalClasses.UserConnect;
import Types.Quiz;
import org.junit.jupiter.api.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class QuizesConnectTest {
    private static QuizesConnect quizzes;
    @BeforeAll
    public static void setup() throws SQLException {
        UserConnect uConnect;
        quizzes = new QuizesConnect(true);
        uConnect = new UserConnect(true);
        uConnect.addUser("1", "1");
        uConnect.addUser("2", "2");
        uConnect.addUser("3", "1");
        uConnect.addUser("4", "2");
        uConnect.addUser("5", "1");
        quizzes.addQuiz("quiz1", "new quiz", 1,  "1, 2, 3, 4" );
        quizzes.addQuiz("quiz2", "new quiz", 3,  "1, 2, 3, 4, 5" );
        quizzes.addQuiz("quiz3", "new quiz", 2,  "1, 2, 3" );
        quizzes.addQuiz("quiz4", "my quiz", 1,  "1, 2, 3, 5, 7, 8" );
        quizzes.addQuiz("quiz5", "new quiz", 2,  "1, 2, 3" );
        quizzes.addQuiz("quiz6", "my quiz", 1,  "1, 2, 3, 5, 7, 8" );
    }

    @Test
    public void testAddQuizzes1() throws SQLException {
        Quiz quiz1 = quizzes.getQuizInfo(1);
        assertEquals(1, quiz1.getQuizId());
        assertEquals("quiz1", quiz1.getQuizName());
        assertEquals("new quiz", quiz1.getQuizDescription());
        assertEquals(1, quiz1.getCreatorID());
        assertEquals("[1,  2,  3,  4]", quiz1.getQuestionsID().toString());
    }
    @Test
    public void testAddQuizzes2() throws SQLException {
        Quiz quiz2 = quizzes.getQuizInfo(2);
        assertEquals(2, quiz2.getQuizId());
        assertEquals("quiz2", quiz2.getQuizName());
        assertEquals("new quiz", quiz2.getQuizDescription());
        assertEquals(3, quiz2.getCreatorID());
        assertEquals("[1,  2,  3,  4,  5]", quiz2.getQuestionsID().toString());
        Quiz quiz3 = quizzes.getQuizInfo(3);
        assertEquals(3, quiz3.getQuizId());
        assertEquals("quiz3", quiz3.getQuizName());
        assertEquals("new quiz", quiz3.getQuizDescription());
        assertEquals(2, quiz3.getCreatorID());
        assertEquals("[1,  2,  3]", quiz3.getQuestionsID().toString());
        Quiz quiz4 = quizzes.getQuizInfo(4);
        assertEquals(4, quiz4.getQuizId());
        assertEquals("quiz4", quiz4.getQuizName());
        assertEquals("my quiz", quiz4.getQuizDescription());
        assertEquals(1, quiz4.getCreatorID());
        assertEquals("[1,  2,  3,  5,  7,  8]", quiz4.getQuestionsID().toString());
    }
    @Test
    public void testDeleteQuiz() throws SQLException {
        quizzes.deleteQuiz(5);
        assertNull(quizzes.getQuizInfo(5));
        quizzes.deleteQuiz(6);
        assertNull(quizzes.getQuizInfo(6));
    }
    @Test
    public void testCompletedQuizAndPopularQuizzes() throws SQLException {
        for(int i = 0; i < 5; i++) {
            quizzes.completeQuiz(2);
        }
        Quiz quiz = quizzes.getQuizInfo(2);
        assertEquals(5, quiz.getCompleted());
        for(int i = 0; i < 10; i++) {
            quizzes.completeQuiz(1);
        }
        quiz = quizzes.getQuizInfo(1);
        assertEquals(10, quiz.getCompleted());
        for(int i = 0; i < 15; i++) {
            quizzes.completeQuiz(3);
        }
        quiz = quizzes.getQuizInfo(3);
        assertEquals(15, quiz.getCompleted());

        ArrayList<Quiz> arr = quizzes.getPopularQuizzes(1);
        quiz = arr.get(0);
        assertEquals(quiz.getCompleted(), 15);
        assertEquals(quiz.getQuizId(), 3);

        arr = quizzes.getPopularQuizzes(2);
        quiz = arr.get(0);
        assertEquals(quiz.getCompleted(), 15);
        assertEquals(quiz.getQuizId(), 3);
        quiz = arr.get(1);
        assertEquals(quiz.getCompleted(), 10);
        assertEquals(quiz.getQuizId(), 1);

        arr = quizzes.getPopularQuizzes(3);
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
    public void testLastDayQuizzes() throws SQLException {
        ArrayList<Quiz> quizzes1 = quizzes.getLastDayQuizzes();
        assertEquals(6, quizzes1.size());
    }
}
