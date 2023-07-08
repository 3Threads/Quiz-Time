package FunctionalClasses;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLConnect {
    protected final String database = "QUIZWEBSITE";
    private final String server = "jdbc:mysql:// localhost:3306/" + database;
    private final String username = "root";
    private final String password = "password";
    protected Connection connect;

    public SQLConnect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connect = DriverManager.getConnection
                    (server, username, password);
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void clear(String tableName) throws SQLException {
        String query = "DELETE FROM " + tableName;
        Statement stmt = connect.createStatement();
        stmt.execute(query);
    }
}
