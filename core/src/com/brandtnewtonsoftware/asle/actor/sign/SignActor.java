package com.brandtnewtonsoftware.asle.actor.sign;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;

import com.brandtnewtonsoftware.asle.ASLTutorGame;
import com.brandtnewtonsoftware.asle.models.RelativeHand;
import com.brandtnewtonsoftware.asle.leap.PrimaryHandListener;
import com.brandtnewtonsoftware.asle.models.sign.Sign;
import com.brandtnewtonsoftware.asle.models.sign.SignAssignment;
import com.brandtnewtonsoftware.asle.util.FontHelper;
import com.leapmotion.leap.Hand;

import java.awt.*;

/**
 * Created by Brandt on 11/1/2015.
 */
public class SignActor extends Actor implements PrimaryHandListener {

    private Texture signTexture;
    private Sign sign;
    private SignRegisteredListener listener;
    private BitmapFont font;
    private boolean showHelp;
    private boolean signComplete;

    private boolean leftHanded;

    public SignActor() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(FontHelper.getThinFont()));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        final int fontSize = FontHelper.getLargeFontSize();
        parameter.size = fontSize;
        parameter.color = Color.BLACK;
        font = generator.generateFont(parameter);
        generator.dispose();

        setPosition(Gdx.graphics.getWidth() / 2 - 35, Gdx.graphics.getHeight() / 2 + 45);

        showHelp = true;
    }

    public void changeSign(SignAssignment signAssignment) {
        sign = signAssignment.getSign();
        signTexture = new Texture(Gdx.files.internal(sign.getImageFileName()));
        signComplete = false;
    }

    public void setShowHelp(boolean showHelp) {
        this.showHelp = showHelp;
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        if (!isVisible())
            return;

        if (showHelp) {
            batch.setColor(getColor());
            batch.getColor().a *= parentAlpha;
            batch.draw(signTexture, getX(), getY(), getOriginX(), getOriginY(), getWidth(),
                    getHeight(), getScaleX(), getScaleY(), getRotation(), 0, 0,
                    signTexture.getWidth(), signTexture.getHeight(), leftHanded, false);
            batch.setColor(Color.BLACK);
        }

        font.draw(batch, Integer.toString(sign.getValue()), getX(), getY());
    }

    public void setListener(SignRegisteredListener listener) {
        this.listener = listener;
    }

    public void setSignComplete() {
        signComplete = true;
    }

    @Override
    public void onPrimaryHandUpdated(Hand hand) {
        leftHanded = hand.isLeft();
        if (!signComplete) {
            RelativeHand relativeHand = ASLTutorGame.getHandCalibrator().evaluateHand(hand);
            listener.onSignRegistered(sign, sign.equals(relativeHand));
        }
    }
}
