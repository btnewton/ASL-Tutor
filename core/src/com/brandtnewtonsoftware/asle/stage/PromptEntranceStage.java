package com.brandtnewtonsoftware.asle.stage;

import com.badlogic.gdx.Gdx;
import com.brandtnewtonsoftware.asle.ASLEGame;
import com.brandtnewtonsoftware.asle.actor.HandNotPresentActor;
import com.brandtnewtonsoftware.asle.actor.ProximityLeapActor;
import com.brandtnewtonsoftware.asle.actor.StagePresenceListener;
import com.brandtnewtonsoftware.asle.leap.HandCountListener;
import com.brandtnewtonsoftware.asle.leap.LeapListener;
import com.brandtnewtonsoftware.asle.leap.PrimaryHandListener;
import com.brandtnewtonsoftware.asle.simulation.state.*;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Vector;

import java.util.LinkedList;
import java.util.List;

/**
 * Prompts user to move hand over leap motion device
 */
public class PromptEntranceStage extends GameState implements PrimaryHandListener, StagePresenceListener, HandCountListener {

    private List<PrimaryHandListener> primaryHandPositionListeners = new LinkedList<>();
    private List<HandCountListener> handCountListeners = new LinkedList<>();

    public PromptEntranceStage(ASLEGame game) {
        super(game);
        LeapListener listener = game.getListener();
        listener.addHandCountListener(this);
        listener.addPrimaryHandListener(this);

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
        getGame().getListener().removePrimaryHandListener(this);
    }

    @Override
    public void onHandCountChange(int handCount) {
        for (HandCountListener listener : handCountListeners) {
            listener.onHandCountChange(handCount);
        }
    }

    @Override
    public void onStagePresenceChange(boolean onStage) {
        if (onStage) {
            Gdx.app.postRunnable(() -> getGame().setGameState(new TestStage(getGame())));
        }
    }

    @Override
    public void onPrimaryHandUpdated(Hand hand) {
        for (PrimaryHandListener listener : primaryHandPositionListeners) {
            listener.onPrimaryHandUpdated(hand);
        }
    }
}
