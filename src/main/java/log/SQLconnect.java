package log;

import java.sql.*;

public class SQLconnect {
    private final String server = "jdbc:mysql:// localhost:3306";
    private final String database = "QuizWebsite";
    private final String username = "root";
    private final String password = "password";
    private Connection connect;

    public SQLconnect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connect = DriverManager.getConnection
                    (server, username, password);
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void addUser(String username, String email, String password) throws SQLException {
        Statement stmt = connect.createStatement();
        stmt.execute("USE " + database);
        String query = "INSERT INTO USERS (USERNAME, EMAIL, PASSWORD) VALUES( ?, ?, ?)";
        PreparedStatement preparedStatement = connect.prepareStatement(query);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, email);
        preparedStatement.setString(3, HashPassword.stringToHash(password));
        preparedStatement.executeUpdate();
    }

    public boolean checkEmail(String email) throws SQLException {
        Statement stmt = connect.createStatement();
        stmt.execute("USE " + database);
        String found = "SELECT * FROM USERS WHERE EMAIL = '" + email + "';";
        ResultSet result = stmt.executeQuery(found);
        return result.next();
    }

    public boolean checkUser(String email, String password) throws SQLException {
        Statement stmt = connect.createStatement();
        stmt.execute("USE " + database);
        String found = "SELECT * FROM USERS WHERE EMAIL = '" + email + "';";
        ResultSet result = stmt.executeQuery(found);
        if (!result.next()) return false;
        String hs = HashPassword.stringToHash(password);
        String pass = result.getString("password");
        return pass.equals(hs);
    }
}
