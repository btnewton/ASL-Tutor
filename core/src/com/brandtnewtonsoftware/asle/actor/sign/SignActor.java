package com.brandtnewtonsoftware.asle.actor.sign;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;

import com.brandtnewtonsoftware.asle.ASLTutorGame;
import com.brandtnewtonsoftware.asle.models.RelativeHand;
import com.brandtnewtonsoftware.asle.leap.PrimaryHandListener;
import com.brandtnewtonsoftware.asle.models.sign.SignAssignment;
import com.brandtnewtonsoftware.asle.util.FontHelper;
import com.brandtnewtonsoftware.asle.util.MeasuredString;
import com.leapmotion.leap.Hand;

/**
 * Created by Brandt on 11/1/2015.
 */
public class SignActor extends Actor implements PrimaryHandListener {

    private Sprite signSprite;
    private SignAssignment signAssignment;
    private SignRegisteredListener listener;

    private boolean showSign;
    private boolean signComplete;

    BitmapFont font;

    private boolean leftHanded;

    public SignActor() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(FontHelper.getThinFont()));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = FontHelper.getSmallFontSize();
        parameter.color = Color.BLACK;
        font = generator.generateFont(parameter);
        generator.dispose();

        setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
    }

    public void changeSign(SignAssignment signAssignment) {
        this.signAssignment = signAssignment;
        Texture signTexture = new Texture(Gdx.files.internal(signAssignment.sign.getImageFileName()));
        signSprite = new Sprite(signTexture);
        signSprite.setPosition(Gdx.graphics.getWidth() /5, Gdx.graphics.getHeight() /2 - signSprite.getHeight() /2);
        signSprite.setBounds(signSprite.getX(), signSprite.getY(), signSprite.getWidth(), signSprite.getHeight());
        signSprite.flip(leftHanded, false);
        signComplete = false;

//        signText.update(Character.toString(signAssignment.sign.getValue()));
//        proficiency.update("Proficiency: " + signAssignment.performance.getProficiencyRating());
    }

    public void setShowSign(boolean showSign) {
        this.showSign = showSign;
    }

    @Override
    public void draw(Batch batch, float parentAlpha){

        if (showSign) {
            signSprite.draw(batch);
        }

        font.draw(batch, "SDF", Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
    }

    public void setListener(SignRegisteredListener listener) {
        this.listener = listener;
    }

    public void setSignComplete() {
        signComplete = true;
    }

    @Override
    public void onPrimaryHandUpdated(Hand hand) {

        if (signSprite != null && leftHanded == hand.isLeft()) {
            signSprite.flip(hand.isLeft(), false);
        }
        leftHanded = hand.isLeft();

        if (!signComplete) {
            RelativeHand relativeHand = ASLTutorGame.getHandCalibrator().evaluateHand(hand);
            listener.onSignRegistered(signAssignment.sign, signAssignment.sign.equals(relativeHand));
        }
    }
}
