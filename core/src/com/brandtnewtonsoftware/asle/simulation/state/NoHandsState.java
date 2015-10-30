package com.brandtnewtonsoftware.asle.simulation.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.leapmotion.leap.Frame;

/**
 * Created by Brandt on 10/29/2015.
 */
public class NoHandsState extends GameState {

    @Override
    public boolean checkState(Frame frame) {
        return frame.hands().count() == 0;
    }

    @Override
    public void render(SpriteBatch batch) {
        Texture texture = new Texture(Gdx.files.internal("data/ic_leap.png"));
        Sprite sprite = new Sprite(texture);
        float x = (Gdx.graphics.getWidth() / 2) - (texture.getWidth() / 2);
        float y = (Gdx.graphics.getHeight() / 2) - (texture.getHeight() * 2);
        sprite.setPosition(x, y);
        sprite.draw(batch);
    }
}
