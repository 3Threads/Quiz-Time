package BusinessLogic;

import java.util.ArrayList;

public class ListToString {

    public ListToString() {
    }

    private char findDelim(ArrayList<String> list) {
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

    public String generateString(ArrayList<String> list) {
        char delim = findDelim(list);
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append(s);
            sb.append(delim);
        }
        return sb.toString();
    }

    public String iThString(String s, int i) {
        char delim = s.charAt(s.length() - 1);
        String[] pieces = s.split(String.valueOf(delim));
        return pieces[i];
    }

}