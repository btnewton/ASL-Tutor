package com.brandtnewtonsoftware.asle.stage;

import com.badlogic.gdx.Gdx;
import com.brandtnewtonsoftware.asle.ASLEGame;
import com.brandtnewtonsoftware.asle.actor.HandNotPresentActor;
import com.brandtnewtonsoftware.asle.actor.ProximityLeapActor;
import com.brandtnewtonsoftware.asle.actor.StagePresenceListener;
import com.brandtnewtonsoftware.asle.leap.HandCountListener;
import com.brandtnewtonsoftware.asle.leap.LeapListener;
import com.brandtnewtonsoftware.asle.leap.PrimaryHandPositionListener;
import com.brandtnewtonsoftware.asle.simulation.state.*;
import com.leapmotion.leap.Vector;

import java.util.LinkedList;
import java.util.List;

/**
 * Prompts user to move hand over leap motion device
 */
public class PromptEntranceStage extends GameState implements PrimaryHandPositionListener, StagePresenceListener, HandCountListener {

    private List<PrimaryHandPositionListener> primaryHandPositionListeners = new LinkedList<>();
    private List<HandCountListener> handCountListeners = new LinkedList<>();

    public PromptEntranceStage(ASLEGame game) {
        super(game);
        LeapListener listener = game.getListener();
        listener.addHandCountListener(this);
        listener.addPrimaryHandPositionListener(this);

        final ProximityLeapActor proximityLeapActor = new ProximityLeapActor();
        primaryHandPositionListeners.add(proximityLeapActor);
        proximityLeapActor.addStagePresenceListener(this);
        stage.addActor(proximityLeapActor);

        final HandNotPresentActor handNotPresentActor = new HandNotPresentActor();
        handCountListeners.add(handNotPresentActor);
        stage.addActor(handNotPresentActor);
    }

    @Override
    public void dispose() {
        getGame().getListener().removeHandCountListener(this);
        getGame().getListener().removePrimaryHandPositionListener(this);
    }

    @Override
    public void onHandCountChange(int handCount) {
        for (HandCountListener listener : handCountListeners) {
            listener.onHandCountChange(handCount);
        }
    }

    @Override
    public void onPrimaryHandUpdated(Vector handPosition) {
        for (PrimaryHandPositionListener listener : primaryHandPositionListeners) {
            listener.onPrimaryHandUpdated(handPosition);
        }
    }

    @Override
    public void onStagePresenceChange(boolean onStage) {
        if (onStage) {
            Gdx.app.postRunnable(() -> getGame().setGameState(new TestStage(getGame())));
        }
    }
}
