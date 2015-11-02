package com.brandtnewtonsoftware.asle.knn;

import com.brandtnewtonsoftware.asle.sign.*;
import com.brandtnewtonsoftware.asle.sign.Sign;
import com.leapmotion.leap.*;

/**
 * Created by Brandt on 11/1/2015.
 */
public class HandCalibrator {

    private final double[] maxFingerLengths = new double[5];

    public void calibrate(Hand hand) {
        hand.fingers().forEach(this::calibrateFinger);
        evaluateHand(hand);
    }

    private double vectorDistance(Vector vector1, Vector vector2) {
        return Math.sqrt(Math.pow(vector1.getX() - vector2.getX(), 2) + Math.pow(vector1.getY() - vector2.getY(), 2) + Math.pow(vector1.getZ() - vector2.getZ(), 2));
    }

    // TODO return negative distance if finger is curled
    private double fingerLength(Finger finger) {
        Vector startVector = finger.bone(Bone.Type.TYPE_METACARPAL).prevJoint();
        Vector endVector = finger.bone(Bone.Type.TYPE_DISTAL).nextJoint();
        return vectorDistance(startVector, endVector);
    }

    public LiteHand evaluateHand(Hand hand) {
        LiteHand liteHand = new LiteHand();
        hand.fingers().forEach(finger -> {
            double extension = percentExtended(finger.type(), fingerLength(finger));
            liteHand.setFingerExtension(finger.type().swigValue(), extension);
        });
        return liteHand;
    }

    // Removes calibrated lengths
    public void reset() {
        for (int i = 0; i < maxFingerLengths.length; i++) {
            maxFingerLengths[i] = 0;
        }
    }


    private void summarizeHand(LiteHand hand) {
        String[] fingerNames = new String[]{"Thumb", "Index", "Mid", "Ring", "Pink"};
        double[] fingerExtensions = hand.getFingerExtensions();
        for (int i = 0; i < fingerNames.length; i++) {
            boolean extended = fingerExtensions[i] > Sign.EXTENSION_FLOOR;
            System.out.print(fingerNames[i] + ": " + (extended? "OUT" : "IN") + "\t");
        }
        System.out.println("");
    }

    private double percentExtended(Finger.Type fingerType, double length) {
        return length / maxFingerLengths[fingerType.swigValue()];
    }

    private void calibrateFinger(Finger finger) {
        double distance = fingerLength(finger);
        if (distance > maxFingerLengths[finger.type().swigValue()]) {
            maxFingerLengths[finger.type().swigValue()] = distance;
        }
    }
}
