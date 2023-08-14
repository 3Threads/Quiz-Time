package DAO;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FriendsDAOTest {
    private static FriendsDAO friendsDAO;
    private static final String[] TABLE_NAMES = new String[]{"FRIENDS", "USERS"};
    private static final int USERS_NUM = 16;
    private static final int[] USERS_ID = new int[USERS_NUM+1];
    private static BasicDataSource dataSource;
    @BeforeAll
    public static void setUp() {
        dataSource = DataSource.getDataSource(true);

        UsersDAO usersDAO;
        friendsDAO = new FriendsDAO(dataSource);
        usersDAO = new UsersDAO(dataSource);
        for (int i = 1; i <= 16; i++) {
            String userName = String.valueOf(i);
            usersDAO.addUser(userName, ""+i);
            USERS_ID[i] = usersDAO.getUserId(userName);
        }
    }
    //simple 1 request test
    @Test
    public void testSendRequestSimple() {
        friendsDAO.sendFriendRequest(USERS_ID[1], USERS_ID[2]);
        assertEquals(USERS_ID[1], friendsDAO.getFriendsRequests(USERS_ID[2]).get(0));
    }


    // continue test1. accept request
    @Test
    public void testAcceptRequest() {
        friendsDAO.rejectRequest(USERS_ID[2], USERS_ID[1]);
        friendsDAO.sendFriendRequest(USERS_ID[1], USERS_ID[2]);
        friendsDAO.acceptRequest(USERS_ID[2], USERS_ID[1]);
        assertEquals(0, friendsDAO.getFriendsRequests(USERS_ID[2]).size());
        assertEquals(0, friendsDAO.getFriendsRequests(USERS_ID[1]).size());
        assertEquals(USERS_ID[2], friendsDAO.getFriendsList(USERS_ID[1]).get(0));
        assertEquals(USERS_ID[1], friendsDAO.getFriendsList(USERS_ID[2]).get(0));

    }

    // test for a pair of people
    @Test
    public void testSendRequestComplex() {
        assertEquals(0, friendsDAO.getFriendsRequests(USERS_ID[3]).size());
        friendsDAO.sendFriendRequest(USERS_ID[3], USERS_ID[4]);
        assertEquals(USERS_ID[3], friendsDAO.getFriendsRequests(USERS_ID[4]).get(0));
        friendsDAO.acceptRequest(USERS_ID[4], USERS_ID[3]);
        friendsDAO.sendFriendRequest(USERS_ID[3], USERS_ID[4]);
        assertEquals(0, friendsDAO.getFriendsRequests(USERS_ID[4]).size());
    }

    // If both people sent friend requests to each other, then they are already friends.
    @Test
    public void testBothSentFriendRequest() {
        assertEquals(0, friendsDAO.getFriendsRequests(USERS_ID[5]).size());
        assertEquals(0, friendsDAO.getFriendsRequests(USERS_ID[6]).size());
        friendsDAO.sendFriendRequest(USERS_ID[5], USERS_ID[6]);
        assertEquals(USERS_ID[5], friendsDAO.getFriendsRequests(USERS_ID[6]).get(0));
        friendsDAO.sendFriendRequest(USERS_ID[6], USERS_ID[5]);
        assertEquals(0, friendsDAO.getFriendsRequests(USERS_ID[5]).size());
        assertEquals(0, friendsDAO.getFriendsRequests(USERS_ID[6]).size());
    }

    // Testing delete func
    @Test
    public void testDeleteFriend() {
        assertEquals(0, friendsDAO.getFriendsRequests(USERS_ID[7]).size());
        assertEquals(0, friendsDAO.getFriendsRequests(USERS_ID[8]).size());
        friendsDAO.sendFriendRequest(USERS_ID[8], USERS_ID[7]);
        friendsDAO.acceptRequest(USERS_ID[7], USERS_ID[8]);
        assertEquals(USERS_ID[8], friendsDAO.getFriendsList(USERS_ID[7]).get(0));
        friendsDAO.deleteFriend(USERS_ID[7], USERS_ID[8]);
        assertEquals(0, friendsDAO.getFriendsRequests(USERS_ID[7]).size());
        assertEquals(0, friendsDAO.getFriendsRequests(USERS_ID[8]).size());
    }

    // testing reject
    @Test
    public void testRejectFriend() {
        assertEquals(0, friendsDAO.getFriendsRequests(USERS_ID[9]).size());
        assertEquals(0, friendsDAO.getFriendsRequests(USERS_ID[10]).size());
        friendsDAO.sendFriendRequest(USERS_ID[9], USERS_ID[10]);
        assertEquals(USERS_ID[9], friendsDAO.getFriendsRequests(USERS_ID[10]).get(0));
        friendsDAO.rejectRequest(USERS_ID[10], USERS_ID[9]);
        assertEquals(0, friendsDAO.getFriendsRequests(USERS_ID[9]).size());
        assertEquals(0, friendsDAO.getFriendsList(USERS_ID[9]).size());
        assertEquals(0, friendsDAO.getFriendsRequests(USERS_ID[10]).size());
        assertEquals(0, friendsDAO.getFriendsList(USERS_ID[10]).size());
        assertEquals(-1,friendsDAO.getBetweenUsersInfo(USERS_ID[10], USERS_ID[9]).getAccepted());
    }

    // Complex test for all functions
    @Test
    public void testComplex() {
        assertEquals(0, friendsDAO.getFriendsRequests(USERS_ID[11]).size());
        assertEquals(0, friendsDAO.getFriendsRequests(USERS_ID[12]).size());
        assertEquals(0, friendsDAO.getFriendsRequests(USERS_ID[13]).size());
        friendsDAO.sendFriendRequest(USERS_ID[11], USERS_ID[12]);
        friendsDAO.sendFriendRequest(USERS_ID[11], USERS_ID[13]);
        friendsDAO.sendFriendRequest(USERS_ID[12], USERS_ID[13]);
        assertEquals(USERS_ID[11], friendsDAO.getFriendsRequests(USERS_ID[12]).get(0));
        assertEquals(USERS_ID[11], friendsDAO.getFriendsRequests(USERS_ID[13]).get(0));
        assertEquals(USERS_ID[12], friendsDAO.getFriendsRequests(USERS_ID[13]).get(1));
        friendsDAO.acceptRequest(USERS_ID[12], USERS_ID[11]);
        friendsDAO.acceptRequest(USERS_ID[13], USERS_ID[12]);
        friendsDAO.rejectRequest(USERS_ID[13], USERS_ID[11]);
        assertEquals(0, friendsDAO.getFriendsRequests(USERS_ID[12]).size());
        assertEquals(0, friendsDAO.getFriendsRequests(USERS_ID[13]).size());
        assertEquals(0, friendsDAO.getFriendsRequests(USERS_ID[13]).size());
        friendsDAO.sendFriendRequest(USERS_ID[13], USERS_ID[11]);
        assertEquals(USERS_ID[13], friendsDAO.getFriendsRequests(USERS_ID[11]).get(0));
        friendsDAO.sendFriendRequest(USERS_ID[11], USERS_ID[13]);
        assertEquals(0, friendsDAO.getFriendsRequests(USERS_ID[11]).size());
        assertEquals(USERS_ID[11], friendsDAO.getFriendsList(USERS_ID[12]).get(0));
        assertEquals(USERS_ID[12], friendsDAO.getFriendsList(USERS_ID[13]).get(0));
        assertEquals(USERS_ID[11], friendsDAO.getFriendsList(USERS_ID[13]).get(1));
        friendsDAO.deleteFriend(USERS_ID[13], USERS_ID[11]);
        assertEquals(USERS_ID[11], friendsDAO.getFriendsList(USERS_ID[12]).get(0));
        assertEquals(USERS_ID[12], friendsDAO.getFriendsList(USERS_ID[13]).get(0));
        assertEquals(1, friendsDAO.getFriendsList(USERS_ID[13]).size());
    }
    @Test
    public void testGetBetweenUsersInfo(){
        assertEquals(0, friendsDAO.getFriendsRequests(USERS_ID[14]).size());
        assertEquals(0, friendsDAO.getFriendsRequests(USERS_ID[15]).size());
        assertEquals(0, friendsDAO.getFriendsRequests(USERS_ID[16]).size());
        assertEquals(-1, friendsDAO.getBetweenUsersInfo(USERS_ID[15], USERS_ID[14]).getAccepted());
        assertEquals(-1, friendsDAO.getBetweenUsersInfo(USERS_ID[16], USERS_ID[14]).getAccepted());
        assertEquals(-1, friendsDAO.getBetweenUsersInfo(USERS_ID[15], USERS_ID[16]).getAccepted());
        friendsDAO.sendFriendRequest(USERS_ID[14], USERS_ID[15]);
        friendsDAO.acceptRequest(USERS_ID[15], USERS_ID[14]);
        assertEquals(1, friendsDAO.getBetweenUsersInfo(USERS_ID[15], USERS_ID[14]).getAccepted());

    }
    @AfterEach
    public void finishUnitTest() {
        DataSource.clearTables(dataSource, new String[]{TABLE_NAMES[0]});
    }

    @AfterAll
    public static void finish() {
        DataSource.clearTables(dataSource, TABLE_NAMES);
    }

}
