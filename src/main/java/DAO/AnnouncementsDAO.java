package DAO;

import Types.Announcement;
import Types.Challenge;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class AnnouncementsDAO {
    private final BasicDataSource dataSource;

    public AnnouncementsDAO(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public ArrayList<Announcement> getAnnouncements() {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            ArrayList<Announcement> announcements = new ArrayList<>();
            String foundAnnouncements = "SELECT * FROM ANNOUNCEMENTS ORDER BY WRITE_TIME DESC";
            PreparedStatement statement = connect.prepareStatement(foundAnnouncements);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                int id = result.getInt("ID");
                String title = result.getString("TITLE");
                String body = result.getString("BODY");
                int writerId = result.getInt("WRITER_ID");
                Date writeTime = result.getDate("WRITE_TIME");
                Announcement announcement = new Announcement(id, title, body, writerId, writeTime);
                announcements.add(announcement);
            }
            return announcements;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connect != null) {
                try {
                    connect.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public void addAnnouncement(String title, String body, int writerId) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String postAnnouncement = "INSERT INTO ANNOUNCEMENTS (TITLE, BODY, WRITER_ID) VALUES(?, ?, ?);";
            PreparedStatement statement = connect.prepareStatement(postAnnouncement);
            statement.setString(1, title);
            statement.setString(2, body);
            statement.setInt(3, writerId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connect != null) {
                try {
                    connect.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
