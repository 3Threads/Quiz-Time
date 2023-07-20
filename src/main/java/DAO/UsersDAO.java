package DAO;


import java.sql.*;

import BusinessLogic.HashPassword;
import Types.User;
import org.apache.commons.dbcp2.BasicDataSource;

public class UsersDAO {
    private final BasicDataSource dataSource;

    public UsersDAO(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Boolean addUser(String username, String password) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String query = "INSERT INTO USERS (USERNAME,  PASSWORD) VALUES( ?,  ?)";
            PreparedStatement statement = connect.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, HashPassword.stringToHash(password));
            statement.executeUpdate();
        } catch (SQLException e) {
            return false;
        } finally {
            if (connect != null) {
                try {
                    connect.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }


    public boolean checkUser(String username, String password) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String found = "SELECT * FROM USERS WHERE USERNAME = ?;";
            PreparedStatement statement = connect.prepareStatement(found);
            statement.setString(1, username);
            ResultSet result = statement.executeQuery();
            if (!result.next()) return false;
            String hs = HashPassword.stringToHash(password);
            String pass = result.getString("password");
            return pass.equals(hs);
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
        return false;
    }

    public int getUserId(String username) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String getUserRow = "SELECT * FROM USERS WHERE USERNAME = ?;";
            PreparedStatement statement = connect.prepareStatement(getUserRow);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("ID");
            } else return -1;
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
        return -1;
    }

    public User getUserById(int id) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String getUserRow = "SELECT * FROM USERS WHERE ID = ?;";
            PreparedStatement statement = connect.prepareStatement(getUserRow);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new User(resultSet.getInt("ID"), resultSet.getString("USERNAME"));
            }
            return null;
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
}
