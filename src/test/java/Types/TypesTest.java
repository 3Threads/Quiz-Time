package Types;

import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("deprecation")
public class TypesTest {

    @Test
    public void testMessageType() {
        Message msg1 = new Message(466, 447, "1");

        assertEquals(466, msg1.getFrom());
        assertEquals(447, msg1.getTo());
        assertEquals("1", msg1.getMessage());

        Message msg2 = new Message(466, 447, "2");
        assertEquals(466, msg2.getFrom());
        assertEquals(447, msg2.getTo());
        assertEquals("2", msg2.getMessage());

        Message msg3 = new Message(447, 466, "4");
        assertEquals(447, msg3.getFrom());
        assertEquals(466, msg3.getTo());
        assertEquals("4", msg3.getMessage());
    }

    @Test
    public void testUserType() {
        User user1 = new User(1, 50, "niko", 0);

        assertEquals(1, user1.getId());
        assertEquals(50, user1.getScore());
        assertEquals("niko", user1.getUsername());
        assertFalse(user1.isAdmin());

        User user2 = new User(2, 100, "akaki", 1);

        assertEquals(2, user2.getId());
        assertEquals(100, user2.getScore());
        assertEquals("akaki", user2.getUsername());
        assertTrue(user2.isAdmin());
    }

    @Test
    public void testResultType() {
        Date date1 = new Date(2023, 7, 22);
        Result res1 = new Result(1, 1, 10, 12L, date1);

        assertEquals(1, res1.getUserId());
        assertEquals(1, res1.getQuizId());
        assertEquals(10, res1.getScore());
        assertEquals(12L, res1.getSpentTime());
        assertEquals(date1, res1.getWrittenDate());

        Date date2 = new Date(2017, 11, 27);
        Result res2 = new Result(12321123, 920001231, 0, 120L, date2);

        assertEquals(12321123, res2.getUserId());
        assertEquals(920001231, res2.getQuizId());
        assertEquals(0, res2.getScore());
        assertEquals(120L, res2.getSpentTime());
        assertEquals(date2, res2.getWrittenDate());
    }

    @Test
    public void testFriendInfoType() {
        FriendInfo friend1 = new FriendInfo(10, 11, 1);
        FriendInfo friend2 = new FriendInfo(11, 10, 1);
        FriendInfo friend3 = new FriendInfo(12, 11, 1);
        FriendInfo friend4 = new FriendInfo(13, 10, 0);

        assertEquals(10, friend1.getUser1Id());
        assertEquals(11, friend1.getUser2Id());
        assertEquals(1, friend1.getAccepted());

        assertEquals(11, friend2.getUser1Id());
        assertEquals(10, friend2.getUser2Id());
        assertEquals(1, friend2.getAccepted());

        assertEquals(12, friend3.getUser1Id());
        assertEquals(11, friend3.getUser2Id());
        assertEquals(1, friend3.getAccepted());

        assertEquals(13, friend4.getUser1Id());
        assertEquals(10, friend4.getUser2Id());
        assertEquals(0, friend4.getAccepted());
    }

    @Test
    public void testChallengeType() {
        Challenge chal1 = new Challenge(1, 2);
        Challenge chal2 = new Challenge(5, 2);
        Challenge chal3 = new Challenge(7, 3);

        assertEquals(1, chal1.getUserId());
        assertEquals(2, chal1.getQuizId());

        assertEquals(5, chal2.getUserId());
        assertEquals(2, chal2.getQuizId());

        assertEquals(7, chal3.getUserId());
        assertEquals(3, chal3.getQuizId());
    }

    @Test
    public void testQuizType() {
        Time qz1TimeLimit = new Time(1, 20, 20);
        int qz1ID = 1000;
        String qz1Name = "IQ TEST";
        String qz1Desc = "IT IS HARD ONE";
        Date qz1Date = new Date(2000, 10, 9);
        int qz1CreatorID = 12345;
        int qz1Completed = 21;
        ArrayList<String> qz1Categories = new ArrayList<>(List.of(new String[]{"Sports"}));

        Quiz qz1 = new Quiz(qz1TimeLimit, qz1ID, qz1Name, qz1Desc, qz1Date, qz1CreatorID, qz1Completed, "Sports");

        assertEquals(qz1TimeLimit, qz1.getTimeLimit());
        assertEquals(qz1ID, qz1.getQuizId());
        assertEquals(qz1Name, qz1.getQuizName());
        assertEquals(qz1Desc, qz1.getQuizDescription());
        assertEquals(qz1Date, qz1.getCreationTime());
        assertEquals(qz1CreatorID, qz1.getCreatorID());
        assertEquals(qz1Categories, qz1.getCategories());
    }

    @Test
    public void testAnnouncementType(){
        int id = 10;
        String title = "NEW QUIZ ANNOUNCED";
        String body = "Some description";
        int writerId = 25;
        Date writeTime = new Date(2023, 4, 17);

        Announcement announcement = new Announcement(id, title, body, writerId, writeTime);

        assertEquals(id, announcement.getId());
        assertEquals(title, announcement.getTitle());
        assertEquals(body, announcement.getBody());
        assertEquals(writerId, announcement.getWriterId());
        assertEquals(writeTime, announcement.getWriteTime());
    }

    @Test
    public void testRating(){
        int userId = 10;
        int rate = 300;
        String comment = "GOOD QUIZ";
        Date ratingsDate = new Date(2023, 5,20);

        Rating rating = new Rating(userId, rate, comment, ratingsDate);

        assertEquals(userId, rating.getUserId());
        assertEquals(rate, rating.getRating());
        assertEquals(comment, rating.getComment());
        assertEquals(ratingsDate, rating.getRatingsDate());
    }
}
