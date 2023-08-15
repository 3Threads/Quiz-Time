package DAO;//package Tests;

import Types.Result;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResultsDAOTest {

    private static ResultsDAO resultsDAO;
    private static FriendsDAO friendsDAO;
    private static BasicDataSource dataSource;
    private static final String[] TABLE_NAMES = new String[]{"COMPLETED_QUIZZES", "USERS", "QUIZZES", "FRIENDS"};
    private static final int USERS_NUM = 5;
    private static final int[] USERS_ID = new int[USERS_NUM];
    private static final int QUIZZES_NUM = 6;
    private static final int[] QUIZ_ID = new int[QUIZZES_NUM];

    /*
        Creating users and quizzes
     */
    @BeforeAll
    public static void setup() {
        dataSource = DataSource.getDataSource(true);

        resultsDAO = new ResultsDAO(dataSource);
        UsersDAO usersDAO = new UsersDAO(dataSource);
        QuizzesDAO quizzesDAO = new QuizzesDAO(dataSource);
        friendsDAO = new FriendsDAO(dataSource);

        for (int i = 1; i <= USERS_NUM; i++) {
            usersDAO.addUser("USER" + i, "password");
            USERS_ID[i - 1] = usersDAO.getUserId("USER" + i);
        }

        for (int i = 1; i <= 6; i++) {
            quizzesDAO.addQuiz("QZ" + i, "new quiz", USERS_ID[0], null, "Sports");
            QUIZ_ID[i - 1] = quizzesDAO.getQuizByName("QZ" + i).getQuizId();
        }

    }

    /*
        Testing that addResult method works correctly by using getUserResultsOnQuiz method
     */
    @Test
    public void testAddResult() {
        resultsDAO.addResult(USERS_ID[2], QUIZ_ID[3], 10, 2000);
        ArrayList<Result> res = resultsDAO.getUserResultsOnQuiz(USERS_ID[2], QUIZ_ID[3]);
        assertEquals(1, res.size());
        assertEquals(10, res.get(0).getScore());

        resultsDAO.addResult(USERS_ID[3], QUIZ_ID[5], 30, 3000);
        res = resultsDAO.getUserResultsOnQuiz(USERS_ID[3], QUIZ_ID[5]);
        assertEquals(1, res.size());
        assertEquals(30, res.get(0).getScore());

        resultsDAO.addResult(USERS_ID[4], QUIZ_ID[0], 14, 500);
        res = resultsDAO.getUserResultsOnQuiz(USERS_ID[4], QUIZ_ID[0]);
        assertEquals(1, res.size());
        assertEquals(14, res.get(0).getScore());

        resultsDAO.addResult(USERS_ID[0], QUIZ_ID[0], 10, 1000);
        resultsDAO.addResult(USERS_ID[0], QUIZ_ID[0], 15, 800);
        res = resultsDAO.getUserResultsOnQuiz(USERS_ID[0], QUIZ_ID[0]);
        assertEquals(2, res.size());
        assertEquals(15, res.get(0).getScore());
        assertEquals(10, res.get(1).getScore());
    }

    /*
        Testing getQuizResults and check if every return result is about correct quiz
     */
    @Test
    public void testGetQuizResults() {
        resultsDAO.addResult(USERS_ID[0], QUIZ_ID[1], 40, 900);
        ArrayList<Result> res = resultsDAO.getQuizResults(QUIZ_ID[1]);
        assertEquals(1, res.size());
        assertEquals(QUIZ_ID[1], res.get(0).getQuizId());
        assertEquals(USERS_ID[0], res.get(0).getUserId());
        assertEquals(40, res.get(0).getScore());
        assertEquals(900, res.get(0).getSpentTime());

        resultsDAO.addResult(USERS_ID[3], QUIZ_ID[5], 50, 120);
        res = resultsDAO.getQuizResults(QUIZ_ID[5]);
        assertEquals(1, res.size());
        assertEquals(QUIZ_ID[5], res.get(0).getQuizId());
        assertEquals(USERS_ID[3], res.get(0).getUserId());
        assertEquals(50, res.get(0).getScore());
        assertEquals(120, res.get(0).getSpentTime());
    }


    /*
        Method getQuizResults() sorts results by scores.
     */
    @Test
    public void testGerQuizResultsHard() {
        for (int i = 0; i < USERS_NUM; i++) {
            resultsDAO.addResult(USERS_ID[i], QUIZ_ID[0], 100 * (USERS_NUM - i), 1000 * (USERS_NUM - i));
        }

        ArrayList<Result> res = resultsDAO.getQuizResults(QUIZ_ID[0]);
        assertEquals(USERS_NUM, res.size());
        for (int i = 0; i < USERS_NUM; i++) {
            assertEquals(QUIZ_ID[0], res.get(i).getQuizId());
            assertEquals(USERS_ID[i], res.get(i).getUserId());
            assertEquals(100 * (USERS_NUM - i), res.get(i).getScore());
            assertEquals(1000 * (USERS_NUM - i), res.get(i).getSpentTime());
        }
    }

    /*
        Testing getUserResults and check if every return result is about one user
     */
    @Test
    public void testUserResults() {
        resultsDAO.addResult(USERS_ID[3], QUIZ_ID[5], 50, 120);
        resultsDAO.addResult(USERS_ID[3], QUIZ_ID[2], 100, 1200);

        ArrayList<Result> res = resultsDAO.getUserResults(USERS_ID[3]);
        assertEquals(res.size(), 2);

        assertEquals(USERS_ID[3], res.get(0).getUserId());
        assertEquals(QUIZ_ID[2], res.get(0).getQuizId());
        assertEquals(100, res.get(0).getScore());
        assertEquals(1200, res.get(0).getSpentTime());

        assertEquals(USERS_ID[3], res.get(1).getUserId());
        assertEquals(QUIZ_ID[5], res.get(1).getQuizId());
        assertEquals(50, res.get(1).getScore());
        assertEquals(120, res.get(1).getSpentTime());


        /*
            If score is same it should be sorted by spentTime
         */
        resultsDAO.addResult(USERS_ID[0], QUIZ_ID[5], 100, 1000);
        resultsDAO.addResult(USERS_ID[0], QUIZ_ID[2], 100, 2000);
        resultsDAO.addResult(USERS_ID[0], QUIZ_ID[3], 100, 3000);

        res = resultsDAO.getUserResults(USERS_ID[0]);
        assertEquals(res.size(), 3);

        assertEquals(USERS_ID[0], res.get(0).getUserId());
        assertEquals(QUIZ_ID[5], res.get(0).getQuizId());
        assertEquals(100, res.get(0).getScore());
        assertEquals(1000, res.get(0).getSpentTime());

        assertEquals(USERS_ID[0], res.get(1).getUserId());
        assertEquals(QUIZ_ID[2], res.get(1).getQuizId());
        assertEquals(100, res.get(1).getScore());
        assertEquals(2000, res.get(1).getSpentTime());

        assertEquals(USERS_ID[0], res.get(2).getUserId());
        assertEquals(QUIZ_ID[3], res.get(2).getQuizId());
        assertEquals(100, res.get(2).getScore());
        assertEquals(3000, res.get(2).getSpentTime());
    }


    /*
        test getting all friends results on specific quiz
     */
    @Test
    public void testFriendsResults(){
        friendsDAO.sendFriendRequest(USERS_ID[0], USERS_ID[1]);
        friendsDAO.acceptRequest(USERS_ID[1], USERS_ID[0]);
        friendsDAO.sendFriendRequest(USERS_ID[0], USERS_ID[2]);
        friendsDAO.acceptRequest(USERS_ID[2], USERS_ID[0]);

        resultsDAO.addResult(USERS_ID[1], QUIZ_ID[0], 100, 1000);
        resultsDAO.addResult(USERS_ID[1], QUIZ_ID[1], 300, 2000);
        resultsDAO.addResult(USERS_ID[2], QUIZ_ID[1], 200, 3000);

        ArrayList<Result> res = resultsDAO.getUserFriendsResultOnQuiz(USERS_ID[0], QUIZ_ID[0]);

        assertEquals(1, res.size());
        assertEquals(USERS_ID[1], res.get(0).getUserId());
        assertEquals(QUIZ_ID[0], res.get(0).getQuizId());
        assertEquals(100, res.get(0).getScore());
        assertEquals(1000, res.get(0).getSpentTime());


        res = resultsDAO.getUserFriendsResultOnQuiz(USERS_ID[0], QUIZ_ID[1]);

        assertEquals(2, res.size());
        assertEquals(USERS_ID[1], res.get(0).getUserId());
        assertEquals(QUIZ_ID[1], res.get(0).getQuizId());
        assertEquals(300, res.get(0).getScore());
        assertEquals(2000, res.get(0).getSpentTime());

        assertEquals(USERS_ID[2], res.get(1).getUserId());
        assertEquals(QUIZ_ID[1], res.get(1).getQuizId());
        assertEquals(200, res.get(1).getScore());
        assertEquals(3000, res.get(1).getSpentTime());
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
