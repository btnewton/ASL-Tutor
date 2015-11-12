package com.brandtnewtonsoftware.asle.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.brandtnewtonsoftware.asle.util.FontHelper;
import com.brandtnewtonsoftware.asle.util.MeasuredString;

/**
 * Created by Brandt on 11/1/2015.
 */
public class HudActor extends Actor {

    private BitmapFont regfont;
    private BitmapFont smfont;

    private MeasuredString streakText;
    private MeasuredString scoreText;

    public HudActor() {
        FreeTypeFontGenerator thinGenerator = new FreeTypeFontGenerator(Gdx.files.internal(FontHelper.getThinFont()));
        FreeTypeFontGenerator regularGenerator = new FreeTypeFontGenerator(Gdx.files.internal(FontHelper.getRegularFont()));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = FontHelper.getRegularFontSize();
        parameter.color = Color.BLACK;
        regfont = regularGenerator.generateFont(parameter);
        parameter.size = FontHelper.getSmallFontSize();
        smfont = thinGenerator.generateFont(parameter);
        regularGenerator.dispose();
        thinGenerator.dispose();

        streakText = new MeasuredString();
        scoreText = new MeasuredString();
    }

    @Override
    public void draw(Batch batch, float alpha){
        float scoreX = Gdx.graphics.getWidth() - scoreText.getWidth() - 20;
        float scoreY = Gdx.graphics.getHeight() - scoreText.getHeight() - 20;
        regfont.draw(batch, scoreText.toString(), scoreX, scoreY);

        if (!streakText.isEmpty()) {
            smfont.draw(batch, streakText.toString(), Gdx.graphics.getWidth() - streakText.getWidth() - 20, scoreY - streakText.getHeight() - 80);
        }
    }

    public void setScoreText(String scoreText) {
        this.scoreText.update(scoreText, regfont);
    }

    public void setStreakText(String streakText) {
        this.streakText.update(streakText, smfont);
    }
}
