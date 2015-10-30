package com.brandtnewtonsoftware.asle.knn;

import com.leapmotion.leap.Bone;
import com.leapmotion.leap.Vector;

/**
 * Created by brandt on 10/28/15.
 */
class LiteBone {

    private Integer id;
    private Vector boneTip;
    private Bone.Type type;

    public LiteBone(int id, Vector boneTip, Bone.Type type) {
        this(boneTip, type);
        this.id = id;
    }

    public LiteBone(Vector boneTip, Bone.Type type) {
        this.boneTip = boneTip;
        this.type = type;
    }

    public Vector getBoneTip() {
        return boneTip;
    }

    public Bone.Type getType() {
        return type;
    }
}
