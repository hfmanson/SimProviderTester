package nl.mansoft.isoappletprovider;

import android.content.Context;
import android.telephony.IccOpenLogicalChannelResponse;
import android.telephony.TelephonyManager;
import android.util.Log;

import nl.mansoft.smartcardio.CardException;
import nl.mansoft.smartcardio.CommandAPDU;
import nl.mansoft.smartcardio.ResponseAPDU;

public class IccSmartcardIO extends BaseSmartcardIO {
    private final static String TAG = IccSmartcardIO.class.getSimpleName();
    private int mChannel;
    private TelephonyManager mTelephonyManager;
    private static BaseSmartcardIO sSmartcardIO;

    public IccSmartcardIO(Context context, byte[] aid) {
        super(context, aid);
        mDebug = true;
        mChannel = -1;
        mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        openChannel(aid);
        setupToken();
        sSmartcardIO = this;
    }

    public static BaseSmartcardIO getInstance() {
        return sSmartcardIO;
    }

    @Override
    public ResponseAPDU runAPDU(CommandAPDU c) throws CardException {
        showCommandApduInfo(c);
        String response;
        if (c.getNc() == 0) {
            response = mTelephonyManager.iccTransmitApduLogicalChannel(mChannel, c.getCLA(), c.getINS(), c.getP1(), c.getP2(), c.getNe(), null);
        } else {
            response = mTelephonyManager.iccTransmitApduLogicalChannel(mChannel, c.getCLA(), c.getINS(), c.getP1(), c.getP2(), c.getNc(), Util.ByteArrayToHexString(c.getData()));
        }
        ResponseAPDU responseAPDU = new ResponseAPDU(Util.HexStringToByteArray(response));
        showResponseApduInfo(responseAPDU);
        return responseAPDU;
    }

    @Override
    public void teardown() {
        closeChannel();
    }

    @Override
    public void closeChannel() {
        if (mChannel != -1 && !mTelephonyManager.iccCloseLogicalChannel(mChannel)) {
            Log.e(TAG, "Error closing logical channel");
        }
        mChannel = -1;
    }

    public byte[] openChannel(byte[] aid) {
        closeChannel();
        byte[] result = null;
        // Open channel and select aid
        IccOpenLogicalChannelResponse iccOpenLogicalChannelResponse = mTelephonyManager.iccOpenLogicalChannel(Util.ByteArrayToHexString(aid));
        int status = iccOpenLogicalChannelResponse.getStatus();
        if (status == IccOpenLogicalChannelResponse.STATUS_NO_ERROR) {
            mChannel = iccOpenLogicalChannelResponse.getChannel();
            Log.d(TAG, "channel: " + mChannel);
            result = iccOpenLogicalChannelResponse.getSelectResponse();
        } else {
            Log.e(TAG, "Error opening channel");
            mChannel = -1;
        }
        return result;
    }
}
