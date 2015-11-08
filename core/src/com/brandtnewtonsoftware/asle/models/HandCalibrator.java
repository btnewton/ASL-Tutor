package com.brandtnewtonsoftware.asle.models;

import com.leapmotion.leap.*;

/**
 * Created by Brandt on 11/1/2015.
 */
public class HandCalibrator {

    private final double[] maxFingerLengths = new double[5];

    public void calibrate(Hand hand) {
        hand.fingers().forEach(finger -> calibrateFinger(finger, hand.wristPosition()));
        evaluateHand(hand);
    }

    private double vectorDistance(Vector vector1, Vector vector2) {
        return Math.sqrt(Math.pow(vector1.getX() - vector2.getX(), 2) + Math.pow(vector1.getY() - vector2.getY(), 2) + Math.pow(vector1.getZ() - vector2.getZ(), 2));
    }

    private double fingerLength(Finger finger, Vector palmCenter) {
        Vector endVector = finger.bone(Bone.Type.TYPE_DISTAL).nextJoint();
        return vectorDistance(palmCenter, endVector);
    }

    public RelativeHand evaluateHand(Hand hand) {
        RelativeHand relativeHand = new RelativeHand();
        hand.fingers().forEach(finger -> {
            double extension = percentExtended(finger.type(), fingerLength(finger, hand.wristPosition()));
            relativeHand.setFingerExtension(finger.type().swigValue(), extension);
        });
        return relativeHand;
    }

    // Removes calibrated lengths
    public void reset() {
        for (int i = 0; i < maxFingerLengths.length; i++) {
            maxFingerLengths[i] = 0;
        }
    }

    private double percentExtended(Finger.Type fingerType, double length) {
        return length / maxFingerLengths[fingerType.swigValue()];
    }

    private void calibrateFinger(Finger finger, Vector palmCenter) {
        double distance = fingerLength(finger, palmCenter);
        if (distance > maxFingerLengths[finger.type().swigValue()]) {
            maxFingerLengths[finger.type().swigValue()] = distance;
        }
    }
}
