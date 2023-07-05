package log;

import java.sql.*;

public class SQLconnect {
    private String server = "jdbc:mysql:// localhost:3306";
    private String database = "users";
    private String username = "root";
    private String password = "1234";
    private HashPassword hash;
    private Connection connect;
    public SQLconnect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connect = DriverManager.getConnection
                    (server, username, password);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException sq) {
            sq.printStackTrace();
        }
    }
    public void addUser(String username, String email, String password) throws SQLException {
        Statement stmt = connect.createStatement();
        stmt.execute("USE users");
        String insertUser = "INSERT INTO usernamesAndPasswords (username," +
                "email, password) VALUES('" + username + "','" + email + "','" + hash.stringToHash(password) + "');";
        stmt.execute(insertUser);
    }
    public boolean checkEmail(String email) throws SQLException {
        Statement stmt = connect.createStatement();
        stmt.execute("USE users");
        String found = "SELECT * FROM usernamesandpasswords " +
                "where email = '" + email + "';";
        ResultSet result = stmt.executeQuery(found);
        return result.next();
    }
    public boolean checkUser(String email, String password) throws SQLException {
        Statement stmt = connect.createStatement();
        stmt.execute("USE users");
        String found = "SELECT * FROM usernamesandpasswords " +
                "where email = '" + email + "';";
        ResultSet result = stmt.executeQuery(found);
        if(!result.next()) return false;
        String hs = hash.stringToHash(password);
        String pass = result.getString("password");
        return pass.equals(hs);
    }
}
