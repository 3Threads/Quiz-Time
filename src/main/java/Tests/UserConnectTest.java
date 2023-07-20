package Tests;

import FunctionalClasses.DataSource;
import FunctionalClasses.UserConnect;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserConnectTest {

    private static UserConnect connect;

    @BeforeAll
    public static void setup() {
        BasicDataSource dataSource = DataSource.getDataSource(true);

        connect = new UserConnect(dataSource);
    }


    // check before and after adding
    @Test
    public void testFullCheck() {
        assertFalse(connect.checkUser("2@2.com", "2"));
        connect.addUser("2", "2");
        assertTrue(connect.checkUser("2", "2"));
    }

    @Test
    public void testAddUser() {
        assertTrue(connect.addUser("1", "1"));
        assertTrue(connect.checkUser("1", "1"));
    }

    //    adding already existed user
    @Test
    public void testAlreadyExisted() {
        assertTrue(connect.addUser("0", "0"));
        assertFalse(connect.addUser("0", "0"));
    }

    @Test
    public void testMultipleAdds() {
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