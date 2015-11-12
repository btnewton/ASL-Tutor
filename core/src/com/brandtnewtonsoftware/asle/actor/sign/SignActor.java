package com.brandtnewtonsoftware.asle.actor.sign;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;

import com.brandtnewtonsoftware.asle.ASLTutorGame;
import com.brandtnewtonsoftware.asle.models.RelativeHand;
import com.brandtnewtonsoftware.asle.leap.PrimaryHandListener;
import com.brandtnewtonsoftware.asle.models.sign.Sign;
import com.brandtnewtonsoftware.asle.models.sign.SignAssignment;
import com.brandtnewtonsoftware.asle.util.FontHelper;
import com.leapmotion.leap.Hand;

/**
 * Created by Brandt on 11/1/2015.
 */
public class SignActor extends Actor implements PrimaryHandListener {

    private Sprite signSprite;
    private Sign sign;
    private SignRegisteredListener listener;
    private BitmapFont font;
    private boolean showSign;
    private boolean signComplete;
    private float xFontOffset;
    private float yFontOffset;

    private boolean leftHanded;

    public SignActor() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(FontHelper.getThinFont()));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = FontHelper.getLargeFontSize();
        parameter.color = Color.BLACK;
        font = generator.generateFont(parameter);
        generator.dispose();

        setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
    }

    public void changeSign(SignAssignment signAssignment) {
        sign = signAssignment.getSign();
        Texture signTexture = new Texture(Gdx.files.internal(sign.getImageFileName()));
        signSprite = new Sprite(signTexture);
        signSprite.setPosition(Gdx.graphics.getWidth() /5, Gdx.graphics.getHeight() /2 - signSprite.getHeight() /2);
        signSprite.setBounds(signSprite.getX(), signSprite.getY(), signSprite.getWidth(), signSprite.getHeight());
        signSprite.flip(leftHanded, false);
        signComplete = false;

        GlyphLayout layout = new GlyphLayout();
        layout.setText(font, Character.toString(sign.getValue()));
        xFontOffset = layout.width / 2;
        yFontOffset = layout.height / 2;
    }

    public void setShowSign(boolean showSign) {
        this.showSign = showSign;
    }

    @Override
    public void draw(Batch batch, float parentAlpha){

//        TODO remove comments
        if (showSign) {
            signSprite.draw(batch);
        }

        font.draw(batch, Character.toString(sign.getValue()), getX() - xFontOffset, getY() + yFontOffset);
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
            listener.onSignRegistered(sign, sign.equals(relativeHand));
        }
    }
}
