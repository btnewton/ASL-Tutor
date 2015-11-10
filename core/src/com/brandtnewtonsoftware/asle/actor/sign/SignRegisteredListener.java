package com.brandtnewtonsoftware.asle.actor.sign;

import com.brandtnewtonsoftware.asle.models.sign.Sign;

/**
 * Created by Brandt on 11/1/2015.
 */
public interface SignRegisteredListener {
    void onSignRegistered(Sign sign, boolean signCorrect);
}
