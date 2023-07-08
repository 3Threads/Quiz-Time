package FunctionalClasses;

import java.sql.*;
import java.util.ArrayList;

public class FriendsConnect extends  SQLconnect{
    private final String tableName;

    public FriendsConnect(String tableName) {
        super();
        this.tableName = tableName;
    }
    public ArrayList<Integer> getFriendsRequests(int userID) throws SQLException {
        ArrayList<Integer> requests = new ArrayList<>();
        Statement stmt = connect.createStatement();
        stmt.execute("USE " + database);
        String foundRequests = "SELECT USER1ID FROM "+tableName+"WHERE USER2ID = '" + userID + "' AND ACCEPTED = 0;";
        ResultSet result = stmt.executeQuery(foundRequests);
        while(result.next()) {
            int usID = result.getInt("USER1ID");
            requests.add(usID);
        }
        return requests;
    }
    public ArrayList<Integer> getFriendsList(int userID) throws SQLException {
        ArrayList<Integer> friends = new ArrayList<>();
        Statement stmt = connect.createStatement();
        stmt.execute("USE " + database);
        String foundFriends = "SELECT USER1ID FROM "+ tableName +"WHERE USER2ID = '" + userID + "' AND ACCEPTED = 1 union " +
                "SELECT USER2ID WHERE USER1ID = '" + userID + "' AND ACCEPTED = 1";
        ResultSet result = stmt.executeQuery(foundFriends);
        while(result.next()) {
            int usID = result.getInt("USER1ID");
            friends.add(usID);
        }
        return friends;
    }
    public void acceptRequest(int curUserId, int newFriendId) throws SQLException {
        Statement stmt = connect.createStatement();
        stmt.execute("USE " + database);
        String acceptFriend = "UPDATE "+ tableName +" SET ACCEPTED = 1 WHERE  USER1ID = " + newFriendId +
                                    "AND USER2ID = " + curUserId + ";";
        stmt.execute(acceptFriend);
    }
    public void rejectRequest(int curUserId, int notNewFriendId) throws SQLException {
        Statement stmt = connect.createStatement();
        stmt.execute("USE " + database);
        String acceptFriend = "DELETE FROM "+ tableName +" WHERE  USER1ID = " + notNewFriendId +
                "AND USER2ID = " + curUserId + ";";
        stmt.execute(acceptFriend);
    }
    public void deleteFriend(int curUserId, int notFriend) throws SQLException {
        Statement stmt = connect.createStatement();
        stmt.execute("USE " + database);
        String acceptFriend = "DELETE FROM "+ tableName +" WHERE  (USER1ID = " + notFriend +
                "AND USER2ID = " + curUserId + ") or (USER1ID = " + curUserId +
                                    " + AND USER2ID = " + notFriend + ");";
        stmt.execute(acceptFriend);
    }
}
