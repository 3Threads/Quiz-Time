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
    private static MessagesDAO mConnect;
    private static BasicDataSource dataSource;
    private static int USERS_NUM = 50;
    private static final int[] USERS_ID = new int[USERS_NUM+1];
    @BeforeAll
    public static void setup() {
        dataSource = DataSource.getDataSource(true);

        UsersDAO uConnect;
        mConnect = new MessagesDAO(dataSource);
        uConnect = new UsersDAO(dataSource);

        for (int i = 1; i <= 50; i++) {
            String userName = String.valueOf(i);
            uConnect.addUser(userName, "1" + (i % 2));
            USERS_ID[i] = uConnect.getUserId(userName);
        }

    }


    //simple 1 message test
    @Test
    public void testSimpleSendMessage() {
        mConnect.sendMessage(USERS_ID[1], USERS_ID[2], "ggg");
        assertEquals("ggg", mConnect.getMessagesWith(USERS_ID[1], USERS_ID[2]).get(0).getMessage());
    }


    //incorrect message sent
    @Test
    public void testIncorrectMessageSent() {
        mConnect.sendMessage(USERS_ID[3], USERS_ID[4], "g");
        assertNotEquals("ggg", mConnect.getMessagesWith(USERS_ID[3], USERS_ID[4]).get(0).getMessage());
    }


    //2 messages sent from 1-->2 and from 2-->1
    @Test
    public void testSendMessageToEachOther1() {
        mConnect.sendMessage(USERS_ID[5], USERS_ID[6], "1");
        mConnect.sendMessage(USERS_ID[6], USERS_ID[5], "2");
        assertEquals("1", mConnect.getMessagesWith(USERS_ID[5], USERS_ID[6]).get(0).getMessage());
        assertEquals("2", mConnect.getMessagesWith(USERS_ID[5], USERS_ID[6]).get(1).getMessage());
    }

    //same test as "testSendMessageToEachOther1" but sent 2--->1 and then 1-->2
    @Test
    public void testSendMessageToEachOther2() throws InterruptedException {
        mConnect.sendMessage(USERS_ID[6], USERS_ID[5], "1");
        mConnect.sendMessage(USERS_ID[5], USERS_ID[6], "2");
        assertEquals("1", mConnect.getMessagesWith(USERS_ID[5], USERS_ID[6]).get(0).getMessage());
        assertEquals("2", mConnect.getMessagesWith(USERS_ID[5], USERS_ID[6]).get(1).getMessage());
    }



    //Mixed messages
    @Test
    public void testComplexMessages() {
        mConnect.sendMessage(USERS_ID[7], USERS_ID[9], "79");
        mConnect.sendMessage(USERS_ID[7], USERS_ID[10], "710");
        mConnect.sendMessage(USERS_ID[10], USERS_ID[7], "107");
        mConnect.sendMessage(USERS_ID[9], USERS_ID[8], "98");
        mConnect.sendMessage(USERS_ID[8], USERS_ID[10], "810");
        assertEquals(2, mConnect.getMessagesWith(USERS_ID[7], USERS_ID[10]).size());
        assertEquals(1, mConnect.getMessagesWith(USERS_ID[8], USERS_ID[9]).size());
        assertEquals(0, mConnect.getMessagesWith(USERS_ID[8], USERS_ID[7]).size());
        assertEquals("79", mConnect.getMessagesWith(USERS_ID[7], USERS_ID[9]).get(0).getMessage());
        assertEquals("710", mConnect.getMessagesWith(USERS_ID[7], USERS_ID[10]).get(0).getMessage());
        assertEquals("107", mConnect.getMessagesWith(USERS_ID[7], USERS_ID[10]).get(1).getMessage());
        assertEquals("98", mConnect.getMessagesWith(USERS_ID[8], USERS_ID[9]).get(0).getMessage());
        assertEquals("98", mConnect.getMessagesWith(USERS_ID[9], USERS_ID[8]).get(0).getMessage());
        assertEquals("810", mConnect.getMessagesWith(USERS_ID[8], USERS_ID[10]).get(0).getMessage());

    }

    @Test
    public void testNotSeenMessages() {
        mConnect.sendMessage(USERS_ID[40], USERS_ID[41], "1");
        mConnect.sendMessage(USERS_ID[40], USERS_ID[41], "2");
        mConnect.sendMessage(USERS_ID[41], USERS_ID[40], "4");
        mConnect.sendMessage(USERS_ID[40], USERS_ID[41], "7");
        mConnect.sendMessage(USERS_ID[40], USERS_ID[41], "3");
        mConnect.sendMessage(USERS_ID[41], USERS_ID[40], "5");
        mConnect.sendMessage(USERS_ID[41], USERS_ID[40], "9");
        mConnect.sendMessage(USERS_ID[41], USERS_ID[40], "6");

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

        HashMap<Integer, ArrayList<String>> curr1 = mConnect.getNotSeenMessage(USERS_ID[41]);
        HashMap<Integer, ArrayList<String>> curr2 = mConnect.getNotSeenMessage(USERS_ID[40]);

        assertEquals(res1, curr1);
        assertEquals(res2, curr2);
    }

    @Test
    public void testSetSeenSimple1() {
        mConnect.sendMessage(USERS_ID[42], USERS_ID[43], "1");
        mConnect.sendMessage(USERS_ID[43], USERS_ID[42], "2");

        mConnect.setMessagesSeen(USERS_ID[43], USERS_ID[42]);

        mConnect.sendMessage(USERS_ID[42], USERS_ID[43], "3");

        ArrayList<String> strings = new ArrayList<>();
        strings.add("3");
        HashMap<Integer, ArrayList<String>> res = new HashMap<>();
        res.put(USERS_ID[42], strings);

        HashMap<Integer, ArrayList<String>> curr = mConnect.getNotSeenMessage(USERS_ID[43]);
        assertEquals(res, curr);
    }

    @Test
    public void testSetSeenHard() {
        mConnect.sendMessage(USERS_ID[44], USERS_ID[45], "1");
        mConnect.sendMessage(USERS_ID[44], USERS_ID[45], "2");
        mConnect.sendMessage(USERS_ID[44], USERS_ID[45], "3");

        mConnect.sendMessage(USERS_ID[44], USERS_ID[46], "4");
        mConnect.sendMessage(USERS_ID[44], USERS_ID[46], "5");
        mConnect.sendMessage(USERS_ID[44], USERS_ID[46], "6");

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

        HashMap<Integer, ArrayList<String>> curr1 = mConnect.getNotSeenMessage(USERS_ID[45]);
        HashMap<Integer, ArrayList<String>> curr2 = mConnect.getNotSeenMessage(USERS_ID[46]);

        assertEquals(res1, curr1);
        assertEquals(res2, curr2);

        mConnect.setMessagesSeen(USERS_ID[45], USERS_ID[44]);
        curr1 = mConnect.getNotSeenMessage(USERS_ID[45]);

        res1.clear();

        assertEquals(res1, curr1);
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
