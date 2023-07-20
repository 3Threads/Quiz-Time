package Tests;

import DAO.ChallengesDao;
import DAO.DataSource;
import DAO.QuizzesDAO;
import DAO.UsersDAO;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChallengesDaoTest {
    private static ChallengesDao challengesDao;

    @BeforeAll
    public static void init() {
        BasicDataSource dataSource = DataSource.getDataSource(true);
        UsersDAO usersDAO;
        QuizzesDAO quizzesDAO;
        challengesDao = new ChallengesDao(dataSource);
        usersDAO = new UsersDAO(dataSource);
        quizzesDAO = new QuizzesDAO(dataSource);
        usersDAO.addUser("1", "1");
        usersDAO.addUser("2", "2");
        usersDAO.addUser("3", "3");
        usersDAO.addUser("4", "4");
        quizzesDAO.addQuiz("1", "1", 1, "1");
        quizzesDAO.addQuiz("11", "11", 1, "1, 2");
        quizzesDAO.addQuiz("111", "111", 1, "1, 2, 3");
        quizzesDAO.addQuiz("2", "2", 2, "1, 2, 3");
        quizzesDAO.addQuiz("3", "3", 3, "1, 2");
        quizzesDAO.addQuiz("33", "33", 3, "1, 2, 3, 4, 5, 6");

    }

    // Checks functions and ordering of challenges
    @Test
    public void testSendChallenges() {
        challengesDao.sendChallenge(4, 1, 4);
        assertEquals(4, challengesDao.getChallenges(1).get(0).getQuizId());
        challengesDao.sendChallenge(4, 1, 5);
        challengesDao.sendChallenge(4, 1, 6);
        challengesDao.sendChallenge(4, 2, 1);
        challengesDao.sendChallenge(4, 2, 2);
        challengesDao.sendChallenge(4, 2, 3);
        assertEquals(5, challengesDao.getChallenges(1).get(1).getQuizId());
        assertEquals(6, challengesDao.getChallenges(1).get(2).getQuizId());
        assertEquals(1, challengesDao.getChallenges(2).get(0).getQuizId());
        assertEquals(2, challengesDao.getChallenges(2).get(1).getQuizId());
        assertEquals(3, challengesDao.getChallenges(2).get(2).getQuizId());
    }

    // Checks functions and ordering of challenges. If 2 people sent same quiz challenge to someone it means he got 2 different challenges.
    @Test
    public void testSendChallenges2() {
        challengesDao.sendChallenge(1, 4, 6);
        challengesDao.sendChallenge(3, 4, 2);
        challengesDao.sendChallenge(2, 4, 3);
        challengesDao.sendChallenge(2, 4, 3);
        assertEquals(4, challengesDao.getChallenges(4).size());
        challengesDao.sendChallenge(4, 3, 5);
        assertEquals(5, challengesDao.getChallenges(3).get(0).getQuizId());
    }
}
