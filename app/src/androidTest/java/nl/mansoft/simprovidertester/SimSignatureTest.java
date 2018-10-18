package nl.mansoft.simprovidertester;

import org.junit.Test;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

import nl.mansoft.isoappletprovider.SimKeystore;
import nl.mansoft.isoappletprovider.Util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


public class SimSignatureTest extends SimTest {
    /**
     * Test of initSign, update, and sign method, of class SimSignature.
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.security.KeyStoreException
     * @throws java.io.IOException
     * @throws java.security.cert.CertificateException
     * @throws java.security.UnrecoverableKeyException
     * @throws java.security.InvalidKeyException
     * @throws java.security.SignatureException
     */
    @Test
    public void testSignature() throws NoSuchAlgorithmException, KeyStoreException, IOException, CertificateException, UnrecoverableKeyException, InvalidKeyException, SignatureException {
        System.out.println("testSignature");
        //String alias = TestUtil.getSystemProperty("nl.mansoft.isoappletprovider.alias");
        String alias = "keycert";
        if (alias != null) {
            SecureRandom secureRandom = SecureRandom.getInstance("SIM-PRNG");
            byte[] random = secureRandom.generateSeed(128);
            assertNotNull(random);
            KeyStore ks = KeyStore.getInstance(SimKeystore.getType());
            ks.load(null, new char[] { '1', '2', '3', '4' });
            System.out.println(ks.getType());
            PrivateKey privatekey = (PrivateKey) ks.getKey(alias, null);
            if (privatekey == null) {
                fail("Private key '" + alias + "' not found");
            } else {
                Signature signSignature = Signature.getInstance("NONEwithRSA", getProvider());
                signSignature.initSign(privatekey);
                signSignature.update(random);
                byte[] signature = signSignature.sign();
                assertNotNull(signature);
                System.out.println("signature: " + Util.ByteArrayToHexString(signature));

                Certificate certificate = ks.getCertificate(alias);
                if (certificate == null) {
                    fail("Certificate '" + alias + "' not found");
                } else {
                    Signature verifySignature = Signature.getInstance("NONEwithRSA");
                    verifySignature.initVerify(certificate);
                    verifySignature.update(random);
                    assertTrue(verifySignature.verify(signature));
                }
            }
        }
    }

}
