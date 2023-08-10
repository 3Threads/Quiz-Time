package DAO;


import java.sql.*;
import java.util.ArrayList;

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
            String query = "INSERT INTO USERS VALUES(default, ?, default,  ?)";
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
            String found = "SELECT * FROM USERS WHERE BINARY USERNAME = ?;";
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
                return new User(resultSet.getInt("ID"), resultSet.getInt("SCORE"), resultSet.getString("USERNAME"), resultSet.getInt("STATUS"));
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

    public void getAdminToUser(int userId) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String getUserRow = "UPDATE USERS SET STATUS = 1 WHERE ID = ?;";
            PreparedStatement statement = connect.prepareStatement(getUserRow);
            statement.setInt(1, userId);
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
    public void updateScore(int userId, int newScore) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String getUserRow = "UPDATE USERS SET SCORE = ? WHERE ID = ?;";
            PreparedStatement statement = connect.prepareStatement(getUserRow);
            statement.setInt(1, newScore);
            statement.setInt(2, userId);
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
    public ArrayList<User> getHighestScoreUsers(int limit) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String getUserRow = "SELECT * FROM USERS ORDER BY SCORE DESC LIMIT ?";
            PreparedStatement statement = connect.prepareStatement(getUserRow);
            statement.setInt(1,limit);
            ResultSet result = statement.executeQuery();
            ArrayList<User> arr = new ArrayList<>();
            while(result.next()) {
                arr.add(new User(result.getInt("ID"), result.getInt("SCORE"), result.getString("USERNAME"), result.getInt("STATUS")));
            }
            return arr;
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
    public void deleteAdminToUser(int userId) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String getUserRow = "UPDATE USERS SET STATUS = 0 WHERE ID = ?;";
            PreparedStatement statement = connect.prepareStatement(getUserRow);
            statement.setInt(1, userId);
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
    public void deleteUser(int userId) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String getUserRow = "DELETE FROM USERS WHERE ID = ?;";
            PreparedStatement statement = connect.prepareStatement(getUserRow);
            statement.setInt(1, userId);
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
    public ArrayList<User> searchUsers(String userName) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String getUserRow = "SELECT * FROM USERS WHERE USERNAME LIKE ?;";
            PreparedStatement statement = connect.prepareStatement(getUserRow);
            statement.setString(1, "%"+userName+"%");
            ResultSet result = statement.executeQuery();
            ArrayList<User> arr = new ArrayList<>();
            while(result.next()) {
                arr.add(new User(result.getInt("ID"), result.getInt("SCORE"), result.getString("USERNAME"), result.getInt("STATUS")));
            }
            return arr;
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
