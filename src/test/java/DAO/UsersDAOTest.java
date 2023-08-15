package DAO;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UsersDAOTest {

    private static UsersDAO usersDAO;
    private static BasicDataSource dataSource;
    private static final String[] TABLE_NAMES = new String[]{"USERS"};
    @BeforeAll
    public static void setup() {
        dataSource = DataSource.getDataSource(true);

        usersDAO = new UsersDAO(dataSource);
    }


    // check before and after adding
    @Test
    public void testFullCheck() {
        assertFalse(usersDAO.checkUser("2@2.com", "2"));
        usersDAO.addUser("2", "2");
        assertTrue(usersDAO.checkUser("2", "2"));
    }

    @Test
    public void testAddUser() {
        assertFalse(usersDAO.checkUser("1", "1"));
        assertTrue(usersDAO.addUser("1", "1"));
        assertTrue(usersDAO.checkUser("1", "1"));
    }

    @Test
    public void testDeleteUser(){
        assertFalse(usersDAO.checkUser("niko", "password"));
        assertTrue(usersDAO.addUser("niko", "password"));
        int userId = usersDAO.getUserId("niko");
        usersDAO.deleteUser(userId);
        assertFalse(usersDAO.checkUser("niko", "password"));
    }

    //    adding already existed user
    @Test
    public void testAlreadyExisted() {
        assertTrue(usersDAO.addUser("0", "0"));
        assertFalse(usersDAO.addUser("0", "0"));
    }

    @Test
    public void testMultipleAdds() {
        assertTrue(usersDAO.addUser("3", "3"));
        assertTrue(usersDAO.addUser("31", "31"));
        assertTrue(usersDAO.addUser("32", "32"));
        assertFalse(usersDAO.addUser("3", "3"));
        assertFalse(usersDAO.addUser("31", "31"));
        assertFalse(usersDAO.addUser("32", "32"));
        assertTrue(usersDAO.checkUser("3", "3"));
        assertTrue(usersDAO.checkUser("31", "31"));
        assertTrue(usersDAO.checkUser("32", "32"));
    }

    @Test
    public void testUpdateScore(){
        usersDAO.addUser("niko", "password");
        int userId = usersDAO.getUserId("niko");
        assertEquals(0, usersDAO.getUserById(userId).getScore());

        usersDAO.updateScore(userId, 100);
        assertEquals(100, usersDAO.getUserById(userId).getScore());

        usersDAO.updateScore(userId, 200);
        assertEquals(200, usersDAO.getUserById(userId).getScore());
    }

    @Test
    public void testGetAndDeleteAdmin(){
        usersDAO.addUser("niko", "password");
        int userId = usersDAO.getUserId("niko");
        assertFalse(usersDAO.getUserById(userId).isAdmin());

        usersDAO.getAdminToUser(userId);
        assertTrue(usersDAO.getUserById(userId).isAdmin());

        usersDAO.deleteAdminToUser(userId);
        assertFalse(usersDAO.getUserById(userId).isAdmin());
    }

    @Test
    public void testSearchUser(){
        assertTrue(usersDAO.searchUsers("niko").isEmpty());
        usersDAO.addUser("niko", "password");

        assertEquals("niko", usersDAO.searchUsers("niko").get(0).getUsername());

        usersDAO.addUser("niko1", "password");
        assertEquals("niko1", usersDAO.searchUsers("niko").get(1).getUsername());
    }


    @AfterEach
    public void finishUnitTest() {
        DataSource.clearTables(dataSource, new String[]{TABLE_NAMES[0]});
    }


    @AfterAll
    public static void finish() {
        DataSource.clearTables(dataSource, TABLE_NAMES);
    }

}