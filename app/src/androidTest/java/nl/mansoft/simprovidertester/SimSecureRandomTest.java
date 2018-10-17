package nl.mansoft.simprovidertester;

import org.junit.Test;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import nl.mansoft.isoappletprovider.Util;

import static org.junit.Assert.assertEquals;

public class SimSecureRandomTest extends SimTest {
    public static void printRandom(byte random[]) {
        System.out.println(Util.ByteArrayToHexString(random));
    }

    public static SecureRandom getInstance() throws NoSuchAlgorithmException {
        return SecureRandom.getInstance("SIM-PRNG");
    }
    /**
     * Test of engineSetSeed method, of class SimSecureRandom.
     */
    @Test
    public void testEngineSetSeed() throws NoSuchAlgorithmException {
        System.out.println("engineSetSeed");
        byte[] seed = null;
        //SimSecureRandom instance = new SimSecureRandom();
        //instance.engineSetSeed(seed);
        SecureRandom secureRandom = getInstance();
        secureRandom.setSeed(seed);
    }

    /**
     * Test of engineNextBytes method, of class SimSecureRandom.
     */
    @Test
    public void testEngineNextBytes() throws NoSuchAlgorithmException {
        System.out.println("engineNextBytes");
        byte[] bytes = new byte[32];
        //SimSecureRandom instance = new SimSecureRandom();
        //instance.engineNextBytes(bytes);
        SecureRandom secureRandom = getInstance();
        secureRandom.nextBytes(bytes);
        printRandom(bytes);
    }

    /**
     * Test of engineGenerateSeed method, of class SimSecureRandom.
     */
    @Test
    public void testEngineGenerateSeed() throws NoSuchAlgorithmException {
        System.out.println("engineGenerateSeed");
        int numBytes = 32;
//        SimSecureRandom instance = new SimSecureRandom();
//        byte[] result = instance.engineGenerateSeed(numBytes);
        SecureRandom secureRandom = getInstance();
        byte[] result = secureRandom.generateSeed(numBytes);
        printRandom(result);
        assertEquals(numBytes, result.length);
    }
}