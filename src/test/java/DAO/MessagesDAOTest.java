package DAO;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class MessagesDAOTest {

    private static final String[] TABLE_NAMES = new String[]{"MESSAGES", "USERS"};
    private static MessagesDAO messagesDAO;
    private static BasicDataSource dataSource;
    private static final int USERS_NUM = 50;
    private static final int[] USERS_ID = new int[USERS_NUM];

    /*
        Creating users
    */
    @BeforeAll
    public static void setup() {
        dataSource = DataSource.getDataSource(true);

        UsersDAO usersDAO;
        messagesDAO = new MessagesDAO(dataSource);
        usersDAO = new UsersDAO(dataSource);

        for (int i = 0; i < 50; i++) {
            String userName = String.valueOf(i);
            usersDAO.addUser(userName, "1" + (i % 2));
            USERS_ID[i] = usersDAO.getUserId(userName);
        }
    }

    /*
        Simple 1 message test
     */
    @Test
    public void testSimpleSendMessage() {
        messagesDAO.sendMessage(USERS_ID[1], USERS_ID[2], "ggg");
        assertEquals("ggg", messagesDAO.getMessagesWith(USERS_ID[1], USERS_ID[2]).get(0).getMessage());
    }

    /*
        Incorrect message sent
     */
    @Test
    public void testIncorrectMessageSent() {
        messagesDAO.sendMessage(USERS_ID[3], USERS_ID[4], "g");
        assertNotEquals("ggg", messagesDAO.getMessagesWith(USERS_ID[3], USERS_ID[4]).get(0).getMessage());
    }

    /*
        2 messages sent from 1-->2 and from 2-->1
     */
    @Test
    public void testSendMessageToEachOther1() {
        messagesDAO.sendMessage(USERS_ID[5], USERS_ID[6], "1");
        messagesDAO.sendMessage(USERS_ID[6], USERS_ID[5], "2");
        assertEquals("1", messagesDAO.getMessagesWith(USERS_ID[5], USERS_ID[6]).get(0).getMessage());
        assertEquals("2", messagesDAO.getMessagesWith(USERS_ID[5], USERS_ID[6]).get(1).getMessage());
    }

    /*
        Same test as "testSendMessageToEachOther1" but sent 2--->1 and then 1-->2
     */
    @Test
    public void testSendMessageToEachOther2() {
        messagesDAO.sendMessage(USERS_ID[6], USERS_ID[5], "1");
        messagesDAO.sendMessage(USERS_ID[5], USERS_ID[6], "2");
        assertEquals("1", messagesDAO.getMessagesWith(USERS_ID[5], USERS_ID[6]).get(0).getMessage());
        assertEquals("2", messagesDAO.getMessagesWith(USERS_ID[5], USERS_ID[6]).get(1).getMessage());
    }

    /*
        Mixed messages
     */
    @Test
    public void testComplexMessages() {
        messagesDAO.sendMessage(USERS_ID[7], USERS_ID[9], "79");
        messagesDAO.sendMessage(USERS_ID[7], USERS_ID[10], "710");
        messagesDAO.sendMessage(USERS_ID[10], USERS_ID[7], "107");
        messagesDAO.sendMessage(USERS_ID[9], USERS_ID[8], "98");
        messagesDAO.sendMessage(USERS_ID[8], USERS_ID[10], "810");
        assertEquals(2, messagesDAO.getMessagesWith(USERS_ID[7], USERS_ID[10]).size());
        assertEquals(1, messagesDAO.getMessagesWith(USERS_ID[8], USERS_ID[9]).size());
        assertEquals(0, messagesDAO.getMessagesWith(USERS_ID[8], USERS_ID[7]).size());
        assertEquals("79", messagesDAO.getMessagesWith(USERS_ID[7], USERS_ID[9]).get(0).getMessage());
        assertEquals("710", messagesDAO.getMessagesWith(USERS_ID[7], USERS_ID[10]).get(0).getMessage());
        assertEquals("107", messagesDAO.getMessagesWith(USERS_ID[7], USERS_ID[10]).get(1).getMessage());
        assertEquals("98", messagesDAO.getMessagesWith(USERS_ID[8], USERS_ID[9]).get(0).getMessage());
        assertEquals("98", messagesDAO.getMessagesWith(USERS_ID[9], USERS_ID[8]).get(0).getMessage());
        assertEquals("810", messagesDAO.getMessagesWith(USERS_ID[8], USERS_ID[10]).get(0).getMessage());
    }

    /*
        Test not seen messages
     */
    @Test
    public void testNotSeenMessages() {
        messagesDAO.sendMessage(USERS_ID[40], USERS_ID[41], "1");
        messagesDAO.sendMessage(USERS_ID[40], USERS_ID[41], "2");
        messagesDAO.sendMessage(USERS_ID[41], USERS_ID[40], "4");
        messagesDAO.sendMessage(USERS_ID[40], USERS_ID[41], "7");
        messagesDAO.sendMessage(USERS_ID[40], USERS_ID[41], "3");
        messagesDAO.sendMessage(USERS_ID[41], USERS_ID[40], "5");
        messagesDAO.sendMessage(USERS_ID[41], USERS_ID[40], "9");
        messagesDAO.sendMessage(USERS_ID[41], USERS_ID[40], "6");

        ArrayList<String> strings1 = new ArrayList<>();
        strings1.add("1");
        strings1.add("2");
        strings1.add("7");
        strings1.add("3");
        HashMap<Integer, ArrayList<String>> res1 = new HashMap<>();
        res1.put(USERS_ID[40], strings1);

        ArrayList<String> strings2 = new ArrayList<>();
        strings2.add("4");
        strings2.add("5");
        strings2.add("9");
        strings2.add("6");
        HashMap<Integer, ArrayList<String>> res2 = new HashMap<>();
        res2.put(USERS_ID[41], strings2);

        HashMap<Integer, ArrayList<String>> curr1 = messagesDAO.getNotSeenMessage(USERS_ID[41]);
        HashMap<Integer, ArrayList<String>> curr2 = messagesDAO.getNotSeenMessage(USERS_ID[40]);

        assertEquals(res1, curr1);
        assertEquals(res2, curr2);
    }

    /*
        Test set seen messages
     */
    @Test
    public void testSetSeenSimple() {
        messagesDAO.sendMessage(USERS_ID[42], USERS_ID[43], "1");
        messagesDAO.sendMessage(USERS_ID[43], USERS_ID[42], "2");

        messagesDAO.setMessagesSeen(USERS_ID[43], USERS_ID[42]);

        messagesDAO.sendMessage(USERS_ID[42], USERS_ID[43], "3");

        ArrayList<String> strings = new ArrayList<>();
        strings.add("3");
        HashMap<Integer, ArrayList<String>> res = new HashMap<>();
        res.put(USERS_ID[42], strings);

        HashMap<Integer, ArrayList<String>> curr = messagesDAO.getNotSeenMessage(USERS_ID[43]);
        assertEquals(res, curr);
    }

    /*
        Test complex set seen messages
    */
    @Test
    public void testSetSeenHard() {
        messagesDAO.sendMessage(USERS_ID[44], USERS_ID[45], "1");
        messagesDAO.sendMessage(USERS_ID[44], USERS_ID[45], "2");
        messagesDAO.sendMessage(USERS_ID[44], USERS_ID[45], "3");

        messagesDAO.sendMessage(USERS_ID[44], USERS_ID[46], "4");
        messagesDAO.sendMessage(USERS_ID[44], USERS_ID[46], "5");
        messagesDAO.sendMessage(USERS_ID[44], USERS_ID[46], "6");

        ArrayList<String> strings1 = new ArrayList<>();
        strings1.add("1");
        strings1.add("2");
        strings1.add("3");
        HashMap<Integer, ArrayList<String>> res1 = new HashMap<>();
        res1.put(USERS_ID[44], strings1);

        ArrayList<String> strings2 = new ArrayList<>();
        strings2.add("4");
        strings2.add("5");
        strings2.add("6");
        HashMap<Integer, ArrayList<String>> res2 = new HashMap<>();
        res2.put(USERS_ID[44], strings2);

        HashMap<Integer, ArrayList<String>> curr1 = messagesDAO.getNotSeenMessage(USERS_ID[45]);
        HashMap<Integer, ArrayList<String>> curr2 = messagesDAO.getNotSeenMessage(USERS_ID[46]);

        assertEquals(res1, curr1);
        assertEquals(res2, curr2);

        messagesDAO.setMessagesSeen(USERS_ID[45], USERS_ID[44]);
        curr1 = messagesDAO.getNotSeenMessage(USERS_ID[45]);

        res1.clear();

        assertEquals(res1, curr1);
    }

    /*
        Test getInteractors() method
    */
    @Test
    public void testGetInteractorsSimple() {
        messagesDAO.sendMessage(USERS_ID[1], USERS_ID[0], "4");
        messagesDAO.sendMessage(USERS_ID[2], USERS_ID[0], "5");
        messagesDAO.sendMessage(USERS_ID[3], USERS_ID[0], "6");

        ArrayList<Integer> res = messagesDAO.getInteractorsList(USERS_ID[0]);
        assertEquals(USERS_ID[3], res.get(0));
        assertEquals(USERS_ID[2], res.get(1));
        assertEquals(USERS_ID[1], res.get(2));
    }

    /*
        Test complex getInteractors() method
    */
    @Test
    public void testGetInteractorsHard() {
        messagesDAO.sendMessage(USERS_ID[0], USERS_ID[1], "1");
        messagesDAO.sendMessage(USERS_ID[1], USERS_ID[0], "4");
        messagesDAO.sendMessage(USERS_ID[1], USERS_ID[0], "4");
        ArrayList<Integer> res = messagesDAO.getInteractorsList(USERS_ID[0]);
        assertEquals(USERS_ID[1], res.get(0));
        res = messagesDAO.getInteractorsList(USERS_ID[1]);
        assertEquals(USERS_ID[0], res.get(0));

        messagesDAO.sendMessage(USERS_ID[0], USERS_ID[2], "2");
        messagesDAO.sendMessage(USERS_ID[2], USERS_ID[0], "5");
        res = messagesDAO.getInteractorsList(USERS_ID[0]);
        assertEquals(USERS_ID[2], res.get(0));

        messagesDAO.sendMessage(USERS_ID[3], USERS_ID[2], "3");
        res = messagesDAO.getInteractorsList(USERS_ID[2]);
        assertEquals(USERS_ID[3], res.get(0));
        assertEquals(USERS_ID[0], res.get(1));
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
