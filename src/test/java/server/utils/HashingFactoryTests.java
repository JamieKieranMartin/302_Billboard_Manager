package server.utils;

import java.util.Random;

import common.utils.session.HashingFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HashingFactoryTests {
    @Test
    public void testingHashingPassword() throws Exception {
        String hashedPass = HashingFactory.hashPassword("1234");
        assertEquals(hashedPass, "7110EDA4D09E062AA5E4A390B0A572AC0D2C0220".toLowerCase());
    }

    @Test
    public void testingDecode() throws Exception {
        byte[] result = HashingFactory.decodeHex(HashingFactory.hashPassword("1234"));
        byte[] testSet = {113, 16, -19, -92, -48, -98, 6, 42, -91, -28, -93, -112, -80, -91, 114, -84, 13, 44, 2, 32};
        assertArrayEquals(testSet, result);
    }

    @Test
    public void testingGetSalt() throws Exception {
        byte[] result = HashingFactory.getSalt();
        byte[] array = new byte[32];
        new Random().nextBytes(array);
        assertNotEquals(array, result);
    }

    @Test
    public void testingHAS() throws Exception {
        String password = "1234";
        byte[] salt = "salt".getBytes();
        byte[] result = HashingFactory.hashAndSaltPassword(password, salt);
        byte[] check = {39, -123, 2, -76, 102, 1, -85, 30};
        assertArrayEquals(check,result);
    }

}
