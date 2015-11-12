package com.brandtnewtonsoftware.asle.util;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

/**
 * Created by Brandt on 11/11/2015.
 */
public class MeasuredString {

    String value;
    private float width;
    private float height;

    public synchronized void update(String value, BitmapFont font) {
        this.value = value;

        GlyphLayout glyphLayout = new GlyphLayout();
        glyphLayout.setText(font, value);
        width = glyphLayout.width;
        height = glyphLayout.height;
    }

    public boolean isEmpty() {
        return value == null || value.isEmpty();
    }

    public float getHeight() {
        return height;
    }

    public String toString() {
        if (value != null)
            return value;
        return "";
    }

    public float getWidth() {
        return width;
    }
}
