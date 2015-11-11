package com.brandtnewtonsoftware.asle.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Brandt on 11/1/2015.
 */
public class SuccessActor extends Actor {

    private Texture texture;
    private Long flashStart;

    public SuccessActor() {
        texture = new Texture(Gdx.files.internal("img/ic_success.png"));
        setPosition(Gdx.graphics.getWidth() - texture.getWidth() - 15, Gdx.graphics.getHeight() - texture.getHeight() - 15);
        setBounds(getX(), getY(), texture.getWidth(), texture.getHeight());
    }

    public void flash() {
        if (!isVisible()) {
            setVisible(true);
        }
        flashStart = System.currentTimeMillis();
    }

    public void endFlash() {
        flashStart = null;
        setVisible(false);
    }

    public boolean isFlashing() {
        if (flashStart != null) {
            final long FLASH_DURATION = 2000;
            if (System.currentTimeMillis() - flashStart >= FLASH_DURATION) {
                endFlash();
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public void draw(Batch batch, float alpha){
        if (isVisible() && isFlashing()) {
            batch.setColor(getColor());
            // always make sure to only multiply by the parent alpha
            batch.getColor().a *= alpha;
            batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(), getWidth(),
                    getHeight(), getScaleX(), getScaleY(), getRotation(), 0, 0,
                    texture.getWidth(), texture.getHeight(), false, false);
            batch.setColor(Color.BLACK);
        }
    }
}
