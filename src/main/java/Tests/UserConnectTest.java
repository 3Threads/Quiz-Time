package Tests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import FunctionalClasses.UserConnect;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

public class UserConnectTest {

    private static UserConnect connect;

    @BeforeAll
    public static void setUp() {
        connect = new UserConnect("USERSTEST");
        connect.addUser("0", "0");
    }

    @Test
    public void testAddUser() throws SQLException {
        connect.addUser("1", "1");
        assertTrue(connect.checkUser("1", "1"));

    }

    // check before and after adding
    @Test
    public void testFullCheck() throws SQLException {
        assertFalse(connect.checkUser("2@2.com", "2"));
        connect.addUser("2", "2");
        assertTrue(connect.checkUser("2", "2"));
    }


    //    adding already existed user
    @Test
    public void testAlreadyExisted() {
        assertFalse(connect.addUser("0", "0"));
    }

    @Test
    public void testMultipleAdds() throws SQLException {
        assertTrue(connect.addUser("3", "3"));
        assertTrue(connect.addUser("31", "31"));
        assertTrue(connect.addUser("32", "32"));
        assertFalse(connect.addUser("3", "3"));
        assertFalse(connect.addUser("31", "31"));
        assertFalse(connect.addUser("32", "32"));
        assertTrue(connect.checkUser("3", "3"));
        assertTrue(connect.checkUser("31", "31"));
        assertTrue(connect.checkUser("32", "32"));

    }


}
