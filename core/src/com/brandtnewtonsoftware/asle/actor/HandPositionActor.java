package com.brandtnewtonsoftware.asle.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by brandt on 11/16/15.
 */
public class HandPositionActor extends Actor {

    private Sprite circle;

    public HandPositionActor() {
        final int WIDTH = Gdx.graphics.getWidth();
        final int HEIGHT = Gdx.graphics.getHeight();

        Texture texture = new Texture(Gdx.files.internal("img/white_circle.png"));
        circle = new Sprite(texture);
        circle.setAlpha(0.3f);
        circle.setSize(ArcTimerActor.DIAMETER + 25, ArcTimerActor.DIAMETER + 25);
        circle.setCenter(circle.getHeight()/2, circle.getHeight()/2);

        circle.setOrigin(circle.getWidth() / 2, circle.getHeight() / 2);
        circle.setPosition(WIDTH / 2 - circle.getWidth() /2, HEIGHT / 2 - circle.getHeight()/2);
        circle.setBounds(circle.getX(), circle.getY(), circle.getWidth(), circle.getHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        circle.draw(batch);
        // TODO if off stage scale down proportional to distance from center stage, down to size of a quarter.
//        circle.setScale(radiusMultiplier);
    }

}
