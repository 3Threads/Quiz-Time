package DAO;

import java.sql.*;
import java.util.ArrayList;

import Types.Message;
import org.apache.commons.dbcp2.BasicDataSource;

public class MessagesDAO {
    private final BasicDataSource dataSource;

    public MessagesDAO(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public ArrayList<Message> getMessagesWith(int curUserId, int friendId) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            ArrayList<Message> messages = new ArrayList<>();
            String getMessages = "SELECT USER1_ID, USER2_ID, MESSAGE FROM MESSAGES where (USER1_ID = ? AND USER2_ID = ?) OR (USER1_ID = ? AND USER2_ID = ?) ORDER BY SEND_DATE;";
            PreparedStatement statement = connect.prepareStatement(getMessages);
            statement.setInt(1, curUserId);
            statement.setInt(2, friendId);
            statement.setInt(3, friendId);
            statement.setInt(4, curUserId);
            ResultSet result = statement.executeQuery();
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
            String str = "INSERT INTO MESSAGES (USER1_ID, USER2_ID, MESSAGE) VALUES(?,?,?)";
            PreparedStatement statement = connect.prepareStatement(str);
            statement.setString(1, String.valueOf(fromUserId));
            statement.setString(2, String.valueOf(toUserId));
            statement.setString(3, message);
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
}
