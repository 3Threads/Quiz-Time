package Tests;
import DAO.DataSource;
import DAO.QuizzesDAO;
import DAO.RatingsDAO;
import DAO.UsersDAO;
import Types.Rating;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Time;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
public class RatingsDAOTest {
    private static RatingsDAO ratings;
    @BeforeAll
    public static void setup() {
        BasicDataSource dataSource = DataSource.getDataSource(true);

        UsersDAO uConnect;
        QuizzesDAO quizzes;
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

        ratings.addRatingToQuiz(1, 1, 2, "notBad");
        ratings.addRatingToQuiz(2, 1, 4, "well");
        ratings.addRatingToQuiz(3, 2, 1, "awful");
        ratings.addRatingToQuiz(1, 2, 5, "excellent");
        ratings.addRatingToQuiz(4, 5, 4, "good");
        ratings.addRatingToQuiz(5, 1, 2, "bad");
        ratings.addRatingToQuiz(1, 3, 3, "norm");
    }

    @Test
    public void testGetQuizRatings() {
        ArrayList<Rating> ratesNew = ratings.getQuizRatings(1, "newest");
        assertEquals(ratesNew.size(), 3);
        assertEquals(ratesNew.get(0).getRating(), 2);
        assertEquals(ratesNew.get(0).getComment(), "notBad");
        assertEquals(ratesNew.get(1).getRating(), 4);
        assertEquals(ratesNew.get(1).getComment(), "well");
        assertEquals(ratesNew.get(2).getRating(), 2);
        assertEquals(ratesNew.get(2).getComment(), "bad");

        ArrayList<Rating> ratesOld = ratings.getQuizRatings(1, "oldest");
        assertEquals(ratesOld.size(), 3);
        assertEquals(ratesOld.get(2).getRating(), 2);
        assertEquals(ratesOld.get(2).getComment(), "bad");
        assertEquals(ratesOld.get(1).getRating(), 4);
        assertEquals(ratesOld.get(1).getComment(), "well");
        assertEquals(ratesOld.get(0).getRating(), 2);
        assertEquals(ratesOld.get(0).getComment(), "notBad");

        ArrayList<Rating> rates1 = ratings.getQuizRatings(5, "oldest");
        assertEquals(rates1.size(), 1);
        assertEquals(rates1.get(0).getRating(), 4);
        assertEquals(rates1.get(0).getComment(), "good");

        ArrayList<Rating> rates2 = ratings.getQuizRatings(3, "newest");
        assertEquals(rates2.size(), 1);
        assertEquals(rates2.get(0).getRating(), 3);
        assertEquals(rates2.get(0).getComment(), "norm");
    }
    @Test
    public void testAvgRating() {
        int rate1 = ratings.getAvgRatingOfQuiz(1);
        assertEquals(rate1, 3);

        int rate2 = ratings.getAvgRatingOfQuiz(2);
        assertEquals(rate2, 3);

        int rate3 = ratings.getAvgRatingOfQuiz(5);
        assertEquals(rate3, 4);

        int rate4 = ratings.getAvgRatingOfQuiz(3);
        assertEquals(rate4, 3);

        int rate5 = ratings.getAvgRatingOfQuiz(4);
        assertEquals(rate5, 0);
    }

    @Test
    public void testHaveAlreadyRated() {
        assertEquals(true, ratings.haveAlreadyRated(1, 1));
        assertEquals(false, ratings.haveAlreadyRated(1, 5));
        assertEquals(true, ratings.haveAlreadyRated(2, 1));
        assertEquals(false, ratings.haveAlreadyRated(2, 3));
        assertEquals(true, ratings.haveAlreadyRated(5, 1));
        assertEquals(false, ratings.haveAlreadyRated(4, 1));
    }
    @Test
    public void testDeleteRating() {
        assertEquals(true, ratings.haveAlreadyRated(1, 1));
        ratings.deleteRating(1,1);
        assertEquals(false, ratings.haveAlreadyRated(1, 1));
        ratings.addRatingToQuiz(1, 1, 2, "notBad");

        assertEquals(true, ratings.haveAlreadyRated(2, 1));
        ratings.deleteRating(2,1);
        assertEquals(false, ratings.haveAlreadyRated(2, 1));
        ratings.addRatingToQuiz(2, 1, 4, "well");

        assertEquals(true, ratings.haveAlreadyRated(5, 1));
        ratings.deleteRating(5,1);
        assertEquals(false, ratings.haveAlreadyRated(5, 1));
        ratings.addRatingToQuiz(5, 1, 2, "bad");
    }
}
