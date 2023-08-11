package DAO;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class MessagesDAOTest {
    private static MessagesDAO mConnect;

    @BeforeAll
    public static void setup() {
        BasicDataSource dataSource = DataSource.getDataSource(true);

        UsersDAO uConnect;
        mConnect = new MessagesDAO(dataSource);
        uConnect = new UsersDAO(dataSource);

        for (int i = 1; i <= 50; i++) {
            uConnect.addUser(String.valueOf(i), "1" + (i % 2));
        }

    }


    //simple 1 message test
    @Test
    public void testSimpleSendMessage() {
        mConnect.sendMessage(1, 2, "ggg");
        assertEquals("ggg", mConnect.getMessagesWith(1, 2).get(0).getMessage());
    }


    //incorrect message sent
    @Test
    public void testIncorrectMessageSent() {
        mConnect.sendMessage(3, 4, "g");
        assertNotEquals("ggg", mConnect.getMessagesWith(3, 4).get(0).getMessage());
    }


    //2 messages sent from 1-->2 and from 2-->1
    @Test
    public void testSendMessageToEachOther1() {
        mConnect.sendMessage(5, 6, "1");
        mConnect.sendMessage(6, 5, "2");
        assertEquals("1", mConnect.getMessagesWith(5, 6).get(0).getMessage());
        assertEquals("2", mConnect.getMessagesWith(5, 6).get(1).getMessage());
    }

    //same test as "testSendMessageToEachOther1" but sent 2--->1 and then 1-->2
    @Test
    public void testSendMessageToEachOther2() {
        mConnect.sendMessage(6, 5, "1");
        mConnect.sendMessage(5, 6, "2");
        assertEquals("1", mConnect.getMessagesWith(5, 6).get(0).getMessage());
        assertEquals("2", mConnect.getMessagesWith(5, 6).get(1).getMessage());
    }


    //Mixed messages
    @Test
    public void testComplexMessages() {
        mConnect.sendMessage(7, 9, "79");
        mConnect.sendMessage(7, 10, "710");
        mConnect.sendMessage(10, 7, "107");
        mConnect.sendMessage(9, 8, "98");
        mConnect.sendMessage(8, 10, "810");
        assertEquals(2, mConnect.getMessagesWith(7, 10).size());
        assertEquals(1, mConnect.getMessagesWith(8, 9).size());
        assertEquals(0, mConnect.getMessagesWith(8, 7).size());
        assertEquals("79", mConnect.getMessagesWith(7, 9).get(0).getMessage());
        assertEquals("710", mConnect.getMessagesWith(7, 10).get(0).getMessage());
        assertEquals("107", mConnect.getMessagesWith(7, 10).get(1).getMessage());
        assertEquals("98", mConnect.getMessagesWith(8, 9).get(0).getMessage());
        assertEquals("98", mConnect.getMessagesWith(9, 8).get(0).getMessage());
        assertEquals("810", mConnect.getMessagesWith(8, 10).get(0).getMessage());

    }

//    @Test
//    public void testGetInteractorsSimple() {
//        mConnect.sendMessage(16, 17, "1");
//        mConnect.sendMessage(18, 16, "1");
//        mConnect.sendMessage(16, 19, "1");
//        ArrayList<Integer> list1 = mConnect.getInteractorsList(16);
//
//        ArrayList<Integer> list2 = new ArrayList<>();
//        list2.add(17);
//        list2.add(18);
//        list2.add(19);
//
//        Collections.sort(list1);
//        Collections.sort(list2);
//        assertEquals(list1, list2);
//    }

//    @Test
//    public void testGetInteractorsMedium1() {
//        mConnect.sendMessage(21, 22, "1");
//        mConnect.sendMessage(21, 23, "1");
//
//        mConnect.sendMessage(21, 24, "1");
//        mConnect.sendMessage(24, 21, "1");
//        mConnect.sendMessage(21, 25, "1");
//
//        mConnect.sendMessage(21, 26, "1");
//
//        mConnect.sendMessage(21, 27, "1");
//        mConnect.sendMessage(27, 21, "1");
//        ArrayList<Integer> list1 = mConnect.getInteractorsList(21);
//
//        ArrayList<Integer> list2 = new ArrayList<>();
//        for (int i = 22; i <= 27; i++) {
//            list2.add(i);
//        }
//
//        Collections.sort(list1);
//        Collections.sort(list2);
//        assertEquals(list1, list2);
//    }

//    @Test
//    public void testGetInteractorsMedium2() {
//        for (int j = 32; j < 38; j++) {
//            mConnect.sendMessage(j, 31, "1");
//        }
//        ArrayList<Integer> list1 = mConnect.getInteractorsList(31);
//
//        ArrayList<Integer> list2 = new ArrayList<>();
//        for (int i = 32; i <= 37; i++) {
//            list2.add(i);
//        }
//
//        assertEquals(list1, list2);
//    }

//    @Test
//    public void testGetInteractorsHard() {
//        mConnect.sendMessage(11, 12, "1");
//        mConnect.sendMessage(11, 12, "1");
//
//        mConnect.sendMessage(12, 13, "2");
//        mConnect.sendMessage(13, 12, "2");
//
//        mConnect.sendMessage(13, 14, "3");
//        mConnect.sendMessage(14, 13, "3");
//
//        mConnect.sendMessage(14, 15, "4");
//        mConnect.sendMessage(15, 14, "4");
//        mConnect.sendMessage(14, 15, "4");
//        mConnect.sendMessage(14, 15, "4");
//        mConnect.sendMessage(15, 14, "4");
//
//        ArrayList<Integer> list1 = mConnect.getInteractorsList(11);
//        ArrayList<Integer> list2 = mConnect.getInteractorsList(12);
//        ArrayList<Integer> list3 = mConnect.getInteractorsList(13);
//        ArrayList<Integer> list4 = mConnect.getInteractorsList(14);
//        ArrayList<Integer> list5 = mConnect.getInteractorsList(15);
//
//        ArrayList<Integer> res1 = new ArrayList<>();
//        res1.add(12);
//        ArrayList<Integer> res2 = new ArrayList<>();
//        res2.add(11);
//        res2.add(13);
//        ArrayList<Integer> res3 = new ArrayList<>();
//        res3.add(12);
//        res3.add(14);
//        ArrayList<Integer> res4 = new ArrayList<>();
//        res4.add(13);
//        res4.add(15);
//        ArrayList<Integer> res5 = new ArrayList<>();
//        res5.add(14);
//
//        assertEquals(list1, res1);
//        assertEquals(list2, res2);
//        assertEquals(list3, res3);
//        assertEquals(list4, res4);
//        assertEquals(list5, res5);
//    }

    @Test
    public void testNotSeenMessages() {
        mConnect.sendMessage(40, 41, "1");
        mConnect.sendMessage(40, 41, "2");
        mConnect.sendMessage(41, 40, "4");
        mConnect.sendMessage(40, 41, "7");
        mConnect.sendMessage(40, 41, "3");
        mConnect.sendMessage(41, 40, "5");
        mConnect.sendMessage(41, 40, "9");
        mConnect.sendMessage(41, 40, "6");

        ArrayList<String> strings1 = new ArrayList<>();
        strings1.add("1");
        strings1.add("2");
        strings1.add("7");
        strings1.add("3");
        HashMap<Integer, ArrayList<String>> res1 = new HashMap<>();
        res1.put(40, strings1);

        ArrayList<String> strings2 = new ArrayList<>();
        strings2.add("4");
        strings2.add("5");
        strings2.add("9");
        strings2.add("6");
        HashMap<Integer, ArrayList<String>> res2 = new HashMap<>();
        res2.put(41, strings2);

        HashMap<Integer, ArrayList<String>> curr1 = mConnect.getNotSeenMessage(41);
        HashMap<Integer, ArrayList<String>> curr2 = mConnect.getNotSeenMessage(40);

        assertEquals(res1, curr1);
        assertEquals(res2, curr2);
    }

    @Test
    public void testSetSeenSimple1() {
        mConnect.sendMessage(42, 43, "1");
        mConnect.sendMessage(43, 42, "2");

        mConnect.setMessagesSeen(43, 42);

        mConnect.sendMessage(42, 43, "3");

        ArrayList<String> strings = new ArrayList<>();
        strings.add("3");
        HashMap<Integer, ArrayList<String>> res = new HashMap<>();
        res.put(42, strings);

        HashMap<Integer, ArrayList<String>> curr = mConnect.getNotSeenMessage(43);
        assertEquals(res, curr);
    }

    @Test
    public void testSetSeenHard() {
        mConnect.sendMessage(44, 45, "1");
        mConnect.sendMessage(44, 45, "2");
        mConnect.sendMessage(44, 45, "3");

        mConnect.sendMessage(44, 46, "4");
        mConnect.sendMessage(44, 46, "5");
        mConnect.sendMessage(44, 46, "6");

        ArrayList<String> strings1 = new ArrayList<>();
        strings1.add("1");
        strings1.add("2");
        strings1.add("3");
        HashMap<Integer, ArrayList<String>> res1 = new HashMap<>();
        res1.put(44, strings1);

        ArrayList<String> strings2 = new ArrayList<>();
        strings2.add("4");
        strings2.add("5");
        strings2.add("6");
        HashMap<Integer, ArrayList<String>> res2 = new HashMap<>();
        res2.put(44, strings2);

        HashMap<Integer, ArrayList<String>> curr1 = mConnect.getNotSeenMessage(45);
        HashMap<Integer, ArrayList<String>> curr2 = mConnect.getNotSeenMessage(46);

        assertEquals(res1, curr1);
        assertEquals(res2, curr2);

        mConnect.setMessagesSeen(45, 44);
        curr1 = mConnect.getNotSeenMessage(45);

        res1.clear();

        assertEquals(res1, curr1);
    }

}
