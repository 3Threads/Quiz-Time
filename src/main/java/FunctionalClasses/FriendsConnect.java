package FunctionalClasses;

import java.sql.*;
import java.util.ArrayList;

public class FriendsConnect extends SQLConnect {
    private final String tableName = "FRIENDS";

    public FriendsConnect(boolean isTesting) {
        super(isTesting);
    }

    public ArrayList<Integer> getFriendsRequests(int userID) throws SQLException {
        ArrayList<Integer> requests = new ArrayList<>();
        Statement stmt = connect.createStatement();
        String foundRequests = "SELECT USER1_ID FROM " + tableName + " WHERE USER2_ID = '" + userID + "' AND ACCEPTED = 0;";
        ResultSet result = stmt.executeQuery(foundRequests);
        while (result.next()) {
            int usID = result.getInt("USER1_ID");
            requests.add(usID);
        }
        return requests;
    }

    public ArrayList<Integer> getFriendsList(int userID) throws SQLException {
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
    }
    public void sendFriendRequest(int fromUserId, int toUserId) throws SQLException {
        if(getFriendsRequests(fromUserId).contains(toUserId)){
            acceptRequest(fromUserId, toUserId);
            return;
        }
        if(getFriendsList(fromUserId).contains(toUserId)) return;
        Statement stmt = connect.createStatement();
        String sendRequest = "INSERT INTO " + tableName + "(USER1_ID, USER2_ID) VALUES(" + fromUserId + "," + toUserId + ");";
        stmt.execute(sendRequest);
    }
    public void acceptRequest(int curUserId, int newFriendId) throws SQLException {
        Statement stmt = connect.createStatement();
        String acceptFriend = "UPDATE " + tableName + " SET ACCEPTED = 1 WHERE USER1_ID = " + newFriendId +
                " AND USER2_ID = " + curUserId + ";";
        stmt.execute(acceptFriend);
    }

    public void rejectRequest(int curUserId, int notNewFriendId) throws SQLException {
        Statement stmt = connect.createStatement();
        String acceptFriend = "DELETE FROM " + tableName + " WHERE  USER1_ID = " + notNewFriendId +
                " AND USER2_ID = " + curUserId + ";";
        stmt.execute(acceptFriend);
    }

    public void deleteFriend(int curUserId, int notFriend) throws SQLException {
        Statement stmt = connect.createStatement();
        String acceptFriend = "DELETE FROM " + tableName + " WHERE (USER1_ID = " + notFriend +
                " AND USER2_ID = " + curUserId + ") OR (USER1_ID = " + curUserId +
                " AND USER2_ID = " + notFriend + ");";
        stmt.execute(acceptFriend);
    }
}
