package FunctionalClasses;

import java.sql.*;
import java.util.ArrayList;

import Types.Message;

public class MessagesConnect extends SQLConnect {
    private final String tableName = "MESSAGES";

    public MessagesConnect(boolean isTesting) {
        super(isTesting);
    }

    public ArrayList<Message> getMessagesWith(int curUserId, int friendId) throws SQLException {
        ArrayList<Message> messages = new ArrayList<>();
        Statement stmt = connect.createStatement();
        String getMessages = "SELECT USER1_ID, USER2_ID, MESSAGE FROM " + tableName + " where (USER1_ID = '" + curUserId + "' AND USER2_ID = '" +
                friendId + "') OR (USER1_ID = '" + friendId + "' AND USER2_ID = '" +
                curUserId + "') ORDER BY SEND_DATE;";
        ResultSet result = stmt.executeQuery(getMessages);
        while (result.next()) {
            int from = result.getInt("USER1_ID");
            int to = result.getInt("USER2_ID");
            String message = result.getString("MESSAGE");
            Message msg = new Message(from, to, message);
            messages.add(msg);
        }
        return messages;
    }

    public void sendMessage(int fromUserId, int toUserId, String message) throws SQLException {
        String str = "INSERT INTO " + tableName + "(USER1_ID, USER2_ID, MESSAGE) VALUES(?,?,?)";
        PreparedStatement preparedStatement = connect.prepareStatement(str);
        preparedStatement.setString(1, String.valueOf(fromUserId));
        preparedStatement.setString(2, String.valueOf(toUserId));
        preparedStatement.setString(3, message);
        preparedStatement.executeUpdate();
    }
}
