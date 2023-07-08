package FunctionalClasses;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashPassword {
    private static final String salt = "MACS";

    public static String hexToString(byte[] bytes) {
        StringBuffer buff = new StringBuffer();
        for (int aByte : bytes) {
            int val = aByte;
            val = val & 0xff;  // remove higher bits, sign
            if (val < 16) buff.append('0'); // leading 0
            buff.append(Integer.toString(val, 16));
        }
        return buff.toString();
    }

    public static String stringToHash(String s) {
        String password = s + salt;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            return hexToString(md.digest(password.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
