package Tests;

import FunctionalClasses.UserConnect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserConnectTest {

    private static UserConnect connect;

    @BeforeEach
    public void setUp() throws SQLException {
        String tableName = "USERSTEST";
        connect = new UserConnect(tableName);
        connect.clear(tableName);
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
        assertTrue(connect.addUser("0", "0"));
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