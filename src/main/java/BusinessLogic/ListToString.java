package BusinessLogic;

import java.util.ArrayList;

public class ListToString {

    public ListToString() {}

    private char findDelim(ArrayList<String> list) {
        for (int i = 0; i < 128; i++) {
            boolean found = true;
            char delim = (char) i;
            for (String s : list) {
                if (s.contains(Character.toString(delim))) {
                    found = false;
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
        for (int i = 0; i < list.size() - 1; i++) {
            sb.append(list.get(i));
            sb.append(delim);
        }
        sb.append(list.get(list.size() - 1));
        return sb.toString();
    }


}