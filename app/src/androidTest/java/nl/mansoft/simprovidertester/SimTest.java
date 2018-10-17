package nl.mansoft.simprovidertester;

import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.fail;

import java.security.Provider;
import java.security.Security;

import nl.mansoft.isoappletprovider.SimProvider;
import nl.mansoft.isoappletprovider.SmartcardIO;

public class SimTest extends ActivityInstrumentationTest {
    private Provider mProvider;
    private SmartcardIO mSmartcardIO;

    @Before
    public void afterActivityLaunched() {
        super.afterActivityLaunched();
        mSmartcardIO = new SmartcardIO(mTestActivity, SmartcardIO.AID_ISOAPPLET, null);
        mSmartcardIO.waitReady();
        try {
            mSmartcardIO.setSessionAndOpenChannel();
        } catch (Exception e) {
            fail(e.getMessage());
        }
        mProvider = new SimProvider();
        Security.addProvider(mProvider);
    }

    @After
    public void tearDown() throws Exception {
        mSmartcardIO.teardown();
    }

    protected Provider getProvider() {
        return mProvider;
    }

}
