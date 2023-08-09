package Tests;

import DAO.ChallengesDAO;
import DAO.DataSource;
import DAO.QuizzesDAO;
import DAO.UsersDAO;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Time;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChallengesDaoTest {
    private static ChallengesDAO challengesDao;

    @BeforeAll
    public static void init() {
        BasicDataSource dataSource = DataSource.getDataSource(true);
        UsersDAO usersDAO;
        QuizzesDAO quizzesDAO;
        challengesDao = new ChallengesDAO(dataSource);
        usersDAO = new UsersDAO(dataSource);
        quizzesDAO = new QuizzesDAO(dataSource);
        usersDAO.addUser("1", "1");
        usersDAO.addUser("2", "2");
        usersDAO.addUser("3", "3");
        usersDAO.addUser("4", "4");
        quizzesDAO.addQuiz("1", "1", 1,  new Time(0, 2, 3), "Sports,History");
        quizzesDAO.addQuiz("11", "11", 1,  new Time(0, 2, 3), "Sports,History");
        quizzesDAO.addQuiz("111", "111", 1,  new Time(0, 2, 3), "Sports,History");
        quizzesDAO.addQuiz("2", "2", 2,  new Time(0, 2, 3), "Sports,History");
        quizzesDAO.addQuiz("3", "3", 3,  new Time(0, 2, 3), "Sports,History");
        quizzesDAO.addQuiz("33", "33", 3,  new Time(0, 2, 3), "Sports,History");
        challengesDao.sendChallenge(1, 2, 4);
        challengesDao.sendChallenge(2, 3, 5);
        challengesDao.sendChallenge(3, 4, 6);
        challengesDao.sendChallenge(4, 1, 1);
        challengesDao.sendChallenge(3, 2, 2);
        challengesDao.sendChallenge(1, 3, 3);
    }

    // Checks functions and ordering of challenges
    @Test
    public void testSendChallenges() {
        assertEquals(4, challengesDao.getChallenges(2).get(0).getQuizId());
        assertEquals(2, challengesDao.getChallenges(2).get(1).getQuizId());
        assertEquals(6, challengesDao.getChallenges(4).get(0).getQuizId());
        assertEquals(5, challengesDao.getChallenges(3).get(0).getQuizId());
        assertEquals(3, challengesDao.getChallenges(3).get(1).getQuizId());
    }
    @Test
    public void testHaveChallenged() {
        assertEquals(true, challengesDao.alreadyChallenged(1, 2, 4));
        assertEquals(false, challengesDao.alreadyChallenged(1, 2, 2));

        assertEquals(false, challengesDao.alreadyChallenged(1, 1, 4));
        assertEquals(true, challengesDao.alreadyChallenged(4, 1, 1));
    }
    @Test
    public void testDeleteChallenge() {
        assertEquals(true, challengesDao.alreadyChallenged(1, 2, 4));
        challengesDao.removeChallenge(1,2,4);
        assertEquals(false, challengesDao.alreadyChallenged(1, 2, 4));
        challengesDao.sendChallenge(1, 2, 4);

        assertEquals(true, challengesDao.alreadyChallenged(4, 1, 1));
        challengesDao.removeChallenge(4,1,1);
        assertEquals(false, challengesDao.alreadyChallenged(4, 1, 1));
        challengesDao.sendChallenge(4,1,1);
    }

}
