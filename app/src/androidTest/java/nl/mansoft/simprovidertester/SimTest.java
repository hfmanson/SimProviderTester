package nl.mansoft.simprovidertester;

import java.security.Provider;
import java.security.Security;

import nl.mansoft.isoappletprovider.SimProvider;

public class SimTest extends ActivityInstrumentationTest {
    private Provider mProvider;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mProvider = new SimProvider();
        Security.addProvider(mProvider);
    }

    protected Provider getProvider() {
        return mProvider;
    }
}
