package com.brandtnewtonsoftware.asle.leap;

import com.leapmotion.leap.Hand;
import com.leapmotion.leap.HandList;
import com.leapmotion.leap.Vector;

/**
 * Created by Brandt on 10/31/2015.
 */
public class LeapHelper {

    public static final double MAX_X = 251;
    public static final double MAX_Y = 597;
    public static final double MAX_Z = 258;
    public static final double STAGE_RADIUS = 75;
    public static final double OUTER_RADIUS = 350;

    // Returns X & Z distance from origin of leap device
    public static double getDistanceToCenterStage(Vector vector) {
        double distance = Math.hypot(vector.getX(), vector.getZ());
        if (OUTER_RADIUS < distance)
            distance = OUTER_RADIUS;
        return distance;
    }

    public static boolean inCenterStage(double distance) {
        return distance <= STAGE_RADIUS;
    }

    public static boolean inCenterStage(Vector vector) {
        return inCenterStage(getDistanceToCenterStage(vector));
    }

    public static Hand getDefaultHand(HandList hands) {
        if (hands.count() > 0)
            return hands.rightmost();
        else
            return null;
    }
}
