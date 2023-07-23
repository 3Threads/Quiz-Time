package Tests;

import DAO.DataSource;
import DAO.QuestionsDAO;
import DAO.QuizzesDAO;
import DAO.UsersDAO;
import Types.Quiz;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.testng.AssertJUnit.assertEquals;

public class QuestionsDAOTest {

    private static QuestionsDAO questions;
    private static QuizzesDAO quizzes;


    private static String TYPE1 = "Question Response";
    private String TYPE2 = "Fill in the blank";
    private String TYPE3 = "Picture Response";
    private String TYPE4 = "Multiple Choice";
    private String TYPE5 = "Multiple choice with multiple answers";
    private String TYPE6 = "Multi Answers";
    private String TYPE7 = "Matching";
    static int id;

    @BeforeAll
    public static void setup() {
        BasicDataSource dataSource = DataSource.getDataSource(true);

        UsersDAO uConnect;
        quizzes = new QuizzesDAO(dataSource);
        uConnect = new UsersDAO(dataSource);

        uConnect.addUser("123456", "1");

        quizzes.addQuiz("quiz1", "new quiz", 1);

        Quiz qz = quizzes.getQuizByName("quiz1");
        id = qz.getQuizId();

        questions = new QuestionsDAO(dataSource);
        ArrayList<String> arr = new ArrayList<>();
        arr.add(0, "200");
        questions.addQuestion("how much?", id, TYPE1, arr);

    }


    @Test
    public void testAddQuestion(){
        assertEquals(new ArrayList<>(List.of(new String[]{"1", "2", "3", "4", "how much"})), questions.getQuestionsIdByQuizId(id));
    }
}
