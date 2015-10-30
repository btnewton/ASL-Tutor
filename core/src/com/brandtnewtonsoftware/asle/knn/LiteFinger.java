package com.brandtnewtonsoftware.asle.knn;

import com.leapmotion.leap.Bone;
import com.leapmotion.leap.Finger;

/**
 * Created by brandt on 10/28/15.
 */
public class LiteFinger {

    private Integer id;
    private final Finger.Type type;
    private final LiteBone metacarpal;
    private final LiteBone distal;

    public LiteFinger(Finger finger) {
        this.type = finger.type();
        metacarpal = new LiteBone(finger.bone(Bone.Type.TYPE_METACARPAL).nextJoint(), Bone.Type.TYPE_METACARPAL);
        distal = new LiteBone(finger.bone(Bone.Type.TYPE_DISTAL).nextJoint(), Bone.Type.TYPE_DISTAL);
    }

    public LiteFinger(int id, Finger.Type type, LiteBone metacarpal, LiteBone distal) {
        this(type, metacarpal, distal);
        this.id = id;
    }

    public LiteFinger(Finger.Type type, LiteBone metacarpal, LiteBone distal) {
        this.type = type;
        this.metacarpal = metacarpal;
        this.distal = distal;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public Finger.Type getType() {
        return type;
    }

    public LiteBone getMetacarpal() {
        return metacarpal;
    }

    public LiteBone getDistal() {
        return distal;
    }
}
