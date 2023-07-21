package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

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

    public HashMap<Integer, ArrayList<String>> getNotSeenMessage(int userId){
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            HashMap<Integer, ArrayList<String>> notSeenMessages = new HashMap<>();
            String getMessages = "SELECT * FROM MESSAGES where USER2_ID = ? and SEEN = 0 ORDER BY SEND_DATE";
            PreparedStatement preparedStatement = connect.prepareStatement(getMessages);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("USER1_ID");
                String message = resultSet.getString("MESSAGE");
                if(notSeenMessages.containsKey(id)) {
                    notSeenMessages.get(id).add(message);
                } else {
                    ArrayList<String> newArr = new ArrayList<>();
                    newArr.add(message);
                    notSeenMessages.put(id, newArr);
                }
            }
            return notSeenMessages;
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
    public void setMessagesSeen(int curUserId, int friendId){
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            String setSeenMessage = "UPDATE MESSAGES SET SEEN = 1 WHERE USER1_ID = ? AND USER2_ID = ? AND SEEN = 0";
            PreparedStatement statement = connect.prepareStatement(setSeenMessage);
            statement.setInt(1, friendId);
            statement.setInt(2, curUserId);
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

    public ArrayList<Integer> getInteractorsList(int userID){
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            ArrayList<Integer> interactors = new ArrayList<>();
            String foundInteractors = "SELECT USER1_ID FROM MESSAGES WHERE USER2_ID = ? union " +
                    " SELECT USER2_ID FROM MESSAGES WHERE USER1_ID = ?";
            PreparedStatement statement = connect.prepareStatement(foundInteractors);
            statement.setInt(1, userID);
            statement.setInt(2, userID);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                int usID = result.getInt("USER1_ID");
                if(!interactors.contains(userID)) interactors.add(usID);
            }
            return interactors;
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