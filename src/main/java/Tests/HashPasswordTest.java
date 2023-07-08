package Tests;

import Controllers.HashPassword;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HashPasswordTest {
    @Test
    public void testMolly() {
        assertEquals("262d1a52fbc3a33588fba3a68d63a1511d16da0d", HashPassword.stringToHash("molly"));
    }


    @Test
    public void testFlomo() {
        assertEquals("2def06fb91eb2d1bcd9e22dd131c6999113b552b", HashPassword.stringToHash("flomo"));
    }

    @Test
    public void testWithNums() {
        assertEquals("92948372caffc4cd3b31fbd193fa1b06738a2174", HashPassword.stringToHash("flomo123"));
        assertEquals("8785066cd7584666239eb5ea7f9bea4f89f4c8d0", HashPassword.stringToHash("molly777"));
    }

    @Test
    public void testOnlyNumbers() {
        assertEquals("59ddd534dd4a66572e193d285710a2c607b76e07", HashPassword.stringToHash("1234567"));
        assertNotEquals("8785066cd7584666239eb5ea7f9bea4f89f4c8d0", HashPassword.stringToHash("777"));
    }


}
