package FunctionalClasses;

import java.sql.*;

public class UserConnect extends SQLConnect {
    private final String tableName = "USERS";

    public UserConnect(boolean isTesting) {
        super(isTesting);
    }

    public Boolean addUser(String username, String password) {
        try {
            String query = "INSERT INTO " + tableName + " (USERNAME,  PASSWORD) VALUES( ?,  ?)";
            PreparedStatement preparedStatement = connect.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, HashPassword.stringToHash(password));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }


    public boolean checkUser(String username, String password) throws SQLException {
        Statement stmt = connect.createStatement();
        String found = "SELECT * FROM " + tableName + " WHERE USERNAME = '" + username + "';";
        ResultSet result = stmt.executeQuery(found);
        if (!result.next()) return false;
        String hs = HashPassword.stringToHash(password);
        String pass = result.getString("password");
        return pass.equals(hs);
    }

    public int getUserId(String username) throws SQLException {
        Statement stmt = connect.createStatement();
        String getUserRow = "SELECT * FROM " + tableName + " WHERE USERNAME = '" + username + "';";
        ResultSet resultSet = stmt.executeQuery(getUserRow);
        if (resultSet.next()) {
            return resultSet.getInt("ID");
        } else return 0;
    }
}
