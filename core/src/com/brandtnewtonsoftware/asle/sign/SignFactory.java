package com.brandtnewtonsoftware.asle.sign;

/**
 * Created by Brandt on 11/1/2015.
 */
public class SignFactory {


    public static Sign Make(int value) {
        Sign sign;
        try {
            sign = (Sign) Class.forName(" com.brandtnewtonsoftware.asle.sign.Sign" + value).getConstructor().newInstance();
        } catch (Exception e) {
            sign = null;
        }
        return sign;
    }
}
