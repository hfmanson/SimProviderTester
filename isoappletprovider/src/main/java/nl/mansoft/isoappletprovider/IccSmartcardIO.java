package nl.mansoft.isoappletprovider;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.telephony.IccOpenLogicalChannelResponse;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.Arrays;

import nl.mansoft.smartcardio.CardException;
import nl.mansoft.smartcardio.CommandAPDU;
import nl.mansoft.smartcardio.ResponseAPDU;

public class IccSmartcardIO extends BaseSmartcardIO {
    private final static String TAG = IccSmartcardIO.class.getSimpleName();
    private int mChannel;
    private TelephonyManager mTelephonyManager;
    private static BaseSmartcardIO sSmartcardIO;
    private static final int DEFAULT_EXTENDED_LE = 65532;
    private static final CommandAPDU getResponseAPDU = new CommandAPDU(0x00, 0xC0,0x00, 0x00, 0x100);
    private byte[] mResult;
    private int mOffset;
    private boolean mHasIccCard;

    public IccSmartcardIO(Context context, byte[] aid) {
        super(context, aid);
        mDebug = true;
        mChannel = -1;
        mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        mHasIccCard = mTelephonyManager.hasIccCard();
        if (mHasIccCard) {
            openChannel(aid);
            setupToken();
            sSmartcardIO = this;
            mResult = new byte[DEFAULT_EXTENDED_LE];
        }
    }

    public boolean hasIccCard() {
        return mHasIccCard;
    }

    public static BaseSmartcardIO getInstance() {
        return sSmartcardIO;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private byte[] getResponse(CommandAPDU c) {
        String response;
        int nc = c.getNc();
        if (nc == 0) {
            response = mTelephonyManager.iccTransmitApduLogicalChannel(mChannel, c.getCLA(), c.getINS(), c.getP1(), c.getP2(), c.getNe() & 0xff, null);
        } else {
            response = mTelephonyManager.iccTransmitApduLogicalChannel(mChannel, c.getCLA(), c.getINS(), c.getP1(), c.getP2(), nc, Util.ByteArrayToHexString(c.getData()));
        }
        return Util.HexStringToByteArray(response);
    }

    private void appendToResult(ResponseAPDU responseAPDU) {
        if (responseAPDU.getSW1() == 0x61 || responseAPDU.getSW() == 0x9000) {
            byte[] data = responseAPDU.getData();
            System.arraycopy(data, 0, mResult, mOffset, data.length);
            mOffset += data.length;
        }
    }

    @Override
    public ResponseAPDU runAPDU(CommandAPDU c) throws CardException {

        ResponseAPDU responseAPDU = null;
        if (mChannel == -1) {
            Log.e(TAG, "channel not available");
        }
        else {
            mOffset = 0;
            showCommandApduInfo(c);
            byte[] response = getResponse(c);
            responseAPDU = new ResponseAPDU(response);
            appendToResult(responseAPDU);
            while (responseAPDU.getSW1() == 0x61) {
                response = getResponse(getResponseAPDU);
                responseAPDU = new ResponseAPDU(response);
                appendToResult(responseAPDU);
            }
            if (responseAPDU.getSW() != 0x9000) {
                mOffset = 0;
            }
            mResult[mOffset++] = (byte) responseAPDU.getSW1();
            mResult[mOffset++] = (byte) responseAPDU.getSW2();
            responseAPDU = new ResponseAPDU(Arrays.copyOf(mResult, mOffset));
            showResponseApduInfo(responseAPDU);
        }
        return responseAPDU;
    }

    @Override
    public void teardown() {
        closeChannel();
    }

    @Override
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void closeChannel() {
        if (mChannel != -1 && !mTelephonyManager.iccCloseLogicalChannel(mChannel)) {
            Log.e(TAG, "Error closing logical channel");
        }
        mChannel = -1;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
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
