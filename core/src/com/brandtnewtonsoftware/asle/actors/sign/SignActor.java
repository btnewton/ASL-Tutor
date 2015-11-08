package com.brandtnewtonsoftware.asle.actors.sign;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;

import com.brandtnewtonsoftware.asle.ASLTutorGame;
import com.brandtnewtonsoftware.asle.models.RelativeHand;
import com.brandtnewtonsoftware.asle.leap.PrimaryHandListener;
import com.brandtnewtonsoftware.asle.sign.Sign;
import com.brandtnewtonsoftware.asle.models.Attempt;
import com.brandtnewtonsoftware.asle.util.Stopwatch;
import com.leapmotion.leap.Hand;

import java.util.Random;

/**
 * Created by Brandt on 11/1/2015.
 */
public class SignActor extends Actor implements PrimaryHandListener {

    private Texture texture;
    private Sign sign;
    private SignRegisteredListener listener;
    private BitmapFont font;
    private Attempt attempt;
    private boolean showHelp;
    private Stopwatch stopwatch;

    public SignActor() {
        font = new BitmapFont();
        font.setColor(Color.BLACK);
        stopwatch = new Stopwatch();
        showHelp = true;

        changeSign();
    }

    public void changeSign() {
        int sign = new Random().nextInt(10);
        if (this.sign == null || sign != this.sign.getValue()) {
            changeSign(sign);
        } else {
            changeSign();
        }
    }
    public void changeSign(int signValue) {
        try {
            sign = (Sign) Class.forName("com.brandtnewtonsoftware.asle.sign.Sign" + signValue).getConstructor().newInstance();
        } catch (Exception e) {
            System.err.print("Could not reflect sign " + signValue);
            e.printStackTrace();
            sign = null;
        }

        if (sign != null) {
            texture = new Texture(Gdx.files.internal(sign.getImageFileName()));
            setPosition(Gdx.graphics.getWidth() - texture.getWidth() - 15, Gdx.graphics.getHeight() - texture.getHeight() - 15);
            setBounds(getX(), getY(), texture.getWidth(), texture.getHeight());
            attempt = new Attempt(sign);
            stopwatch.reset();
            stopwatch.start();
        }
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (!visible && stopwatch.isRunning()) {
            stopwatch.stop();
        } else if (visible && !stopwatch.isRunning()) {
            stopwatch.start();
        }
    }

    public void setShowHelp(boolean showHelp) {
        this.showHelp = showHelp;
    }

    public Attempt getAttempt() {
        return attempt;
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        if (!isVisible())
            return;

        Color c = this.getColor();
        batch.setColor(c);

        batch.getColor().a *= parentAlpha;
        batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(), getWidth(),
                getHeight(), getScaleX(), getScaleY(), getRotation(), 0, 0,
                texture.getWidth(), texture.getHeight(), false, false);
        batch.setColor(Color.BLACK);

        if (showHelp) {
            font.draw(batch, Integer.toString(sign.getValue()), getX() - 40, getY());
        }
    }

    public void setListener(SignRegisteredListener listener) {
        this.listener = listener;
    }

    @Override
    public void onPrimaryHandUpdated(Hand hand) {
        if (!attempt.isCompleted()) {
            RelativeHand relativeHand = ASLTutorGame.getHandCalibrator().evaluateHand(hand);
            boolean signCorrect = sign.equals(relativeHand);
            if (signCorrect) {
                stopwatch.stop();
                attempt.setTimeToComplete((int) stopwatch.getTimeElapsed());
            }
            listener.onSignRegistered(sign, sign.equals(relativeHand));
        }
    }
}
