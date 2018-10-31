package nl.mansoft.isoappletprovider;
/*
import android.content.Context;
import android.util.Log;

import org.simalliance.openmobileapi.Channel;
import org.simalliance.openmobileapi.Reader;
import org.simalliance.openmobileapi.SEService;
import org.simalliance.openmobileapi.Session;

import java.io.IOException;

import nl.mansoft.smartcardio.CardException;
import nl.mansoft.smartcardio.CommandAPDU;
import nl.mansoft.smartcardio.ResponseAPDU;

public class SmartcardIO extends BaseSmartcardIO implements SEService.CallBack {
    private final static String TAG = SmartcardIO.class.getSimpleName();

    private Session mSession;
    private Channel mCardChannel;
    private SEService mSeService;
    private Reader mReader;
    private boolean mReady;
    private SEService.CallBack mCallback;

    private static SmartcardIO sSmartcardIO;


    public SmartcardIO(Context context, byte[] aid, SEService.CallBack callBack) {
        super(context, aid);
        mReady = false;
        sSmartcardIO = this;
        mCallback = callBack;
        try {
            setup(context, this);
            setupToken();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static BaseSmartcardIO getInstance() {
        // only return sSmartcardIO when session is available
        return sSmartcardIO != null && sSmartcardIO.mSession != null ? sSmartcardIO : null;
    }

    public void waitReady() {
        synchronized (this) {
            while (!mReady) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ResponseAPDU runAPDU(CommandAPDU c) throws CardException {
        try {
            showCommandApduInfo(c);
            waitReady();
            byte[] result = mCardChannel.transmit(c.getBytes());
            ResponseAPDU responseAPDU = new ResponseAPDU(result);
            showResponseApduInfo(responseAPDU);
            return responseAPDU;
        } catch (IOException ex) {
            throw new CardException(ex.getMessage());
        }
    }

    public void runAPDU(final CommandAPDU c, final TransmitCallback callback) throws CardException {
        showCommandApduInfo(c);
        final byte[] bytes = c.getBytes();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    waitReady();
                    byte[] result = mCardChannel.transmit(bytes);
                    ResponseAPDU responseAPDU = new ResponseAPDU(result);
                    showResponseApduInfo(responseAPDU);
                    callback.callBack(responseAPDU);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void setup(Context context, SEService.CallBack callBack) throws IOException {
        mSeService = new SEService(context, callBack);
    }

    public void getFirstReader() {
        if (mReader == null) {
            Log.d(TAG, "Retrieve available readers...");
            Reader[] readers = mSeService.getReaders();
            if (readers.length < 1) {
                Log.e(TAG, "No readers found");
            } else {
                mReader = readers[0];
            }
        }
    }

    public void teardown() {
        waitReady();
        sSmartcardIO = null;
        closeChannel();

        getFirstReader();
        if (mReader != null) {
            Log.d(TAG, "Closing Sessions from the first reader");
            mReader.closeSessions();
            mReader = null;
        }
        if (mSeService != null && mSeService.isConnected()) {
            Log.d(TAG, "Shutting down service");
            mSeService.shutdown();
            mSeService = null;
        }
    }

    public void closeChannel() {
        if (mCardChannel != null) {
            mCardChannel.close();
            mCardChannel = null;
        }
    }

    public byte[] openChannel(byte aid[]) throws Exception {
        closeChannel();
        mCardChannel = mSession.openLogicalChannel(aid);
        return mCardChannel.getSelectResponse();
    }

    public void setSession() throws IOException {
        Log.d(TAG, "setSession()");
        getFirstReader();
        if (mReader != null) {
            Log.d(TAG, "Create Session from the first reader");
            mSession = mReader.openSession();
        }
    }

    public void setSessionAndOpenChannel() throws Exception {
        setSession();
        if (mAid != null) {
            openChannel(mAid);
        }
    }

    @Override
    public void serviceConnected(SEService seService) {
        Log.i(TAG, "serviceConnected()");
        synchronized (this) {
            mReady = true;
            notifyAll();
        }
        if (mCallback != null) {
            mCallback.serviceConnected(seService);
        }
    }
}
*/