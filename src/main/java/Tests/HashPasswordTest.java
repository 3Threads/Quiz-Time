package Tests;
import log.HashPassword;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import log.UserConnect;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.SQLException;
public class HashPasswordTest {
    @Test
    public void testMolly(){
        assertTrue("262d1a52fbc3a33588fba3a68d63a1511d16da0d".equals(HashPassword.stringToHash("molly")));
    }


    @Test
    public void testFlomo(){
        assertTrue("2def06fb91eb2d1bcd9e22dd131c6999113b552b".equals(HashPassword.stringToHash("flomo")));
    }
    @Test
    public void testWithNums(){
        assertTrue("92948372caffc4cd3b31fbd193fa1b06738a2174".equals(HashPassword.stringToHash("flomo123")));
        assertTrue("8785066cd7584666239eb5ea7f9bea4f89f4c8d0".equals(HashPassword.stringToHash("molly777")));
    }

    @Test
    public void testOnlyNumbers(){
        assertTrue("59ddd534dd4a66572e193d285710a2c607b76e07".equals(HashPassword.stringToHash("1234567")));
        assertFalse("8785066cd7584666239eb5ea7f9bea4f89f4c8d0".equals(HashPassword.stringToHash("777")));
    }




}
