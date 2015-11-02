package com.brandtnewtonsoftware.asle.knn;

import java.util.Iterator;

/**
 * Created by Brandt on 11/1/2015.
 */
public final class LiteHand {

    private final double[] fingerExtensions = new double[5];

    public void setFingerExtension(int index, double extension) {
        fingerExtensions[index] = extension;
    }

    public double[] getFingerExtensions() {
        return fingerExtensions;
    }
}
