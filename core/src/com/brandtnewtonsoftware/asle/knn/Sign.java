package com.brandtnewtonsoftware.asle.knn;

import com.leapmotion.leap.Finger;

/**
 * Created by brandt on 10/28/15.
 */
public class Sign {

    private Integer id;
    private final String signValue;
    private final LiteFingerList fingerList;

    public Sign(int id, String signValue, LiteFingerList fingerList) {
        this(signValue, fingerList);
        this.id = id;
    }

    public Sign(String signValue, LiteFingerList fingerList) {
        this.signValue = signValue;
        this.fingerList = fingerList;
    }

    public Integer getId() {
        return id;
    }

    public String getSignValue() {
        return signValue;
    }

    public LiteFinger getFinger(Finger.Type finger) {
        return fingerList.getFinger(finger);
    }
}
