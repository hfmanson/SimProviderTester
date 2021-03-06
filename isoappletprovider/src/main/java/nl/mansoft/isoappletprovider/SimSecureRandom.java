/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.mansoft.isoappletprovider;

import java.security.SecureRandomSpi;

public class SimSecureRandom extends SecureRandomSpi {
    private final BaseSmartcardIO smartcardIO;


    public SimSecureRandom() {
        smartcardIO = SmartcardIOFactory.getInstance();
    }

    @Override
    protected void engineSetSeed(byte[] seed) {
    }

    @Override
    protected void engineNextBytes(byte[] bytes) {
        int length = bytes.length;
        byte data[] = engineGenerateSeed(length);
        if (data != null) {
            System.arraycopy(data, 0, bytes, 0, length);
        }
    }

    @Override
    protected byte[] engineGenerateSeed(int numBytes) {
        return smartcardIO == null ? null : smartcardIO.getChallenge(numBytes);
    }
}
