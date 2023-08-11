package DAO;

import Types.Announcement;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AnnouncementsDAOTest {

    private static AnnouncementsDAO announcementsDAO;

    @BeforeAll
    public static void setup() {
        BasicDataSource dataSource = DataSource.getDataSource(true);

        UsersDAO uConnect;
        announcementsDAO = new AnnouncementsDAO(dataSource);
        uConnect = new UsersDAO(dataSource);

        uConnect.addUser("ADMIN1", "ADMIN");
        uConnect.addUser("ADMIN2", "ADMIN");
        uConnect.addUser("ADMIN3", "ADMIN");
        uConnect.getAdminToUser(1);
        uConnect.getAdminToUser(2);
        uConnect.getAdminToUser(3);
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
}