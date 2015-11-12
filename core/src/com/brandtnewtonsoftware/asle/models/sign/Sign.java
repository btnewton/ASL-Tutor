package com.brandtnewtonsoftware.asle.models.sign;

import com.brandtnewtonsoftware.asle.models.RelativeHand;

/**
 * Created by Brandt on 11/1/2015.
 */
public abstract class Sign {

    public static final double EXTENSION_FLOOR = .85;

    public final char VALUE;
    public final boolean[] fingersExtended;

    protected Sign(char signValue, boolean[] fingersExtended)  {
        this.VALUE = signValue;
        this.fingersExtended = fingersExtended;
    }


    public char getValue() {
        return VALUE;
    }

    public boolean equals(RelativeHand hand) {
        double[] extensions = hand.getFingerExtensions();
        for (int i = 0; i < 5; i++) {
            if (fingersExtended[i] ^ extensions[i] >= EXTENSION_FLOOR) {
                return false;
            }
        }
        return true;
    }

    public boolean equals(Sign sign) {
        return equals(sign.VALUE);
    }
    public boolean equals(char value) {
        return VALUE == value;
    }

    public String getImageFileName() {
        return "img/signs/ic_sign_" + VALUE + ".png";
    }
}
