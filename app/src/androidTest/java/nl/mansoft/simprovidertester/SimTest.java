package nl.mansoft.simprovidertester;

import org.junit.After;
import org.junit.Before;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.security.Provider;
import java.security.Security;

import nl.mansoft.isoappletprovider.BaseSmartcardIO;
import nl.mansoft.isoappletprovider.IccSmartcardIO;
import nl.mansoft.isoappletprovider.SimProvider;
//import nl.mansoft.isoappletprovider.SmartcardIO;

public class SimTest extends ActivityInstrumentationTest {
    private Provider mProvider;
    private BaseSmartcardIO mSmartcardIO;
/*
    private void setupSmartcardIO() {
        SmartcardIO smartcardIO;
        smartcardIO = new SmartcardIO(mTestActivity, BaseSmartcardIO.AID_ISOAPPLET, null);
        smartcardIO.waitReady();
        try {
            smartcardIO.setSessionAndOpenChannel();
        } catch (Exception e) {
            fail(e.getMessage());
        }
        mSmartcardIO = smartcardIO;
    }
*/
    private void setupIccSmartcardIO() {
        mSmartcardIO = new IccSmartcardIO(mTestActivity, BaseSmartcardIO.AID_ISOAPPLET);
        assertTrue("No UICC card!", ((IccSmartcardIO) mSmartcardIO).hasIccCard());
    }

    @Before
    public void afterActivityLaunched() {
        super.afterActivityLaunched();
        setupIccSmartcardIO();
        //setupSmartcardIO();
        mProvider = new SimProvider();
        Security.addProvider(mProvider);
    }

    @After
    public void tearDown() {
        mSmartcardIO.teardown();
    }

    protected Provider getProvider() {
        return mProvider;
    }

}
