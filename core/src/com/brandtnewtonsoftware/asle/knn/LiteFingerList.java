package com.brandtnewtonsoftware.asle.knn;

import com.leapmotion.leap.Finger;

/**
 * Created by brandt on 10/28/15.
 */
public class LiteFingerList {

    public static final int FINGER_COUNT = 5;
    private final LiteFinger[] fingers = new LiteFinger[FINGER_COUNT];

    public LiteFingerList(LiteFinger[] fingers) {
        for (int i = 0; i < FINGER_COUNT; i++) {
            this.fingers[i] = fingers[i];
        }
    }

    public LiteFingerList(LiteFinger thumb, LiteFinger index, LiteFinger middle, LiteFinger ring, LiteFinger pinky) {
        fingers[Finger.Type.TYPE_THUMB.swigValue()] = thumb;
        fingers[Finger.Type.TYPE_INDEX.swigValue()] = index;
        fingers[Finger.Type.TYPE_MIDDLE.swigValue()] = middle;
        fingers[Finger.Type.TYPE_RING.swigValue()] = ring;
        fingers[Finger.Type.TYPE_PINKY.swigValue()] = pinky;
    }

    public LiteFinger getFinger(Finger.Type finger) {
        return fingers[finger.swigValue()];
    }
}
