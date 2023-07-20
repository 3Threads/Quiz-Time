package Tests;

import FunctionalClasses.DataSource;
import FunctionalClasses.FriendsConnect;
import FunctionalClasses.UserConnect;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FriendsConnectTest {
    private static FriendsConnect friendsConnect;

    @BeforeAll
    public static void setUp() {
        BasicDataSource dataSource = DataSource.getDataSource(true);

        UserConnect userConnect;
        friendsConnect = new FriendsConnect(dataSource);
        userConnect = new UserConnect(dataSource);
        userConnect.addUser("1", "1");
        userConnect.addUser("2", "2");
        userConnect.addUser("3", "3");
        userConnect.addUser("4", "4");
        userConnect.addUser("5", "5");
        userConnect.addUser("6", "6");
        userConnect.addUser("7", "7");
        userConnect.addUser("8", "8");
        userConnect.addUser("a", "a");
        userConnect.addUser("b", "b");
        userConnect.addUser("c", "c");
        userConnect.addUser("d", "c");
        userConnect.addUser("e", "c");

    }

    //simple 1 request test
    @Test
    public void testSendRequestSimple() {
        friendsConnect.sendFriendRequest(1, 2);
        assertEquals(1, friendsConnect.getFriendsRequests(2).get(0));
    }


    // continue test1. accept request
    @Test
    public void testAcceptRequest() {
        friendsConnect.rejectRequest(2, 1);
        friendsConnect.sendFriendRequest(1, 2);
        friendsConnect.acceptRequest(2, 1);
        assertEquals(0, friendsConnect.getFriendsRequests(2).size());
        assertEquals(0, friendsConnect.getFriendsRequests(1).size());
        assertEquals(2, friendsConnect.getFriendsList(1).get(0));
        assertEquals(1, friendsConnect.getFriendsList(2).get(0));

    }

    // test for a pair of people
    @Test
    public void testSendRequestComplex() {
        assertEquals(0, friendsConnect.getFriendsRequests(3).size());
        friendsConnect.sendFriendRequest(3, 4);
        assertEquals(3, friendsConnect.getFriendsRequests(4).get(0));
        friendsConnect.acceptRequest(4, 3);
        friendsConnect.sendFriendRequest(3, 4);
        assertEquals(0, friendsConnect.getFriendsRequests(4).size());
    }

    // If both people sent friend requests to each other, then they are already friends.
    @Test
    public void testBothSentFriendRequest() {
        assertEquals(0, friendsConnect.getFriendsRequests(5).size());
        assertEquals(0, friendsConnect.getFriendsRequests(6).size());
        friendsConnect.sendFriendRequest(5, 6);
        assertEquals(5, friendsConnect.getFriendsRequests(6).get(0));
        friendsConnect.sendFriendRequest(6, 5);
        assertEquals(0, friendsConnect.getFriendsRequests(5).size());
        assertEquals(0, friendsConnect.getFriendsRequests(6).size());
    }

    // Testing delete func
    @Test
    public void testDeleteFriend() {
        assertEquals(0, friendsConnect.getFriendsRequests(7).size());
        assertEquals(0, friendsConnect.getFriendsRequests(8).size());
        friendsConnect.sendFriendRequest(8, 7);
        friendsConnect.acceptRequest(7, 8);
        assertEquals(8, friendsConnect.getFriendsList(7).get(0));
        friendsConnect.deleteFriend(7, 8);
        assertEquals(0, friendsConnect.getFriendsRequests(7).size());
        assertEquals(0, friendsConnect.getFriendsRequests(8).size());
    }

    // testing reject
    @Test
    public void testRejectFriend() {
        assertEquals(0, friendsConnect.getFriendsRequests(9).size());
        assertEquals(0, friendsConnect.getFriendsRequests(10).size());
        friendsConnect.sendFriendRequest(9, 10);
        assertEquals(9, friendsConnect.getFriendsRequests(10).get(0));
        friendsConnect.rejectRequest(10, 9);
        assertEquals(0, friendsConnect.getFriendsRequests(9).size());
        assertEquals(0, friendsConnect.getFriendsList(9).size());
        assertEquals(0, friendsConnect.getFriendsRequests(10).size());
        assertEquals(0, friendsConnect.getFriendsList(10).size());
    }

    // Complex test for all functions
    @Test
    public void testComplex() {
        assertEquals(0, friendsConnect.getFriendsRequests(11).size());
        assertEquals(0, friendsConnect.getFriendsRequests(12).size());
        assertEquals(0, friendsConnect.getFriendsRequests(13).size());
        friendsConnect.sendFriendRequest(11, 12);
        friendsConnect.sendFriendRequest(11, 13);
        friendsConnect.sendFriendRequest(12, 13);
        assertEquals(11, friendsConnect.getFriendsRequests(12).get(0));
        assertEquals(11, friendsConnect.getFriendsRequests(13).get(0));
        assertEquals(12, friendsConnect.getFriendsRequests(13).get(1));
        friendsConnect.acceptRequest(12, 11);
        friendsConnect.acceptRequest(13, 12);
        friendsConnect.rejectRequest(13, 11);
        assertEquals(0, friendsConnect.getFriendsRequests(12).size());
        assertEquals(0, friendsConnect.getFriendsRequests(13).size());
        assertEquals(0, friendsConnect.getFriendsRequests(13).size());
        friendsConnect.sendFriendRequest(13, 11);
        assertEquals(13, friendsConnect.getFriendsRequests(11).get(0));
        friendsConnect.sendFriendRequest(11, 13);
        assertEquals(0, friendsConnect.getFriendsRequests(11).size());
        assertEquals(11, friendsConnect.getFriendsList(12).get(0));
        assertEquals(12, friendsConnect.getFriendsList(13).get(0));
        assertEquals(11, friendsConnect.getFriendsList(13).get(1));
        friendsConnect.deleteFriend(13, 11);
        assertEquals(11, friendsConnect.getFriendsList(12).get(0));
        assertEquals(12, friendsConnect.getFriendsList(13).get(0));
        assertEquals(1, friendsConnect.getFriendsList(13).size());
    }


}
