package com.brandtnewtonsoftware.asle.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.Random;

/**
 * Created by Brandt on 11/1/2015.
 */
public class SuccessActor extends Actor {

    private Texture texture;

    public SuccessActor() {
        texture = new Texture(Gdx.files.internal("img/ic_success.png"));
        setPosition(Gdx.graphics.getWidth() - texture.getWidth() - 15, Gdx.graphics.getHeight() - texture.getHeight() - 15);
        setBounds(getX(), getY(), texture.getWidth(), texture.getHeight());
    }

    @Override
    public void draw(Batch batch, float alpha){
        Color c = this.getColor();
        batch.setColor(c);
        // always make sure to only multiply by the parent alpha
        batch.getColor().a *= alpha;
        batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(), getWidth(),
                getHeight(), getScaleX(), getScaleY(), getRotation(), 0, 0,
                texture.getWidth(), texture.getHeight(), false, false);
        batch.setColor(Color.BLACK);
    }
}
