package nl.mansoft.simprovidertester;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.simalliance.openmobileapi.SEService;

import java.io.IOException;
import java.security.Provider;
import java.security.Security;

import nl.mansoft.isoappletprovider.SimProvider;
import nl.mansoft.isoappletprovider.SmartcardIO;

import static nl.mansoft.isoappletprovider.SmartcardIO.AID_ISOAPPLET;

public class MainActivity extends ActionBarActivity implements SEService.CallBack {
    public static final String TAG = MainActivity.class.getSimpleName();
    private SmartcardIO mSmartcardIO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSmartcardIO = SmartcardIO.getInstance();
        try {
            mSmartcardIO.setup(this, this);
            mSmartcardIO.setupToken();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy()");
        mSmartcardIO.teardown();
        mSmartcardIO = null;
        super.onDestroy();
    }

    @Override
    public void serviceConnected(SEService seService) {
        Log.i(TAG, "serviceConnected()");
        try {
            mSmartcardIO.setSession();
            mSmartcardIO.openChannel(AID_ISOAPPLET);

            Provider p = new SimProvider();
            Security.addProvider(p);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
