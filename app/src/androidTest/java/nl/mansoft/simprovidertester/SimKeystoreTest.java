package nl.mansoft.simprovidertester;

import org.junit.Test;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import nl.mansoft.isoappletprovider.SimKeystore;
import nl.mansoft.isoappletprovider.SimProvider;

public class SimKeystoreTest extends SimTest {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    private static void printCertificates(KeyStore ks) throws KeyStoreException, NoSuchAlgorithmException, CertificateEncodingException {
        Enumeration<String> aliases = ks.aliases();
        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            System.out.println("alias: " + alias);
            System.out.println("********************************************************");
            Certificate certificate = ks.getCertificate(alias);
            X509Certificate x509 = (X509Certificate) certificate;
            System.out.println(x509);
            System.out.println("********************************************************");
        }
    }

    /**
     * Test of engineLoad, engineAliases, engineGetCertificate, and engineContainsAlias of class SimKeystore.
     */
    @Test
    public void testKeystore() throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
        //String alias = TestUtil.getSystemProperty("nl.mansoft.isoappletprovider.alias");
        String alias = "keycert";
        if (alias != null) {
            KeyStore ks = KeyStore.getInstance(SimKeystore.getType());
            ks.load(null, new char[] { '1', '2', '3', '4' });
            System.out.println("Keystore size: " + ks.size());
            printCertificates(ks);
            assertTrue(ks.containsAlias(alias));
            assertFalse(ks.containsAlias("larie"));
        }
    }
}
