package FunctionalClasses;

import java.sql.*;
import java.util.ArrayList;

import Types.Message;
import org.apache.commons.dbcp2.BasicDataSource;

public class MessagesConnect {
    private final String tableName = "MESSAGES";
    private final BasicDataSource dataSource;

    public MessagesConnect(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public ArrayList<Message> getMessagesWith(int curUserId, int friendId) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
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

    public void sendMessage(int fromUserId, int toUserId, String message) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String str = "INSERT INTO " + tableName + "(USER1_ID, USER2_ID, MESSAGE) VALUES(?,?,?)";
            PreparedStatement preparedStatement = connect.prepareStatement(str);
            preparedStatement.setString(1, String.valueOf(fromUserId));
            preparedStatement.setString(2, String.valueOf(toUserId));
            preparedStatement.setString(3, message);
            preparedStatement.executeUpdate();
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
