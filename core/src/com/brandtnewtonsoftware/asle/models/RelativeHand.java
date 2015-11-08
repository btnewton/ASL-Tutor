package com.brandtnewtonsoftware.asle.models;

/**
 * Created by Brandt on 11/1/2015.
 */
public final class RelativeHand {

    private final double[] fingerExtensions = new double[5];

    public void setFingerExtension(int index, double extension) {
        fingerExtensions[index] = extension;
    }

    public double[] getFingerExtensions() {
        return fingerExtensions;
    }
}
