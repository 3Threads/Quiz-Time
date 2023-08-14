package DAO;

import Types.Announcement;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AnnouncementsDAOTest {

    private static AnnouncementsDAO announcementsDAO;
    private static BasicDataSource dataSource;
    private static final String[] tableNames = new String[]{"USERS", "ANNOUNCEMENTS"};

    @BeforeAll
    public static void setup() {
        dataSource = DataSource.getDataSource(true);

        announcementsDAO = new AnnouncementsDAO(dataSource);
        UsersDAO usersDAO = new UsersDAO(dataSource);

        usersDAO.addUser("ADMIN1", "ADMIN");
        usersDAO.addUser("ADMIN2", "ADMIN");
        usersDAO.addUser("ADMIN3", "ADMIN");
        usersDAO.getAdminToUser(1);
        usersDAO.getAdminToUser(2);
        usersDAO.getAdminToUser(3);
    }

    @Test
    void testGetAnnouncements() {
        announcementsDAO.addAnnouncement("title0", "body0", 1);
        announcementsDAO.addAnnouncement("title1", "body1", 2);
        announcementsDAO.addAnnouncement("title2", "body2", 3);

        ArrayList<Announcement> announcements = announcementsDAO.getAnnouncements();
        for (int i = 0; i < announcements.size(); i++) {
            assertEquals("title" + i, announcements.get(i).getTitle());
            assertEquals("body" + i, announcements.get(i).getBody());
            assertEquals(i + 1, announcements.get(i).getWriterId());
        }

    }

    @Test
    void testAddAnnouncement() {
        ArrayList<Announcement> announcements = announcementsDAO.getAnnouncements();
        assertEquals(3, announcements.size());
        for (int i = 0; i < 25; i++) {
            announcementsDAO.addAnnouncement("title" + i, "body" + i, 1);
        }
        announcements = announcementsDAO.getAnnouncements();
        assertEquals(28, announcements.size());
    }

    @Test
    void searchAnnouncement() {
    }

    @AfterAll
    public static void finish() throws SQLException {
        DataSource.clearTables(dataSource, tableNames);
    }
}