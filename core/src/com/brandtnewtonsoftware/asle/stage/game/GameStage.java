package com.brandtnewtonsoftware.asle.stage.game;

import com.badlogic.gdx.Gdx;
import com.brandtnewtonsoftware.asle.ASLTutorGame;
import com.brandtnewtonsoftware.asle.actor.BubbleTimerActor;
import com.brandtnewtonsoftware.asle.actor.GridOverlayActor;
import com.brandtnewtonsoftware.asle.models.User;
import com.brandtnewtonsoftware.asle.actor.sign.SignActor;
import com.brandtnewtonsoftware.asle.actor.sign.SignRegisteredListener;
import com.brandtnewtonsoftware.asle.actor.SuccessActor;
import com.brandtnewtonsoftware.asle.leap.HandCountListener;
import com.brandtnewtonsoftware.asle.leap.LeapListener;
import com.brandtnewtonsoftware.asle.leap.PrimaryHandListener;
import com.brandtnewtonsoftware.asle.models.sign.Sign;
import com.brandtnewtonsoftware.asle.models.sign.SignAssignment;
import com.brandtnewtonsoftware.asle.stage.PromptEntranceStage;
import com.brandtnewtonsoftware.asle.stage.StageManager;
import com.leapmotion.leap.Hand;

import java.util.LinkedList;
import java.util.List;

/**
 * Asks user to perform sign
 */
public abstract class GameStage extends StageManager implements PrimaryHandListener, SignRegisteredListener {

    private List<PrimaryHandListener> primaryHandPositionListeners = new LinkedList<>();
    protected final SuccessActor successActor;
    protected final GridOverlayActor gridOverlayActor;
    protected final SignActor signActor;

    public GameStage(ASLTutorGame game) {
        super(game);

        LeapListener listener = game.getListener();
        listener.addPrimaryHandListener(this);

        signActor = new SignActor();
        signActor.setListener(this);
        signActor.toFront();
        primaryHandPositionListeners.add(signActor);

        gridOverlayActor = new GridOverlayActor(5, 3);

        successActor = new SuccessActor();
        successActor.setVisible(false);
    }

    protected void setGridLines(int horizontalCount, int verticleCount) {
        gridOverlayActor.setHorizontalLineCount(horizontalCount);
        gridOverlayActor.setVerticalLineCount(verticleCount);
    }

    @Override
    public void dispose() {
        LeapListener listener = getGame().getListener();
        listener.removePrimaryHandListener(this);
    }

    @Override
    public void onPrimaryHandUpdated(Hand hand) {
        for (PrimaryHandListener listener : primaryHandPositionListeners) {
            listener.onPrimaryHandUpdated(hand);
        }
    }

    public String getName() {
        return getClass().getSimpleName();
    }

    protected abstract void signComplete(boolean signCorrect);
    protected abstract SignAssignment getNextSign();

    @Override
    public void onSignRegistered(Sign sign, boolean signCorrect) {
        if (signCorrect) {
            signComplete(true);
            successActor.flash();
        }
    }
}
