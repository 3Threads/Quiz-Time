package DAO;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ChallengesDAOTest {
    private static ChallengesDAO challengesDao;
    private static BasicDataSource dataSource;
    private static final String[] TABLE_NAMES = new String[]{"CHALLENGES", "USERS", "QUIZZES"};
    private static final int USERS_NUM = 5;
    private static final int[] USERS_ID = new int[USERS_NUM];
    private static final int QUIZZES_NUM = 6;
    private static final int[] QUIZZES_ID = new int[QUIZZES_NUM];

    /*
        Creating users and quizzes
     */
    @BeforeAll
    public static void init() {
        dataSource = DataSource.getDataSource(true);

        challengesDao = new ChallengesDAO(dataSource);
        UsersDAO usersDAO = new UsersDAO(dataSource);
        QuizzesDAO quizzesDAO = new QuizzesDAO(dataSource);

        for (int i = 1; i <= USERS_NUM; i++) {
            usersDAO.addUser("USER" + i, "password");
            USERS_ID[i - 1] = usersDAO.getUserId("USER" + i);
        }

        for (int i = 1; i <= QUIZZES_NUM; i++) {
            quizzesDAO.addQuiz("QUIZ" + i, "new quiz", USERS_ID[0], null, "Sports");
            QUIZZES_ID[i - 1] = quizzesDAO.getQuizByName("QUIZ" + i).getQuizId();
        }


//        challengesDao.sendChallenge(1, 2, 4);
//        challengesDao.sendChallenge(2, 3, 5);
//        challengesDao.sendChallenge(3, 4, 6);
//        challengesDao.sendChallenge(4, 1, 1);
//        challengesDao.sendChallenge(3, 2, 2);
//        challengesDao.sendChallenge(1, 3, 3);
    }

    /*
        Checks functions and ordering of challenges
     */
    @Test
    public void testSendChallenges() {
        challengesDao.sendChallenge(USERS_ID[0], USERS_ID[1], QUIZZES_ID[3]);
        assertEquals(QUIZZES_ID[3], challengesDao.getChallenges(USERS_ID[1]).get(0).getQuizId());

        challengesDao.sendChallenge(USERS_ID[2], USERS_ID[1], QUIZZES_ID[4]);
        assertEquals(QUIZZES_ID[4], challengesDao.getChallenges(USERS_ID[1]).get(1).getQuizId());

        challengesDao.sendChallenge(USERS_ID[3], USERS_ID[2], QUIZZES_ID[4]);
        assertEquals(QUIZZES_ID[4], challengesDao.getChallenges(USERS_ID[2]).get(0).getQuizId());

        challengesDao.sendChallenge(USERS_ID[2], USERS_ID[3], QUIZZES_ID[4]);
        assertEquals(QUIZZES_ID[4], challengesDao.getChallenges(USERS_ID[3]).get(0).getQuizId());

        challengesDao.sendChallenge(USERS_ID[2], USERS_ID[3], QUIZZES_ID[3]);
        assertEquals(QUIZZES_ID[3], challengesDao.getChallenges(USERS_ID[3]).get(1).getQuizId());

        challengesDao.sendChallenge(USERS_ID[2], USERS_ID[3], QUIZZES_ID[2]);
        assertEquals(QUIZZES_ID[2], challengesDao.getChallenges(USERS_ID[3]).get(2).getQuizId());
    }

    @Test
    public void testHaveChallenged() {
        challengesDao.sendChallenge(USERS_ID[4], USERS_ID[1], QUIZZES_ID[3]);
        assertTrue(challengesDao.alreadyChallenged(USERS_ID[4], USERS_ID[1], QUIZZES_ID[3]));
        assertFalse(challengesDao.alreadyChallenged(USERS_ID[4], USERS_ID[1], QUIZZES_ID[2]));
        assertFalse(challengesDao.alreadyChallenged(USERS_ID[2], USERS_ID[1], QUIZZES_ID[3]));

        assertFalse(challengesDao.alreadyChallenged(USERS_ID[0], USERS_ID[2], QUIZZES_ID[0]));
        challengesDao.sendChallenge(USERS_ID[0], USERS_ID[2], QUIZZES_ID[0]);
        assertTrue(challengesDao.alreadyChallenged(USERS_ID[0], USERS_ID[2], QUIZZES_ID[0]));
    }

    @Test
    public void testRemoveChallenge() {
        challengesDao.sendChallenge(USERS_ID[4], USERS_ID[1], QUIZZES_ID[3]);
        assertTrue(challengesDao.alreadyChallenged(USERS_ID[4], USERS_ID[1], QUIZZES_ID[3]));
        challengesDao.removeChallenge(USERS_ID[4], USERS_ID[1], QUIZZES_ID[3]);
        assertFalse(challengesDao.alreadyChallenged(USERS_ID[4], USERS_ID[1], QUIZZES_ID[3]));

        assertFalse(challengesDao.alreadyChallenged(USERS_ID[0], USERS_ID[2], QUIZZES_ID[0]));
        challengesDao.sendChallenge(USERS_ID[0], USERS_ID[2], QUIZZES_ID[0]);
        assertTrue(challengesDao.alreadyChallenged(USERS_ID[0], USERS_ID[2], QUIZZES_ID[0]));
        challengesDao.removeChallenge(USERS_ID[0], USERS_ID[2], QUIZZES_ID[0]);
        assertFalse(challengesDao.alreadyChallenged(USERS_ID[0], USERS_ID[2], QUIZZES_ID[0]));
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
