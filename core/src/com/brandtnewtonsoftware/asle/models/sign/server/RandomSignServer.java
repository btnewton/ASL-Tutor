package com.brandtnewtonsoftware.asle.models.sign.server;

import com.brandtnewtonsoftware.asle.models.sign.Sign;
import com.brandtnewtonsoftware.asle.models.sign.SignFactory;

import java.util.Random;

/**
 * Created by Brandt on 11/8/2015.
 */
public class RandomSignServer implements ISignServer {

    private Random random = new Random();
    private Integer lastSignValue;

    private int newRandomValue() {
        int sign = new Random().nextInt(10);
        if (lastSignValue != null && sign == lastSignValue) {
            sign = newRandomValue();
        }
        return sign;
    }

    @Override
    public Sign getSignValue() {
        int signValue = newRandomValue();
        lastSignValue = signValue;
        return SignFactory.make(signValue);
    }
}
