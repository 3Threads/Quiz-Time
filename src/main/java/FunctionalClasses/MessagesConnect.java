package FunctionalClasses;

import java.sql.*;
import java.util.ArrayList;

import Types.Message;

public class MessagesConnect extends SQLConnect {
    private final String tableName;

    public MessagesConnect(String tableName) {
        super();
        this.tableName = tableName;
    }

    public ArrayList<Message> getMessagesWith(int curUserId, int friendId) throws SQLException {
        ArrayList<Message> messages = new ArrayList<>();
        Statement stmt = connect.createStatement();
        String getMessages = "SELECT USER1ID, USER2ID, MESSAGE FROM " + tableName + " where (USER1ID = '" + curUserId + "' AND (USER2ID = '" +
                friendId + "') OR (USER1ID = '" + friendId + "' AND (USER2ID = '" +
                curUserId + "') ORDER BY SENDTIME;";
        ResultSet result = stmt.executeQuery(getMessages);
        while (result.next()) {
            int from = result.getInt("USER1ID");
            int to = result.getInt("USER2ID");
            String message = result.getString("MESSAGE");
            Message msg = new Message(from, to, message);
            messages.add(msg);
        }
        return messages;
    }

    public void sendMessage(int fromUserId, int toUserId, String message) throws SQLException {
        Statement stmt = connect.createStatement();
        String str = "INSERT INTO (USER1ID, USER2ID, MESSAGE)" + tableName + "VALUES(?,?,?)";
        PreparedStatement preparedStatement = connect.prepareStatement(str);
        preparedStatement.setString(1, String.valueOf(fromUserId));
        preparedStatement.setString(2, String.valueOf(toUserId));
        preparedStatement.setString(3, message);
        preparedStatement.executeUpdate();
    }
}
