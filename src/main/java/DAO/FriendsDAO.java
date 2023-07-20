package DAO;

import java.sql.*;
import java.util.ArrayList;

import Types.FriendInfo;
import org.apache.commons.dbcp2.BasicDataSource;

public class FriendsDAO {
    private final BasicDataSource dataSource;

    public FriendsDAO(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }

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

    public void deleteFriend(int curUserId, int notFriend) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String acceptFriend = "DELETE FROM FRIENDS WHERE (USER1_ID = ? AND USER2_ID = ?) OR (USER1_ID = ? AND USER2_ID = ?);";
            PreparedStatement statement = connect.prepareStatement(acceptFriend);
            statement.setInt(1, notFriend);
            statement.setInt(2,curUserId);
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
