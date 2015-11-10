package com.brandtnewtonsoftware.asle.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.brandtnewtonsoftware.asle.util.FontHelper;

import javax.swing.*;
import java.text.DecimalFormat;

/**
 * Created by Brandt on 11/9/2015.
 */
public class BubbleTimerActor extends Actor {

    private Sprite circle;
    private static DecimalFormat formatter = new DecimalFormat("0.0#");
    private long timeLimit;
    private Long startTime;
    private BitmapFont font;

    public BubbleTimerActor() {
        final int WIDTH = Gdx.graphics.getWidth();
        final int HEIGHT = Gdx.graphics.getHeight();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(FontHelper.getThinFont()));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = FontHelper.getSmallFontSize();
        parameter.color = Color.BLACK;
        font = generator.generateFont(parameter);
        generator.dispose();

        Texture texture = new Texture(Gdx.files.internal("img/white_circle.png"));
        circle = new Sprite(texture);
        circle.setAlpha(0.3f);
        circle.setSize(HEIGHT * .9f, HEIGHT * .9f);
        circle.setCenter(circle.getWidth()/2, circle.getHeight()/2);

        circle.setPosition(WIDTH / 2 - circle.getWidth() /2, HEIGHT / 2 - circle.getHeight()/2);
        circle.setBounds(circle.getX(), circle.getY(), circle.getWidth(), circle.getHeight());
    }

    public void reset(long timeLimit) {
        startTime = null;
        this.timeLimit = timeLimit;
    }

    public void start() {

        startTime = System.currentTimeMillis();
    }

    public boolean isRunning() {
        return startTime != null;
    }

    public long timeElapsed() {
        long timeElapsed = 0;
        if (isRunning()) {
            timeElapsed = System.currentTimeMillis() - startTime;
        }
        return timeElapsed;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        double timeRemaining = timeLimit - timeElapsed();
        if (timeRemaining < 0)
            timeRemaining = 0;

        float radiusMultiplier = (float) (timeRemaining / timeLimit);
        circle.draw(batch);
        circle.setScale(radiusMultiplier);

        double seconds = timeRemaining / 1000.0;
        font.draw(batch, formatter.format(seconds), Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/10);
    }
}
