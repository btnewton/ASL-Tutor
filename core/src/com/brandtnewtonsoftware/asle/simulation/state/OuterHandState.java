package com.brandtnewtonsoftware.asle.simulation.state;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.brandtnewtonsoftware.asle.leap.LeapHelper;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Vector;


/**
 * Created by Brandt on 10/29/2015.
 */
public class OuterHandState extends GameState {


    @Override
    public boolean checkState(Frame frame) {
        Hand defaultHand = LeapHelper.getDefaultHand(frame.hands());
        return defaultHand != null && !LeapHelper.inCenterStage(defaultHand.palmPosition());
    }

    @Override
    public void render(SpriteBatch batch) {

    }
}
