package DAO;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FriendsDAOTest {
    private static FriendsDAO friendsDAO;

    @BeforeAll
    public static void setUp() {
        BasicDataSource dataSource = DataSource.getDataSource(true);

        UsersDAO usersDAO;
        friendsDAO = new FriendsDAO(dataSource);
        usersDAO = new UsersDAO(dataSource);
        usersDAO.addUser("1", "1");
        usersDAO.addUser("2", "2");
        usersDAO.addUser("3", "3");
        usersDAO.addUser("4", "4");
        usersDAO.addUser("5", "5");
        usersDAO.addUser("6", "6");
        usersDAO.addUser("7", "7");
        usersDAO.addUser("8", "8");
        usersDAO.addUser("a", "a");
        usersDAO.addUser("b", "b");
        usersDAO.addUser("c", "c");
        usersDAO.addUser("d", "c");
        usersDAO.addUser("e", "c");

    }

    //simple 1 request test
    @Test
    public void testSendRequestSimple() {
        friendsDAO.sendFriendRequest(1, 2);
        assertEquals(1, friendsDAO.getFriendsRequests(2).get(0));
    }


    // continue test1. accept request
    @Test
    public void testAcceptRequest() {
        friendsDAO.rejectRequest(2, 1);
        friendsDAO.sendFriendRequest(1, 2);
        friendsDAO.acceptRequest(2, 1);
        assertEquals(0, friendsDAO.getFriendsRequests(2).size());
        assertEquals(0, friendsDAO.getFriendsRequests(1).size());
        assertEquals(2, friendsDAO.getFriendsList(1).get(0));
        assertEquals(1, friendsDAO.getFriendsList(2).get(0));

    }

    // test for a pair of people
    @Test
    public void testSendRequestComplex() {
        assertEquals(0, friendsDAO.getFriendsRequests(3).size());
        friendsDAO.sendFriendRequest(3, 4);
        assertEquals(3, friendsDAO.getFriendsRequests(4).get(0));
        friendsDAO.acceptRequest(4, 3);
        friendsDAO.sendFriendRequest(3, 4);
        assertEquals(0, friendsDAO.getFriendsRequests(4).size());
    }

    // If both people sent friend requests to each other, then they are already friends.
    @Test
    public void testBothSentFriendRequest() {
        assertEquals(0, friendsDAO.getFriendsRequests(5).size());
        assertEquals(0, friendsDAO.getFriendsRequests(6).size());
        friendsDAO.sendFriendRequest(5, 6);
        assertEquals(5, friendsDAO.getFriendsRequests(6).get(0));
        friendsDAO.sendFriendRequest(6, 5);
        assertEquals(0, friendsDAO.getFriendsRequests(5).size());
        assertEquals(0, friendsDAO.getFriendsRequests(6).size());
    }

    // Testing delete func
    @Test
    public void testDeleteFriend() {
        assertEquals(0, friendsDAO.getFriendsRequests(7).size());
        assertEquals(0, friendsDAO.getFriendsRequests(8).size());
        friendsDAO.sendFriendRequest(8, 7);
        friendsDAO.acceptRequest(7, 8);
        assertEquals(8, friendsDAO.getFriendsList(7).get(0));
        friendsDAO.deleteFriend(7, 8);
        assertEquals(0, friendsDAO.getFriendsRequests(7).size());
        assertEquals(0, friendsDAO.getFriendsRequests(8).size());
    }

    // testing reject
    @Test
    public void testRejectFriend() {
        assertEquals(0, friendsDAO.getFriendsRequests(9).size());
        assertEquals(0, friendsDAO.getFriendsRequests(10).size());
        friendsDAO.sendFriendRequest(9, 10);
        assertEquals(9, friendsDAO.getFriendsRequests(10).get(0));
        friendsDAO.rejectRequest(10, 9);
        assertEquals(0, friendsDAO.getFriendsRequests(9).size());
        assertEquals(0, friendsDAO.getFriendsList(9).size());
        assertEquals(0, friendsDAO.getFriendsRequests(10).size());
        assertEquals(0, friendsDAO.getFriendsList(10).size());
    }

    // Complex test for all functions
    @Test
    public void testComplex() {
        assertEquals(0, friendsDAO.getFriendsRequests(11).size());
        assertEquals(0, friendsDAO.getFriendsRequests(12).size());
        assertEquals(0, friendsDAO.getFriendsRequests(13).size());
        friendsDAO.sendFriendRequest(11, 12);
        friendsDAO.sendFriendRequest(11, 13);
        friendsDAO.sendFriendRequest(12, 13);
        assertEquals(11, friendsDAO.getFriendsRequests(12).get(0));
        assertEquals(11, friendsDAO.getFriendsRequests(13).get(0));
        assertEquals(12, friendsDAO.getFriendsRequests(13).get(1));
        friendsDAO.acceptRequest(12, 11);
        friendsDAO.acceptRequest(13, 12);
        friendsDAO.rejectRequest(13, 11);
        assertEquals(0, friendsDAO.getFriendsRequests(12).size());
        assertEquals(0, friendsDAO.getFriendsRequests(13).size());
        assertEquals(0, friendsDAO.getFriendsRequests(13).size());
        friendsDAO.sendFriendRequest(13, 11);
        assertEquals(13, friendsDAO.getFriendsRequests(11).get(0));
        friendsDAO.sendFriendRequest(11, 13);
        assertEquals(0, friendsDAO.getFriendsRequests(11).size());
        assertEquals(11, friendsDAO.getFriendsList(12).get(0));
        assertEquals(12, friendsDAO.getFriendsList(13).get(0));
        assertEquals(11, friendsDAO.getFriendsList(13).get(1));
        friendsDAO.deleteFriend(13, 11);
        assertEquals(11, friendsDAO.getFriendsList(12).get(0));
        assertEquals(12, friendsDAO.getFriendsList(13).get(0));
        assertEquals(1, friendsDAO.getFriendsList(13).size());
    }


}
