package DAO;

import java.sql.*;
import java.util.ArrayList;

import BusinessLogic.HashPassword;
import Types.User;
import org.apache.commons.dbcp2.BasicDataSource;

public class UsersDAO {

    private final BasicDataSource dataSource;


    /**
     * Constructs a UsersDAO object with the provided data source.
     *
     * @param dataSource The BasicDataSource used to establish database connections.
     */
    public UsersDAO(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }


    /**
     * Adds a new user to the database with the provided username and hashed password.
     *
     * @param username The username of the new user.
     * @param password The password of the new user, which will be hashed before storage.
     * @return `true` if the user was successfully added, `false` if an SQLException occurred during the process.
     */
    public Boolean addUser(String username, String password) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String query = "INSERT INTO USERS VALUES(default, ?, default, default,?)";
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


    /**
     * Checks whether a user with the given username and password exists in the database.
     *
     * @param username The username of the user to check.
     * @param password The password of the user to check.
     * @return `true` if the user exists and the provided password matches the stored password, `false` otherwise.
     */
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


    /**
     * Retrieves the user ID associated with a given username from the database.
     *
     * @param username The username of the user.
     * @return The user ID if the username is found, otherwise returns -1.
     */
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


    /**
     * Retrieves user information from the database based on the provided user ID.
     *
     * @param id The ID of the user.
     * @return A User object containing user information if the user is found, otherwise returns null.
     */
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


    /**
     * Changes the status of a user to admin based on the provided user ID.
     * This method updates the user's status in the database to indicate they have admin privileges.
     *
     * @param userId The ID of the user whose status will be changed to admin.
     */
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


    /**
     * Updates the score of a user with the provided user ID.
     * This method modifies the user's score in the database to the new specified score.
     *
     * @param userId The ID of the user whose score will be updated.
     * @param newScore The new score to set for the user.
     */
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


    /**
     * Removes administrative privileges from a user with the provided user ID.
     * This method updates the user's status in the database to remove admin privileges.
     *
     * @param userId The ID of the user whose admin privileges will be removed.
     */
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


    /**
     * Deletes a user's information from the database based on the provided user ID.
     * This method removes all data related to the specified user, including their scores, status, and other details.
     *
     * @param userId The ID of the user to be deleted.
     */
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


    /**
     * Searches for users in the database whose usernames match the provided search string.
     * This method retrieves user information based on a partial username match and returns a list of User objects.
     *
     * @param userName The partial or complete username to search for.
     * @return An ArrayList containing User objects whose usernames match the provided search string.
     */
    public ArrayList<User> searchUsers(String userName) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String getUserRow = "SELECT * FROM USERS WHERE USERNAME LIKE ?;";
            PreparedStatement statement = connect.prepareStatement(getUserRow);
            statement.setString(1, "%" + userName + "%");
            ResultSet result = statement.executeQuery();
            ArrayList<User> arr = new ArrayList<>();
            while (result.next()) {
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
