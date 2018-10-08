package nl.mansoft.simprovidertester;

import java.security.Provider;
import java.security.Security;

import nl.mansoft.isoappletprovider.SimProvider;
import nl.mansoft.isoappletprovider.SmartcardIO;

public class SimTest extends ActivityInstrumentationTest {
    private Provider mProvider;
    private SmartcardIO mSmartcardIO;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mSmartcardIO = new SmartcardIO(mTestActivity, SmartcardIO.AID_ISOAPPLET, null);
        mProvider = new SimProvider();
        Security.addProvider(mProvider);
    }

    @Override
    protected void tearDown() throws Exception {
        mSmartcardIO.teardown();
        super.tearDown();
    }

    protected Provider getProvider() {
        return mProvider;
    }
}
