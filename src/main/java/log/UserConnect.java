package log;

import java.sql.*;

public class UserConnect {
    private final String server = "jdbc:mysql:// localhost:3306";
    private final String database = "QUIZWEBSITE";
    private final String username = "root";
    private final String password = "password";
    private Connection connect;
    private final String tableName;

    public UserConnect(String tableName) {
        this.tableName = tableName.toUpperCase();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connect = DriverManager.getConnection
                    (server, username, password);
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Boolean addUser(String username, String email, String password) {
        try {
            Statement stmt = connect.createStatement();
            stmt.execute("USE " + database);
            String query = "INSERT INTO "+tableName+" (USERNAME, EMAIL, PASSWORD) VALUES( ?, ?, ?)";
            PreparedStatement preparedStatement = connect.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, HashPassword.stringToHash(password));
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            return false;
        }
        return true;
    }

    public boolean checkEmail(String email) throws SQLException {
        Statement stmt = connect.createStatement();
        stmt.execute("USE " + database);
        String found = "SELECT * FROM "+tableName+" WHERE EMAIL = '" + email + "';";
        ResultSet result = stmt.executeQuery(found);
        return result.next();
    }

    public boolean checkUser(String email, String password) throws SQLException {
        Statement stmt = connect.createStatement();
        stmt.execute("USE " + database);
        String found = "SELECT * FROM "+tableName+" WHERE EMAIL = '" + email + "';";
        ResultSet result = stmt.executeQuery(found);
        if (!result.next()) return false;
        String hs = HashPassword.stringToHash(password);
        String pass = result.getString("password");
        return pass.equals(hs);
    }
}