package DAO;


import java.sql.*;

import BusinessLogic.HashPassword;
import Types.User;
import org.apache.commons.dbcp2.BasicDataSource;

public class UsersDAO {
    private final String tableName = "USERS";
    private final BasicDataSource dataSource;

    public UsersDAO(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Boolean addUser(String username, String password) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String query = "INSERT INTO " + tableName + " (USERNAME,  PASSWORD) VALUES( ?,  ?)";
            PreparedStatement preparedStatement = connect.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, HashPassword.stringToHash(password));
            preparedStatement.executeUpdate();
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
            Statement stmt = connect.createStatement();
            String found = "SELECT * FROM " + tableName + " WHERE USERNAME = '" + username + "';";
            ResultSet result = stmt.executeQuery(found);
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
            Statement stmt = connect.createStatement();
            String getUserRow = "SELECT * FROM " + tableName + " WHERE USERNAME = '" + username + "';";
            ResultSet resultSet = stmt.executeQuery(getUserRow);
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
            Statement stmt = connect.createStatement();
            String getUserRow = "SELECT * FROM " + tableName + " WHERE ID = '" + id + "';";
            ResultSet resultSet = stmt.executeQuery(getUserRow);
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
