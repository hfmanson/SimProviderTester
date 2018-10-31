package nl.mansoft.simprovidertester;

import org.junit.Test;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import nl.mansoft.isoappletprovider.SimKeystore;
import nl.mansoft.smartcardio.CardException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

public class SimCipherTest extends SimTest {
    /**
     * Test of doFinal method, of class SimCipher.
     *
     */
    @Test
    public void testDecrypt() throws CardException, KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        System.out.println("testDecrypt");
        //String alias = TestUtil.getSystemProperty("nl.mansoft.isoappletprovider.alias");
        String alias = "keycert";
        if (alias != null) {
            SecureRandom secureRandom = SecureRandom.getInstance("SIM-PRNG");
            byte[] random = secureRandom.generateSeed(128);
            assertNotNull(random);
            KeyStore ks = KeyStore.getInstance(SimKeystore.getType());
            ks.load(null, new char[]{'1', '2', '3', '4'});
            System.out.println(ks.getType());


            Certificate sim923 = ks.getCertificate(alias);
            PublicKey pubkey = sim923.getPublicKey();
            String algorithm = pubkey.getAlgorithm();
            System.out.println("Public key algorithm: " + algorithm);
            Cipher encryptCipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
            encryptCipher.init(Cipher.ENCRYPT_MODE, pubkey);
            byte[] encrypted = encryptCipher.doFinal(random);

            PrivateKey privatekey = (PrivateKey) ks.getKey(alias, null);
            algorithm = privatekey.getAlgorithm();
            System.out.println("Private key algorithm: " + algorithm);
            Cipher decryptCipher = Cipher.getInstance(algorithm, getProvider());
            decryptCipher.init(Cipher.DECRYPT_MODE, privatekey);
            byte[] decrypted = decryptCipher.doFinal(encrypted);
            assertNotNull(decrypted);
            assertArrayEquals(decrypted, random);
        }
    }
}
