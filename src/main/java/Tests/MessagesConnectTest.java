package Tests;

import static org.junit.jupiter.api.Assertions.*;

import FunctionalClasses.MessagesConnect;
import FunctionalClasses.UserConnect;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Text;

import java.sql.SQLException;

public class MessagesConnectTest {
    private static MessagesConnect mConnect;
    private static UserConnect uConnect;

    @BeforeAll
    public static void setup() {
        mConnect = new MessagesConnect(true);
        uConnect = new UserConnect(true);
        uConnect.addUser("1", "1");
        uConnect.addUser("2", "2");
        uConnect.addUser("3", "1");
        uConnect.addUser("4", "2");
        uConnect.addUser("5", "1");
        uConnect.addUser("6", "2");
        uConnect.addUser("7", "2");
        uConnect.addUser("8", "2");
        uConnect.addUser("9", "2");
        uConnect.addUser("10", "2");

    }


    //simple 1 message test
    @Test
    public void test1() throws SQLException {

        mConnect.sendMessage(1, 2, "ggg");
        assertEquals("ggg", mConnect.getMessagesWith(1, 2).get(0).getMessage());
    }


    //incorrect message sent
    @Test
    public void test2() throws SQLException {

        mConnect.sendMessage(3, 4, "g");
        assertNotEquals("ggg", mConnect.getMessagesWith(3, 4).get(0).getMessage());
    }


    //2 messages sent from 1-->2 and from 2-->1
    @Test
    public void test3() throws SQLException {

        mConnect.sendMessage(5,6, "1");
        mConnect.sendMessage(6, 5, "2");
        assertEquals("1", mConnect.getMessagesWith(5,6).get(0).getMessage());
        assertEquals("2", mConnect.getMessagesWith(5,6).get(1).getMessage());
    }

    //same test as "test4" but sent 2--->1 and then 1-->2 (beforeEach) ******************
    @Test
    public void test4() throws SQLException{
        mConnect.sendMessage(6,5, "1");
        mConnect.sendMessage(5, 6, "2");
        assertEquals("1", mConnect.getMessagesWith(5,6).get(0).getMessage());
        assertEquals("2", mConnect.getMessagesWith(5,6).get(1).getMessage());
    }


    //Mixed messages
    @Test
    public void test6() throws SQLException{
        mConnect.sendMessage(7, 9, "79");
        mConnect.sendMessage(7, 10, "710");
        mConnect.sendMessage(10, 7, "107");
        mConnect.sendMessage(9, 8, "98");
        mConnect.sendMessage(8, 10, "810");
        assertEquals(2, mConnect.getMessagesWith(7, 10).size());
        assertEquals(1, mConnect.getMessagesWith(8, 9).size());
        assertEquals(0, mConnect.getMessagesWith(8, 7).size());
        assertEquals("79", mConnect.getMessagesWith(7,9).get(0).getMessage());
        assertEquals("710", mConnect.getMessagesWith(7,10).get(0).getMessage());
        assertEquals("107", mConnect.getMessagesWith(7,10).get(1).getMessage());
        assertEquals("98", mConnect.getMessagesWith(8,9).get(0).getMessage());
        assertEquals("98", mConnect.getMessagesWith(9,8).get(0).getMessage());
        assertEquals("810", mConnect.getMessagesWith(8,10).get(0).getMessage());

    }

}
