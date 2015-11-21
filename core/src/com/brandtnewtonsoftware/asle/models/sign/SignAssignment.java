package com.brandtnewtonsoftware.asle.models.sign;

import com.brandtnewtonsoftware.asle.models.SignPerformance;

/**
 * Created by Brandt on 11/9/2015.
 */
public final class SignAssignment {

    public final SignPerformance performance;
    public final Sign sign;

    public SignAssignment(SignPerformance performance) {
        this.performance = performance;
        sign = SignFactory.make(performance.getSignValue());
    }
}
