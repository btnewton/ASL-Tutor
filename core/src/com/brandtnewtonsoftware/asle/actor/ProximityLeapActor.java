package com.brandtnewtonsoftware.asle.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.brandtnewtonsoftware.asle.leap.HandCountListener;
import com.brandtnewtonsoftware.asle.leap.PrimaryHandPositionListener;
import com.brandtnewtonsoftware.asle.leap.LeapHelper;
import com.brandtnewtonsoftware.asle.leap.LeapListener;
import com.leapmotion.leap.Leap;
import com.leapmotion.leap.Vector;


/**
 * Created by Brandt on 10/31/2015.
 */
public class ProximityLeapActor extends Actor implements PrimaryHandPositionListener {

    Texture texture = new Texture(Gdx.files.internal("data/ic_leap.png"));

    public ProximityLeapActor(LeapListener leapListener) {
        setPosition(Gdx.graphics.getWidth() / 2 - texture.getWidth() / 2, Gdx.graphics.getHeight() / 3 - texture.getHeight() / 2);
        setBounds(getX(), getY(), texture.getWidth(), texture.getHeight());
        setScale(.78f);

        leapListener.addPrimaryHandPositionListener(this);
    }

    @Override
    public void draw(Batch batch, float alpha) {

        batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(), getWidth(),
                getHeight(), getScaleX(), getScaleY(), getRotation(), 0, 0,
                texture.getWidth(), texture.getHeight(), false, false);
    }

    public void dispose() {

    }

    @Override
    public void onPrimaryHandUpdated(Vector handPosition) {
        float scale;
        double distanceFromCenterStage = LeapHelper.getDistanceToCenterStage(handPosition);

        if (LeapHelper.inCenterStage(distanceFromCenterStage)) {
            scale = 1;
        } else {
            scale = (float) ((LeapHelper.OUTER_RADIUS - distanceFromCenterStage) / ((LeapHelper.OUTER_RADIUS - LeapHelper.STAGE_RADIUS) * 2)) + .5f;
        }

        setScale(scale);
    }
}
