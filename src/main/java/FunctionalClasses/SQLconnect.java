package FunctionalClasses;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLconnect {
    private final String server = "jdbc:mysql:// localhost:3306";
    protected final String database = "QUIZWEBSITE";
    private final String username = "root";
    private final String password = "password";
    protected Connection connect;

    public SQLconnect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connect = DriverManager.getConnection
                    (server, username, password);
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }
    public void clear(String tableName) throws SQLException {
        String query = "DELETE FROM "+ tableName;
        Statement stmt = connect.createStatement();
        stmt.execute(query);
    }
}
