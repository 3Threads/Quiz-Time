package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

import Types.Message;
import org.apache.commons.dbcp2.BasicDataSource;

public class MessagesDAO {

    private final BasicDataSource dataSource;


    /**
     * Constructor for the MessagesDAO class.
     * Initializes the data source for database connectivity.
     *
     * @param dataSource The BasicDataSource instance used for database connections.
     */
    public MessagesDAO(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }


    /**
     * Retrieves messages between two users.
     * This method establishes a database connection using the provided data source.
     * It retrieves messages between the specified current user and their friend from the MESSAGES table.
     * The messages are retrieved in ascending order of the SEND_DATE field.
     * If any SQLException occurs during the database operations, the exception is caught,
     * and the stack trace is printed. The database connection is properly closed in the finally block
     * to ensure resource release, even if an exception occurs.
     *
     * @param curUserId The ID of the current user.
     * @param friendId  The ID of the friend to retrieve messages with.
     * @return An ArrayList of Message objects representing the exchanged messages.
     */
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


    /**
     * Sends a message from one user to another.
     * This method establishes a database connection using the provided data source.
     * It inserts a new record into the MESSAGES table, representing a message sent from the specified sender
     * to the specified recipient. The message content is provided as a parameter.
     * If any SQLException occurs during the database operations, the exception is caught,
     * and the stack trace is printed. The database connection is properly closed in the finally block
     * to ensure resource release, even if an exception occurs.
     *
     * @param fromUserId The ID of the user sending the message.
     * @param toUserId   The ID of the user receiving the message.
     * @param message    The content of the message.
     */
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


    /**
     * Retrieves not-seen messages for a given user.
     * This method establishes a database connection using the provided data source.
     * It retrieves messages from the MESSAGES table that are addressed to the specified user
     * and have not been marked as seen (SEEN = 0). The retrieved messages are organized into a HashMap
     * where the keys are the IDs of the senders, and the values are ArrayLists containing the messages
     * sent by each sender. If a sender has sent multiple messages, they are grouped together under the same key.
     * If any SQLException occurs during the database operations, the exception is caught,
     * and the stack trace is printed. The database connection is properly closed in the finally block
     * to ensure resource release, even if an exception occurs.
     *
     * @param userId The ID of the user for whom to retrieve not-seen messages.
     * @return A HashMap where keys are sender IDs and values are ArrayLists of not-seen messages.
     */
    public HashMap<Integer, ArrayList<String>> getNotSeenMessage(int userId) {
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
                if (notSeenMessages.containsKey(id)) {
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


    /**
     * Marks messages between two users as seen.
     * This method establishes a database connection using the provided data source.
     * It updates records in the MESSAGES table where messages were sent from the friendId to the curUserId
     * and have not been marked as seen (SEEN = 0). The method sets the SEEN flag to 1 to indicate that the messages
     * have been seen by the recipient. If any SQLException occurs during the database operations,
     * the exception is caught, and the stack trace is printed. The database connection is properly closed
     * in the finally block to ensure resource release, even if an exception occurs.
     *
     * @param curUserId The ID of the user for whom messages are marked as seen.
     * @param friendId  The ID of the friend who sent the messages.
     */
    public void setMessagesSeen(int curUserId, int friendId) {
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


    /**
     * Retrieves a list of users who have interacted with the specified user through messages.
     * This method establishes a database connection using the provided data source.
     * It retrieves distinct user IDs from the MESSAGES table where the specified user (userID)
     * has either sent or received messages. The retrieved user IDs represent users who have interacted
     * with the specified user. The list of interactors is sorted based on the send date of the messages,
     * with the most recent interactions appearing first. If any SQLException occurs during the database operations,
     * the exception is caught, and the stack trace is printed. The database connection is properly closed
     * in the finally block to ensure resource release, even if an exception occurs.
     *
     * @param userID The ID of the user for whom to retrieve the list of interactors.
     * @return An ArrayList of user IDs representing users who have interacted with the specified user.
     */
    public ArrayList<Integer> getInteractorsList(int userID) {
        Connection connect = null;
        try {
            connect = dataSource.getConnection();
            ArrayList<Integer> interactors = new ArrayList<>();
            String foundInteractors = "SELECT USER1_ID, SEND_DATE FROM MESSAGES WHERE USER2_ID = ? union " +
                    " SELECT USER2_ID, SEND_DATE FROM MESSAGES WHERE USER1_ID = ? ORDER BY SEND_DATE DESC";
            PreparedStatement statement = connect.prepareStatement(foundInteractors);
            statement.setInt(1, userID);
            statement.setInt(2, userID);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                int usID = result.getInt("USER1_ID");
                if (!interactors.contains(usID)) interactors.add(usID);
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