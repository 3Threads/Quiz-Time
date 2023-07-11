package Tests;

import FunctionalClasses.ChallengesConnect;
import FunctionalClasses.QuizesConnect;
import FunctionalClasses.UserConnect;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class ChallengesConnectTest {
    private static ChallengesConnect challengesConnect;

    @BeforeAll
    public static void init() throws SQLException {
        UserConnect userConnect;
        QuizesConnect quizesConnect;
        challengesConnect = new ChallengesConnect(true);
        userConnect = new UserConnect(true);
        quizesConnect = new QuizesConnect(true);
        userConnect.addUser("1", "1");
        userConnect.addUser("2", "2");
        userConnect.addUser("3", "3");
        userConnect.addUser("4", "4");
        quizesConnect.addQuiz("1", "1", 1, "1");
        quizesConnect.addQuiz("11", "11", 1, "1, 2");
        quizesConnect.addQuiz("111", "111", 1, "1, 2, 3");
        quizesConnect.addQuiz("2", "2", 2, "1, 2, 3");
        quizesConnect.addQuiz("3", "3", 3, "1, 2");
        quizesConnect.addQuiz("33", "33", 3, "1, 2, 3, 4, 5, 6");

    }

    // Checks functions and ordering of challenges
    @Test
    public void testSendChallenges() throws SQLException {
        challengesConnect.sendChallenge(4, 1, 4);
        assertEquals(4, challengesConnect.getChallenges(1).get(0).getQuizId());
        challengesConnect.sendChallenge(4, 1, 5);
        challengesConnect.sendChallenge(4, 1, 6);
        challengesConnect.sendChallenge(4, 2, 1);
        challengesConnect.sendChallenge(4, 2, 2);
        challengesConnect.sendChallenge(4, 2, 3);
        assertEquals(5, challengesConnect.getChallenges(1).get(1).getQuizId());
        assertEquals(6, challengesConnect.getChallenges(1).get(2).getQuizId());
        assertEquals(1, challengesConnect.getChallenges(2).get(0).getQuizId());
        assertEquals(2, challengesConnect.getChallenges(2).get(1).getQuizId());
        assertEquals(3, challengesConnect.getChallenges(2).get(2).getQuizId());
    }

    // Checks functions and ordering of challenges. If 2 people sent same quiz challenge to someone it means he got 2 different challenges.
    @Test
    public void testSendChallenges2() throws SQLException {
        challengesConnect.sendChallenge(1, 4, 6);
        challengesConnect.sendChallenge(3, 4, 2);
        challengesConnect.sendChallenge(2, 4, 3);
        challengesConnect.sendChallenge(2, 4, 3);
        assertEquals(4, challengesConnect.getChallenges(4).size());
        challengesConnect.sendChallenge(4, 3, 5);
        assertEquals(5, challengesConnect.getChallenges(3).get(0).getQuizId());
    }
}
