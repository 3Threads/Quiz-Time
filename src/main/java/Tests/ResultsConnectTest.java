package Tests;

import static org.junit.jupiter.api.Assertions.*;
import FunctionalClasses.QuizzesConnect;
import FunctionalClasses.ResultsConnect;
import FunctionalClasses.UserConnect;
import Types.Result;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;

public class ResultsConnectTest {
    private static ResultsConnect results;
    //Creating users, quizzes and adding new results
    @BeforeAll
    public static void setup() throws SQLException {
        UserConnect uConnect;
        QuizzesConnect quizzes;

        uConnect = new UserConnect(true);
        quizzes = new QuizzesConnect(true);
        results = new ResultsConnect(true);

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

        results.addResult(1, 1, 10, new Time(1000));
        results.addResult(1, 1, 15, new Time(800));
        results.addResult(1, 2, 30, new Time(900));
        results.addResult(2, 1, 15, new Time(1500));
        results.addResult(3, 4, 10, new Time(2000));
        results.addResult(5, 1, 14, new Time(500));
        results.addResult(4, 6, 30, new Time(3000));
    }

    //Testing AddResult quiz method by using getUserResultsOnQuiz method
    @Test
    public void testAddResult() throws SQLException {
        ArrayList<Result> res = results.getUserResultsOnQuiz(3, 4);
        assertEquals(res.size(), 1);
        assertEquals(res.get(0).getScore(), 10);

        res = results.getUserResultsOnQuiz(4, 6);
        assertEquals(res.size(), 1);
        assertEquals(res.get(0).getScore(), 30);

        res = results.getUserResultsOnQuiz(5, 1);
        assertEquals(res.size(), 1);
        assertEquals(res.get(0).getScore(), 14);

        res = results.getUserResultsOnQuiz(3, 4);
        assertEquals(res.size(), 1);
        assertEquals(res.get(0).getScore(), 10);

        res = results.getUserResultsOnQuiz(1, 1);
        assertEquals(res.size(), 2);
        assertEquals(res.get(0).getScore(), 15);
        assertEquals(res.get(1).getScore(), 10);
    }

    //Testing that method GetQuizResults correctly returns list of all result on one quiz
    @Test
    public void testGetQuizResults() throws SQLException {
        ArrayList<Result> res = results.getQuizResults(2);
        assertEquals(res.size(), 1);
        assertEquals(res.get(0).getQuizId(), 2);
        assertEquals(res.get(0).getScore(),30);

        res = results.getQuizResults(6);
        assertEquals(res.size(), 1);
        assertEquals(res.get(0).getQuizId(), 6);
        assertEquals(res.get(0).getScore(),30);

        res = results.getQuizResults(1);
        assertEquals(res.size(), 4);
        for(Result rs : res) {
            assertEquals(rs.getQuizId(), 1);
        }
        assertEquals(res.get(0).getScore(), 15);
        assertEquals(res.get(1).getScore(), 15);
        assertEquals(res.get(2).getScore(), 14);
        assertEquals(res.get(3).getScore(), 10);
    }
    //Testing that method GetUserResults correctly returns list of all result on one user
    @Test
    public void testUserResults() throws SQLException {
        ArrayList<Result> res = results.getUserResults(4);
        assertEquals(res.size(), 1);
        assertEquals(res.get(0).getUserId(), 4);
        assertEquals(res.get(0).getQuizId(), 6);
        assertEquals(res.get(0).getScore(), 30);

        res = results.getUserResults(2);
        assertEquals(res.size(), 1);
        assertEquals(res.get(0).getUserId(), 2);
        assertEquals(res.get(0).getQuizId(), 1);
        assertEquals(res.get(0).getScore(), 15);

        res = results.getUserResults(5);
        assertEquals(res.size(), 1);
        assertEquals(res.get(0).getUserId(), 5);
        assertEquals(res.get(0).getQuizId(), 1);
        assertEquals(res.get(0).getScore(), 14);

        res = results.getUserResults(1);
        assertEquals(res.size(), 3);
        for(Result rs : res) assertEquals(rs.getUserId(), 1);
        Result res1 = res.get(0);
        assertEquals(res1.getQuizId(),2);
        assertEquals(res1.getScore(), 30);
        Result res2 = res.get(1);
        assertEquals(res2.getQuizId(),1);
        assertEquals(res2.getScore(), 15);
        Result res3 = res.get(2);
        assertEquals(res3.getQuizId(),1);
        assertEquals(res3.getScore(), 10);
    }
}
