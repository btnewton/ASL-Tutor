package com.brandtnewtonsoftware.asle.models.sign;

/**
 * Created by Brandt on 11/9/2015.
 */
public final class SignAssignment {

    private Sign sign;
    private Integer hintTimeLimit;

    public SignAssignment(Sign sign) {
        this(sign, null);
    }

    public SignAssignment(Sign sign, int hintTimeLimit) {
        this(sign, (Integer) hintTimeLimit);
    }

    private SignAssignment(Sign sign, Integer hintTimeLimit) {
        this.sign = sign;
        this.hintTimeLimit = hintTimeLimit;
    }

    public boolean hasHintTimeLimit() {
        return hintTimeLimit != null;
    }

    public int getHintTimeLimit() {
        return hintTimeLimit;
    }

    public Sign getSign() {
        return sign;
    }
}
