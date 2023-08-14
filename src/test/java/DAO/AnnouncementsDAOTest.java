package DAO;

import Types.Announcement;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AnnouncementsDAOTest {

    private static AnnouncementsDAO announcementsDAO;
    private static BasicDataSource dataSource;
    private static final String[] TABLE_NAMES = new String[]{"ANNOUNCEMENTS", "USERS"};
    private static final int USERS_NUM = 3;
    private static final int[] ADMINS_ID = new int[USERS_NUM];

    @BeforeAll
    public static void setup() {
        dataSource = DataSource.getDataSource(true);

        announcementsDAO = new AnnouncementsDAO(dataSource);
        UsersDAO usersDAO = new UsersDAO(dataSource);

        for (int i = 1; i <= USERS_NUM; i++) {
            usersDAO.addUser("ADMIN" + i, "ADMIN");
            ADMINS_ID[i - 1] = usersDAO.getUserId("ADMIN" + i);
            usersDAO.getAdminToUser(ADMINS_ID[i - 1]);
        }
    }

    @Test
    void testGetAnnouncements() {
        announcementsDAO.addAnnouncement("title0", "body0", ADMINS_ID[0]);
        announcementsDAO.addAnnouncement("title1", "body1", ADMINS_ID[0]);
        announcementsDAO.addAnnouncement("title2", "body2", ADMINS_ID[0]);

        ArrayList<Announcement> announcements = announcementsDAO.getAnnouncements();
        for (int i = 0; i < announcements.size(); i++) {
            assertEquals("title" + i, announcements.get(i).getTitle());
            assertEquals("body" + i, announcements.get(i).getBody());
            assertEquals(ADMINS_ID[0], announcements.get(i).getWriterId());
        }

    }

    @Test
    void testAddAnnouncement() {
        ArrayList<Announcement> announcements = announcementsDAO.getAnnouncements();
        assertEquals(0, announcements.size());
        for (int i = 0; i < 25; i++) {
            announcementsDAO.addAnnouncement("title" + i, "body" + i, ADMINS_ID[0]);
        }
        announcements = announcementsDAO.getAnnouncements();
        assertEquals(25, announcements.size());
    }

    @Test
    void testSearchAnnouncementByUsername() {
        ArrayList<Announcement> announcements = announcementsDAO.searchAnnouncement("admin");
        assertEquals(0, announcements.size());
        announcementsDAO.addAnnouncement("title", "body", ADMINS_ID[0]);
        announcementsDAO.addAnnouncement("title", "body", ADMINS_ID[1]);

        announcements = announcementsDAO.searchAnnouncement("admin");
        assertEquals(2, announcements.size());

        announcements = announcementsDAO.searchAnnouncement("wrongUsername");
        assertEquals(0, announcements.size());
    }

    @Test
    void testSearchAnnouncementByTitle() {
        ArrayList<Announcement> announcements = announcementsDAO.searchAnnouncement("itl");
        assertEquals(0, announcements.size());
        announcementsDAO.addAnnouncement("title1", "body", ADMINS_ID[0]);
        announcementsDAO.addAnnouncement("title2", "body", ADMINS_ID[1]);
        announcementsDAO.addAnnouncement("head", "body", ADMINS_ID[1]);

        announcements = announcementsDAO.searchAnnouncement("itl");
        assertEquals(2, announcements.size());
        assertEquals(3, announcementsDAO.getAnnouncements().size());

        announcements = announcementsDAO.searchAnnouncement("sat");
        assertEquals(0, announcements.size());
    }

    @Test
    void testSearchAnnouncementByBothMatching() {
        ArrayList<Announcement> announcements = announcementsDAO.searchAnnouncement("MIN");
        assertEquals(0, announcements.size());
        announcementsDAO.addAnnouncement("minimal announcement", "body", ADMINS_ID[0]);

        announcements = announcementsDAO.searchAnnouncement("MIN");
        assertEquals(1, announcements.size());

        announcementsDAO.addAnnouncement("head", "body", ADMINS_ID[1]);
        announcements = announcementsDAO.searchAnnouncement("MIN");
        assertEquals(2, announcements.size());
        assertEquals(2, announcementsDAO.getAnnouncements().size());
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