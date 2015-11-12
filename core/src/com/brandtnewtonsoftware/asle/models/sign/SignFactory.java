package com.brandtnewtonsoftware.asle.models.sign;

/**
 * Created by Brandt on 11/1/2015.
 */
public class SignFactory {

    public static Sign make(String value) {
        Sign sign;
        try {
            sign = (Sign) Class.forName("com.brandtnewtonsoftware.asle.models.sign.Sign" + value).getConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            sign = null;
        }
        return sign;
    }
}
