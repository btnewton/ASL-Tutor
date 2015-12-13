package com.brandtnewtonsoftware.asle.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.brandtnewtonsoftware.asle.util.Arc;
import com.brandtnewtonsoftware.asle.util.FontHelper;
import com.brandtnewtonsoftware.asle.util.Stopwatch;

import java.text.DecimalFormat;

/**
 * Created by Brandt on 11/9/2015.
 */
public class ArcTimerActor extends Actor {

    private static DecimalFormat formatter = new DecimalFormat("0.0");
    private long timeLimit;
    private Stopwatch stopwatch;
    private BitmapFont font;
    private float fontOffset;
    private Arc shapeRenderer;
    public static final int DIAMETER = 576;

    public ArcTimerActor(Stopwatch stopwatch, int timeLimit) {
        final int WIDTH = Gdx.graphics.getWidth();
        final int HEIGHT = Gdx.graphics.getHeight();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(FontHelper.getThinFont()));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = FontHelper.getSmallFontSize();
        parameter.color = Color.BLACK;
        font = generator.generateFont(parameter);
        generator.dispose();

        shapeRenderer = new Arc();

        GlyphLayout layout = new GlyphLayout();
        layout.setText(font, "0.0");
        fontOffset = layout.width / 2;

        this.timeLimit = timeLimit;
        this.stopwatch = stopwatch;

        setSize(DIAMETER, DIAMETER);
        setPosition(WIDTH /2, HEIGHT /2);
    }

    public void setTimeLimit(long timeLimit) {
        this.timeLimit = timeLimit;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        double timeRemaining = timeLimit - stopwatch.getTimeElapsed();
        if (timeRemaining < 0)
            timeRemaining = 0;

        float radiusMultiplier = (float) (timeRemaining / timeLimit);

        Color color = getColor();
        color.a *= parentAlpha;
        batch.setColor(color);

        batch.end();

        Gdx.gl20.glEnable(GL20.GL_BLEND);
        Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(new Color(0, 0, 0, .5f));
        float radius = getWidth()/2;
        try {
            shapeRenderer.arc(getX(), getY(), radius, 0, 360 * radiusMultiplier);
        } catch (Exception e){

        }

        shapeRenderer.end();
        Gdx.gl20.glDisable(GL20.GL_BLEND);

        batch.begin();

        String seconds = formatter.format(timeRemaining / 1000.0);
        font.draw(batch, seconds, Gdx.graphics.getWidth() / 2 - fontOffset, Gdx.graphics.getHeight() / 10);
    }
}
