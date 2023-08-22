package DAO;

import java.sql.*;
import java.util.ArrayList;

import Types.FriendInfo;
import org.apache.commons.dbcp2.BasicDataSource;

public class FriendsDAO {

    private final BasicDataSource dataSource;


    /**
     * Constructor for FriendsDAO.
     * Initializes the data source used for database connectivity.
     *
     * @param dataSource The BasicDataSource object used for database connections.
     */
    public FriendsDAO(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }


    /**
     * Retrieves a list of user IDs representing pending friend requests for the specified user.
     * This method establishes a database connection using the provided data source,
     * and queries the FRIENDS table to find user IDs (USER1_ID) where the provided user's ID (USER2_ID)
     * matches and the request is not yet accepted (ACCEPTED = 0). The retrieved user IDs are added to an ArrayList.
     * If any SQLException occurs during the database operations, the exception is caught,
     * and the stack trace is printed. The database connection is properly closed in the finally block
     * to ensure resource release, even if an exception occurs.
     *
     * @param userID The ID of the user for whom friend requests are being retrieved.
     * @return An ArrayList of user IDs representing pending friend requests, or null if an exception occurs.
     */
    public ArrayList<Integer> getFriendsRequests(int userID) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            ArrayList<Integer> requests = new ArrayList<>();
            String foundRequests = "SELECT USER1_ID FROM FRIENDS WHERE USER2_ID = ? AND ACCEPTED = 0;";
            PreparedStatement statement = connect.prepareStatement(foundRequests);
            statement.setInt(1, userID);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                int usID = result.getInt("USER1_ID");
                requests.add(usID);
            }
            return requests;
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
     * Retrieves friend-related information between two users.
     * This method establishes a database connection using the provided data source,
     * and queries the FRIENDS table to retrieve information about the friendship status
     * between two users. The query checks for records where either (USER1_ID = user1 AND USER2_ID = user2)
     * or (USER1_ID = user2 AND USER2_ID = user1). The retrieved information is used to create a FriendInfo object.
     * If no records are found, a default FriendInfo object with negative acceptance status is returned.
     * If any SQLException occurs during the database operations, the exception is caught,
     * and the stack trace is printed. The database connection is properly closed in the finally block
     * to ensure resource release, even if an exception occurs.
     *
     * @param user1 The ID of the first user.
     * @param user2 The ID of the second user.
     * @return A FriendInfo object containing friendship information, or null if an exception occurs.
     */
    public FriendInfo getBetweenUsersInfo(int user1, int user2) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String getInfo = "SELECT * FROM FRIENDS WHERE (USER1_ID = ? AND USER2_ID = ?) OR (USER1_ID = ? AND USER2_ID = ?);";
            PreparedStatement statement = connect.prepareStatement(getInfo);
            statement.setInt(1, user1);
            statement.setInt(2, user2);
            statement.setInt(3, user2);
            statement.setInt(4, user1);
            ResultSet result = statement.executeQuery();
            if (!result.next()) return new FriendInfo(user1, user2, -1);
            return new FriendInfo(result.getInt("USER1_ID"), result.getInt("USER2_ID"),
                    result.getInt("ACCEPTED"));
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
     * Retrieves a list of user IDs representing friends for the specified user.
     * This method establishes a database connection using the provided data source,
     * and queries the FRIENDS table to find user IDs (USER1_ID) where the provided user's ID (USER2_ID)
     * matches and the friendship is accepted (ACCEPTED = 1). The retrieved user IDs are added to an ArrayList.
     * The query is performed in two parts using UNION to account for both directions of friendship.
     * If any SQLException occurs during the database operations, the exception is caught,
     * and the stack trace is printed. The database connection is properly closed in the finally block
     * to ensure resource release, even if an exception occurs.
     *
     * @param userID The ID of the user for whom friends are being retrieved.
     * @return An ArrayList of user IDs representing friends, or null if an exception occurs.
     */
    public ArrayList<Integer> getFriendsList(int userID) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            ArrayList<Integer> friends = new ArrayList<>();
            String foundFriends = "SELECT USER1_ID FROM FRIENDS WHERE USER2_ID = ? AND ACCEPTED = 1 union " +
                    " SELECT USER2_ID FROM FRIENDS WHERE USER1_ID = ? AND ACCEPTED = 1";
            PreparedStatement statement = connect.prepareStatement(foundFriends);
            statement.setInt(1, userID);
            statement.setInt(2, userID);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                int usID = result.getInt("USER1_ID");
                friends.add(usID);
            }
            return friends;
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
     * Sends a friend request from one user to another.
     * This method establishes a database connection using the provided data source.
     * It checks if there is already a pending friend request from the sender to the receiver,
     * and if so, it automatically accepts the request by calling the acceptRequest method.
     * If the sender and receiver are already friends, the method returns.
     * Otherwise, it inserts a new record into the FRIENDS table representing the friend request.
     * If any SQLException occurs during the database operations, the exception is caught,
     * and the stack trace is printed. The database connection is properly closed in the finally block
     * to ensure resource release, even if an exception occurs.
     *
     * @param fromUserId The ID of the user sending the friend request.
     * @param toUserId   The ID of the user receiving the friend request.
     */
    public void sendFriendRequest(int fromUserId, int toUserId) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            if (getFriendsRequests(fromUserId).contains(toUserId)) {
                acceptRequest(fromUserId, toUserId);
                return;
            }
            if (getFriendsList(fromUserId).contains(toUserId)) return;
            String sendRequest = "INSERT INTO FRIENDS (USER1_ID, USER2_ID) VALUES(?,?);";
            PreparedStatement statement = connect.prepareStatement(sendRequest);
            statement.setInt(1, fromUserId);
            statement.setInt(2, toUserId);
            statement.execute();
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
     * Accepts a pending friend request between two users.
     * This method establishes a database connection using the provided data source.
     * It updates the ACCEPTED field in the FRIENDS table to 1 for the specified users,
     * marking their friendship as accepted. The update is based on the user IDs in both directions.
     * If any SQLException occurs during the database operations, the exception is caught,
     * and the stack trace is printed. The database connection is properly closed in the finally block
     * to ensure resource release, even if an exception occurs.
     *
     * @param curUserId   The ID of the user who initiated the friend request.
     * @param newFriendId The ID of the user accepting the friend request.
     */
    public void acceptRequest(int curUserId, int newFriendId) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String acceptFriend = "UPDATE FRIENDS SET ACCEPTED = 1 WHERE USER1_ID = ? AND USER2_ID = ?;";
            PreparedStatement statement = connect.prepareStatement(acceptFriend);
            statement.setInt(1, newFriendId);
            statement.setInt(2, curUserId);
            statement.execute();
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
     * Rejects or cancels a pending friend request between two users.
     * This method establishes a database connection using the provided data source.
     * It deletes the corresponding record from the FRIENDS table based on the specified user IDs,
     * effectively rejecting or canceling the friend request. The deletion is performed in both directions.
     * If any SQLException occurs during the database operations, the exception is caught,
     * and the stack trace is printed. The database connection is properly closed in the finally block
     * to ensure resource release, even if an exception occurs.
     *
     * @param curUserId      The ID of the user who initiated the friend request or wants to cancel the request.
     * @param notNewFriendId The ID of the user who was the target of the request or is being canceled.
     */
    public void rejectRequest(int curUserId, int notNewFriendId) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String acceptFriend = "DELETE FROM FRIENDS WHERE  USER1_ID = ? AND USER2_ID = ?;";
            PreparedStatement statement = connect.prepareStatement(acceptFriend);
            statement.setInt(1, notNewFriendId);
            statement.setInt(2, curUserId);
            statement.execute();
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
     * Deletes the friendship between two users.
     * This method establishes a database connection using the provided data source.
     * It deletes the corresponding record from the FRIENDS table based on the specified user IDs,
     * effectively ending the friendship. The deletion is performed in both directions.
     * If any SQLException occurs during the database operations, the exception is caught,
     * and the stack trace is printed. The database connection is properly closed in the finally block
     * to ensure resource release, even if an exception occurs.
     *
     * @param curUserId The ID of the user initiating the friendship deletion.
     * @param notFriend The ID of the user who is being removed from the friend list.
     */
    public void deleteFriend(int curUserId, int notFriend) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String acceptFriend = "DELETE FROM FRIENDS WHERE (USER1_ID = ? AND USER2_ID = ?) OR (USER1_ID = ? AND USER2_ID = ?);";
            PreparedStatement statement = connect.prepareStatement(acceptFriend);
            statement.setInt(1, notFriend);
            statement.setInt(2, curUserId);
            statement.setInt(3, curUserId);
            statement.setInt(4, notFriend);
            statement.execute();
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

}
