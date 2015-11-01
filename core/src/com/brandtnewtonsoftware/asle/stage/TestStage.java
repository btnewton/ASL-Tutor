package com.brandtnewtonsoftware.asle.stage;

import com.badlogic.gdx.Gdx;
import com.brandtnewtonsoftware.asle.ASLEGame;
import com.brandtnewtonsoftware.asle.knn.Sign;
import com.brandtnewtonsoftware.asle.leap.HandCountListener;
import com.brandtnewtonsoftware.asle.leap.LeapListener;
import com.brandtnewtonsoftware.asle.leap.PrimaryHandPositionListener;
import com.brandtnewtonsoftware.asle.simulation.state.GameState;
import com.leapmotion.leap.Vector;

import java.util.Random;

/**
 * Asks user to perform sign
 */
public class TestStage extends GameState implements PrimaryHandPositionListener, HandCountListener {

    public TestStage(ASLEGame game) {
        super(game);
        LeapListener listener = game.getListener();
        listener.addHandCountListener(this);
        listener.addPrimaryHandPositionListener(this);
    }


    @Override
    public void dispose() {
        LeapListener listener = getGame().getListener();
        listener.removePrimaryHandPositionListener(this);
        listener.removeHandCountListener(this);
    }

    @Override
    public void onHandCountChange(int handCount) {
        if (handCount == 0) {
            Gdx.app.postRunnable(() -> {
                ASLEGame game = getGame();
                game.setGameState(new PromptEntranceStage(game));
            });
        }
    }

    @Override
    public void onPrimaryHandUpdated(Vector handPosition) {

    }
}
