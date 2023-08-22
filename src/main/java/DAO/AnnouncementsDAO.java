package DAO;

import Types.Announcement;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class AnnouncementsDAO {

    private final BasicDataSource dataSource;


    /**
     * Constructor for AnnouncementsDAO.
     * Initializes the data source used for database connectivity.
     *
     * @param dataSource The BasicDataSource object used for database connections.
     */
    public AnnouncementsDAO(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }


    /**
     * Retrieves a list of Announcement objects from the database, ordered by write time.
     * This method establishes a database connection and queries the database to retrieve
     * Announcement objects. The announcements are retrieved in descending order of WRITE_TIME.
     * The retrieved announcements are populated into an ArrayList and returned.
     * If any SQLException occurs during the database operations, the exception is caught,
     * and the stack trace is printed. The database connection is properly closed in the finally block
     * to ensure resource release, even if an exception occurs. If an exception occurs during the process,
     * the method returns null.
     *
     * @return An ArrayList of Announcement objects retrieved from the database, or null if an exception occurs.
     */
    public ArrayList<Announcement> getAnnouncements() {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            ArrayList<Announcement> announcements = new ArrayList<>();
            String foundAnnouncements = "SELECT * FROM ANNOUNCEMENTS ORDER BY WRITE_TIME DESC";
            PreparedStatement statement = connect.prepareStatement(foundAnnouncements);
            return getAnnouncements(announcements, statement);
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


    /**
     * Adds a new announcement to the database.
     * This method establishes a database connection and inserts a new announcement
     * with the provided title, body, and writer ID. The announcement details are
     * set as parameters in a prepared statement and executed for insertion.
     * If any SQLException occurs during the database operations, the exception is caught,
     * and the stack trace is printed. The database connection is properly closed in the finally block
     * to ensure resource release, even if an exception occurs.
     *
     * @param title    The title of the new announcement.
     * @param body     The body content of the new announcement.
     * @param writerId The ID of the writer associated with the new announcement.
     */
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


    /**
     * Searches for announcements in the database based on a search string.
     * This method establishes a database connection and queries the database
     * to retrieve Announcement objects that match the provided search string.
     * The search is performed based on matching titles or writer usernames.
     * The retrieved announcements are populated into an ArrayList and returned.
     * If any SQLException occurs during the database operations, the exception is caught,
     * and the stack trace is printed. The database connection is properly closed in the finally block
     * to ensure resource release, even if an exception occurs. If an exception occurs during the process,
     * the method returns null.
     *
     * @param searchString The search string used to match announcement titles or writer usernames.
     * @return An ArrayList of Announcement objects retrieved from the database, or null if an exception occurs.
     */
    public ArrayList<Announcement> searchAnnouncement(String searchString) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            ArrayList<Announcement> announcements = new ArrayList<>();
            String foundAnnouncements = "SELECT * FROM ANNOUNCEMENTS WHERE TITLE LIKE ? OR (SELECT USERNAME FROM USERS WHERE ID = WRITER_ID) LIKE ?;";
            PreparedStatement statement = connect.prepareStatement(foundAnnouncements);
            statement.setString(1, "%" + searchString + "%");
            statement.setString(2, "%" + searchString + "%");
            return getAnnouncements(announcements, statement);
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


    /**
     * Helper method to populate a list of Announcement objects from a ResultSet.
     * This method takes an ArrayList of Announcement objects and a PreparedStatement
     * containing a database query as parameters. It executes the query and processes
     * the resulting ResultSet to create Announcement objects with retrieved data.
     * For each row in the ResultSet, an Announcement object is created and added to the list.
     * The populated ArrayList is returned after processing all rows.
     *
     * @param announcements An ArrayList of Announcement objects to be populated.
     * @param statement     A PreparedStatement containing a database query.
     * @return The populated ArrayList containing Announcement objects.
     * @throws SQLException If any SQLException occurs during database operations.
     */
    private ArrayList<Announcement> getAnnouncements(ArrayList<Announcement> announcements, PreparedStatement statement) throws SQLException {
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
    }

}
