package com.brandtnewtonsoftware.asle.simulation.state;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Vector;


/**
 * Created by Brandt on 10/29/2015.
 */
public class OuterHandState extends GameState {


    @Override
    public boolean checkState(Frame frame) {
        Vector palmPosition = frame.hands().rightmost().palmPosition();
        double distanceFromCenterStage = Math.hypot(palmPosition.getX(), palmPosition.getZ());
        return distanceFromCenterStage > InStageState.STAGE_RADIUS;
    }

    @Override
    public void render(SpriteBatch batch) {

    }
}
