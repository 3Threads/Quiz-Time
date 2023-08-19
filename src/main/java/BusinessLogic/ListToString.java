package BusinessLogic;

import java.util.ArrayList;

public class ListToString {

    private static  char findDelim(ArrayList<String> list) {
        for (int i = 0; i < 128; i++) {
            boolean found = true;
            char delim = (char) i;
            for (String s : list) {
                if (s.contains(Character.toString(delim))) {
                    found = false;
                    break;
                }
            }
            if (found) {
                return delim;
            }
        }
        return ' ';
    }

    public static String generateString(ArrayList<String> list) {
        char delim = findDelim(list);
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append(s);
            sb.append(delim);
        }
        return sb.toString();
    }

}