package com.brandtnewtonsoftware.asle.simulation.state;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.leapmotion.leap.Frame;

/**
 * Created by Brandt on 10/29/2015.
 */
public abstract class GameState {


    public abstract boolean checkState(Frame frame);
    public abstract void render(SpriteBatch batch);
}
