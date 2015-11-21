package com.brandtnewtonsoftware.asle.util;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

/**
 * Created by Brandt on 11/11/2015.
 */
public class MeasuredString {

    private String value;
    private float width;
    private float height;
    private final BitmapFont bitmapFont;

    public MeasuredString(BitmapFont bitmapFont) {
        this.bitmapFont = bitmapFont;
    }

    public void update(String value) {
        this.value = value;

        GlyphLayout glyphLayout = new GlyphLayout();
        glyphLayout.setText(bitmapFont, value);
        width = glyphLayout.width;
        height = glyphLayout.height;
    }

    public void draw(Batch batch, float x, float y) {
        bitmapFont.draw(batch, value != null? value : "", x, y);
    }

    public float getCenteredX(float x) {
        return x - width/2;
    }
    public float getCenteredY(float y) {
        return y + height/2;
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
