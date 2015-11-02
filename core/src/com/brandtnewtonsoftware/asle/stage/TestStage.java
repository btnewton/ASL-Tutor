package com.brandtnewtonsoftware.asle.stage;

import com.badlogic.gdx.Gdx;
import com.brandtnewtonsoftware.asle.ASLEGame;
import com.brandtnewtonsoftware.asle.actor.SignActor;
import com.brandtnewtonsoftware.asle.actor.SignRegisteredListener;
import com.brandtnewtonsoftware.asle.actor.SuccessActor;
import com.brandtnewtonsoftware.asle.leap.HandCountListener;
import com.brandtnewtonsoftware.asle.leap.LeapListener;
import com.brandtnewtonsoftware.asle.leap.PrimaryHandListener;
import com.brandtnewtonsoftware.asle.sign.Sign;
import com.brandtnewtonsoftware.asle.simulation.state.GameState;
import com.leapmotion.leap.Hand;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

/**
 * Asks user to perform sign
 */
public class TestStage extends GameState implements PrimaryHandListener, HandCountListener, SignRegisteredListener, ActionListener {

    private List<PrimaryHandListener> primaryHandPositionListeners = new LinkedList<>();
    private SuccessActor successActor;
    private SignActor signActor;
    private Timer timer;

    public TestStage(ASLEGame game) {
        super(game);
        LeapListener listener = game.getListener();
        listener.addHandCountListener(this);
        listener.addPrimaryHandListener(this);

        signActor = new SignActor();
        signActor.setListener(this);
        primaryHandPositionListeners.add(signActor);
        stage.addActor(signActor);

        successActor = new SuccessActor();
        successActor.setVisible(false);
        stage.addActor(successActor);
        timer = new Timer(1000, this);
    }

    @Override
    public void dispose() {
        LeapListener listener = getGame().getListener();
        listener.removePrimaryHandListener(this);
        listener.removeHandCountListener(this);
    }

    @Override
    public void onHandCountChange(int handCount) {
        if (handCount == 0) {
            if (timer.isRunning()) {
                timer.stop();
            }
            Gdx.app.postRunnable(() -> {
                ASLEGame game = getGame();
                game.setGameState(new PromptEntranceStage(game));
            });
        }
    }

    @Override
    public void onPrimaryHandUpdated(Hand hand) {
        for (PrimaryHandListener listener : primaryHandPositionListeners) {
            listener.onPrimaryHandUpdated(hand);
        }
    }

    @Override
    public void onSignRegistered(Sign sign, boolean signCorrect) {
        if (signCorrect) {
            if (!timer.isRunning()) {
                signActor.setVisible(false);
                successActor.setVisible(true);
                timer.restart();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Gdx.app.postRunnable(() -> {
            timer.stop();
            successActor.setVisible(false);
            signActor.changeSign();
            signActor.setVisible(true);
        });
    }
}
