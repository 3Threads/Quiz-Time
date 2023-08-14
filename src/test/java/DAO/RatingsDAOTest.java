package DAO;

import Types.Rating;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Time;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
public class RatingsDAOTest {
    private static RatingsDAO ratings;
    private static UsersDAO uConnect;
    private static QuizzesDAO quizzes;

    private static BasicDataSource dataSource;
    private static final String[] TABLE_NAMES = new String[]{"RATINGS", "QUIZZES", "USERS"};
    private static final int[] USERS = new int[5];
    @BeforeAll
    public static void setup() {
        dataSource = DataSource.getDataSource(true);

        quizzes = new QuizzesDAO(dataSource);
        uConnect = new UsersDAO(dataSource);
        ratings = new RatingsDAO(dataSource);
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
        ratings.addRatingToQuiz(USERS[0], quizzes.getQuizByName("quiz1").getQuizId(), 2, "notBad");
        ratings.addRatingToQuiz(USERS[1], quizzes.getQuizByName("quiz1").getQuizId(), 4, "well");
        ratings.addRatingToQuiz(USERS[2], quizzes.getQuizByName("quiz2").getQuizId(), 1, "aa");
        ratings.addRatingToQuiz(USERS[3], quizzes.getQuizByName("quiz5").getQuizId(), 4, "good");
        ratings.addRatingToQuiz(USERS[4], quizzes.getQuizByName("quiz1").getQuizId(), 2, "bad");
        ratings.addRatingToQuiz(USERS[0], quizzes.getQuizByName("quiz3").getQuizId(), 3, "norm");
    }

    @Test
    public void testGetQuizRatings() {
        ArrayList<Rating> ratesNew = ratings.getQuizRatings(quizzes.getQuizByName("quiz1").getQuizId(), "oldest");
        assertEquals(ratesNew.size(), 3);
        assertEquals(ratesNew.get(0).getRating(), 2);
        assertEquals(ratesNew.get(0).getComment(), "notBad");
        assertEquals(ratesNew.get(1).getRating(), 4);
        assertEquals(ratesNew.get(1).getComment(), "well");
        assertEquals(ratesNew.get(2).getRating(), 2);
        assertEquals(ratesNew.get(2).getComment(), "bad");

        ArrayList<Rating> ratesOld = ratings.getQuizRatings(quizzes.getQuizByName("quiz1").getQuizId(), "newest");
        assertEquals(ratesOld.size(), 3);
        assertEquals(ratesOld.get(0).getRating(), 2);
        assertEquals(ratesOld.get(0).getComment(), "bad");
        assertEquals(ratesOld.get(1).getRating(), 4);
        assertEquals(ratesOld.get(1).getComment(), "well");
        assertEquals(ratesOld.get(2).getRating(), 2);
        assertEquals(ratesOld.get(2).getComment(), "notBad");

        ArrayList<Rating> rates1 = ratings.getQuizRatings(quizzes.getQuizByName("quiz5").getQuizId(), "oldest");
        assertEquals(rates1.size(), 1);
        assertEquals(rates1.get(0).getRating(), 4);
        assertEquals(rates1.get(0).getComment(), "good");

        ArrayList<Rating> rates2 = ratings.getQuizRatings(quizzes.getQuizByName("quiz3").getQuizId(), "newest");
        assertEquals(rates2.size(), 1);
        assertEquals(rates2.get(0).getRating(), 3);
        assertEquals(rates2.get(0).getComment(), "norm");
    }
    @Test
    public void testAvgRating() {
        int rate1 = ratings.getAvgRatingOfQuiz(quizzes.getQuizByName("quiz1").getQuizId());
        assertEquals(rate1, 3);

        int rate3 = ratings.getAvgRatingOfQuiz(quizzes.getQuizByName("quiz5").getQuizId());
        assertEquals(rate3, 4);

        int rate4 = ratings.getAvgRatingOfQuiz(quizzes.getQuizByName("quiz3").getQuizId());
        assertEquals(rate4, 3);

        int rate5 = ratings.getAvgRatingOfQuiz(quizzes.getQuizByName("quiz4").getQuizId());
        assertEquals(rate5, 0);
    }

    @Test
    public void testHaveAlreadyRated() {
        assertEquals(true, ratings.haveAlreadyRated(USERS[0], quizzes.getQuizByName("quiz1").getQuizId()));
        assertEquals(true, ratings.haveAlreadyRated(USERS[0], quizzes.getQuizByName("quiz3").getQuizId()));
        assertEquals(true, ratings.haveAlreadyRated(USERS[1], quizzes.getQuizByName("quiz1").getQuizId()));
        assertEquals(false, ratings.haveAlreadyRated(USERS[1], quizzes.getQuizByName("quiz3").getQuizId()));
        assertEquals(true, ratings.haveAlreadyRated(USERS[4], quizzes.getQuizByName("quiz1").getQuizId()));
        assertEquals(false, ratings.haveAlreadyRated(USERS[3], quizzes.getQuizByName("quiz1").getQuizId()));
    }
    @Test
    public void testDeleteRating() {
        ratings.addRatingToQuiz(USERS[0], quizzes.getQuizByName("quiz4").getQuizId(), 3,"aa");
        assertEquals(true, ratings.haveAlreadyRated(USERS[0], quizzes.getQuizByName("quiz4").getQuizId()));
        ratings.deleteRating(USERS[0], quizzes.getQuizByName("quiz4").getQuizId());
        assertEquals(false, ratings.haveAlreadyRated(USERS[0], quizzes.getQuizByName("quiz4").getQuizId()));

        ratings.addRatingToQuiz(USERS[1], quizzes.getQuizByName("quiz3").getQuizId(), 2,"aa");
        assertEquals(true, ratings.haveAlreadyRated(USERS[1], quizzes.getQuizByName("quiz3").getQuizId()));
        ratings.deleteRating(USERS[1], quizzes.getQuizByName("quiz3").getQuizId());
        assertEquals(false, ratings.haveAlreadyRated(USERS[1], quizzes.getQuizByName("quiz3").getQuizId()));
    }

    @AfterAll
    public static void finish() {
        DataSource.clearTables(dataSource, TABLE_NAMES);
    }
}
