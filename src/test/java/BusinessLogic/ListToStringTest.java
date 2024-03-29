package BusinessLogic;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListToStringTest {

    @Test
    public void testListToStringSimple() {
        String str1 = "blah";
        String str2 = "hey";
        String str3 = "yo";
        String[] questions = new String[]{str1, str2, str3};
        check(questions);
    }

    @Test
    public void testAllSymbols() {
        StringBuilder str = new StringBuilder();
        StringBuilder str2 = new StringBuilder();
        for (int i = 0; i < 128; i++) {
            char delim = (char) i;
            char delim2 = (char) (128 - i);
            str.append(delim);
            str2.append(delim2);
        }
        String[] questions = new String[]{str.toString(), str2.toString(), str.toString()};
        check(questions);
    }

    private void check(String[] questions) {
        ArrayList<String> qList = new ArrayList<>(Arrays.asList(questions));

        String res = ListToString.generateString(qList);

        ArrayList<String> answ = new ArrayList<>();
        int num = 0;
        for (String s : qList) {
            String curr = res.substring(num, num + s.length());
            answ.add(curr);
            num += s.length() + 1;
        }

        assertEquals(answ, qList);
    }
}
