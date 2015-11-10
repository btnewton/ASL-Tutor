package com.brandtnewtonsoftware.asle.util;

/**
 * Created by Brandt on 11/9/2015.
 */
public class FontHelper {

    private static final String FONT_PATH = "fonts/";

    private static final String REGULAR = "Roboto-Regular.ttf";
    private static final String THIN = "Roboto-Thin.ttf";
    private static final String MEDIUM = "Roboto-Medium.ttf";

    private static int largeFontSize = 150;
    private static int regularFontSize = 100;
    private static int smallFontSize = 75;

    public static int getLargeFontSize() {
        return largeFontSize;
    }

    public static int getSmallFontSize() {
        return smallFontSize;
    }

    public static int getRegularFontSize() {
        return regularFontSize;
    }

    public static String getThinFont() {
        return FONT_PATH + THIN;
    }

    public static String getRegularFont() {
        return FONT_PATH + REGULAR;
    }

    public static String getMediumFont() {
        return FONT_PATH + MEDIUM;
    }


}
