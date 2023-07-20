package DAO;

import java.sql.*;
import java.util.ArrayList;

import Types.FriendInfo;
import org.apache.commons.dbcp2.BasicDataSource;

public class FriendsDAO {
    private final String tableName = "FRIENDS";
    private final BasicDataSource dataSource;

    public FriendsDAO(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public ArrayList<Integer> getFriendsRequests(int userID) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            ArrayList<Integer> requests = new ArrayList<>();
            Statement stmt = connect.createStatement();
            String foundRequests = "SELECT USER1_ID FROM " + tableName + " WHERE USER2_ID = '" + userID + "' AND ACCEPTED = 0;";
            ResultSet result = stmt.executeQuery(foundRequests);
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
            Statement stmt = connect.createStatement();
            String getInfo = "SELECT * FROM " + tableName + " WHERE (USER1_ID = " + user1 +
                    " AND USER2_ID = " + user2 + ") OR (USER1_ID = " + user2 +
                    " AND USER2_ID = " + user1 + ");";
            ResultSet result = stmt.executeQuery(getInfo);
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
            Statement stmt = connect.createStatement();
            String foundFriends = "SELECT USER1_ID FROM " + tableName + " WHERE USER2_ID = '" + userID + "' AND ACCEPTED = 1 union " +
                    " SELECT USER2_ID FROM FRIENDS WHERE USER1_ID = '" + userID + "' AND ACCEPTED = 1";
            ResultSet result = stmt.executeQuery(foundFriends);
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
            Statement stmt = connect.createStatement();
            String sendRequest = "INSERT INTO " + tableName + "(USER1_ID, USER2_ID) VALUES(" + fromUserId + "," + toUserId + ");";
            stmt.execute(sendRequest);
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
            Statement stmt = connect.createStatement();
            String acceptFriend = "UPDATE " + tableName + " SET ACCEPTED = 1 WHERE USER1_ID = " + newFriendId +
                    " AND USER2_ID = " + curUserId + ";";
            stmt.execute(acceptFriend);
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
            Statement stmt = connect.createStatement();
            String acceptFriend = "DELETE FROM " + tableName + " WHERE  USER1_ID = " + notNewFriendId +
                    " AND USER2_ID = " + curUserId + ";";
            stmt.execute(acceptFriend);
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
            Statement stmt = connect.createStatement();
            String acceptFriend = "DELETE FROM " + tableName + " WHERE (USER1_ID = " + notFriend +
                    " AND USER2_ID = " + curUserId + ") OR (USER1_ID = " + curUserId +
                    " AND USER2_ID = " + notFriend + ");";
            stmt.execute(acceptFriend);
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
