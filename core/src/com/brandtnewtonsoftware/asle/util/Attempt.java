package com.brandtnewtonsoftware.asle.util;

import com.brandtnewtonsoftware.asle.sign.Sign;

/**
 * Created by Brandt on 11/4/2015.
 */
public class Attempt {

    private Sign sign;
    private int attemptCount;
    private int successCount;

    public Attempt(Sign sign, int attemptCount, int successCount) {
        this.sign = sign;
        this.attemptCount = attemptCount;
        this.successCount = successCount;
    }

    public int getAttemptCount() {
        return attemptCount;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public void addSuccessfulSign() {
        attemptCount++;
        successCount++;
    }
    public void addUnsuccessfulSign() {
        attemptCount++;
    }

    public Sign getSign() {
        return sign;
    }
}
