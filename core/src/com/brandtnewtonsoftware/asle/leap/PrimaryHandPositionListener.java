package com.brandtnewtonsoftware.asle.leap;

import com.leapmotion.leap.Vector;

/**
 * Created by Brandt on 10/31/2015.
 */
public interface PrimaryHandPositionListener {
    void onPrimaryHandUpdated(Vector handPosition);
}
