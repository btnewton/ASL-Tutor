package com.brandtnewtonsoftware.asle.stage.game;

import com.brandtnewtonsoftware.asle.ASLTutorGame;
import com.brandtnewtonsoftware.asle.actor.GridOverlayActor;
import com.brandtnewtonsoftware.asle.actor.HandPositionActor;
import com.brandtnewtonsoftware.asle.actor.sign.SignActor;
import com.brandtnewtonsoftware.asle.actor.sign.SignRegisteredListener;
import com.brandtnewtonsoftware.asle.leap.LeapListener;
import com.brandtnewtonsoftware.asle.leap.PrimaryHandListener;
import com.brandtnewtonsoftware.asle.models.sign.Sign;
import com.brandtnewtonsoftware.asle.models.sign.SignAssignment;
import com.brandtnewtonsoftware.asle.stage.StageManager;
import com.leapmotion.leap.Hand;

import java.util.LinkedList;
import java.util.List;

/**
 * Asks user to perform sign
 */
public abstract class GameStage extends StageManager implements PrimaryHandListener, SignRegisteredListener {

    private List<PrimaryHandListener> primaryHandPositionListeners = new LinkedList<>();
    protected final GridOverlayActor gridOverlayActor;
    protected final SignActor signActor;
    protected final HandPositionActor handPositionActor;

    public GameStage(ASLTutorGame game) {
        super(game);

        game.getListener().addPrimaryHandListener(this);

        handPositionActor = new HandPositionActor();
        stage.addActor(handPositionActor);

        signActor = new SignActor();
        signActor.setListener(this);
        primaryHandPositionListeners.add(signActor);

        gridOverlayActor = new GridOverlayActor(5, 3);
    }

    protected void setGridLines(int horizontalCount, int verticalCount) {
        gridOverlayActor.setHorizontalLineCount(horizontalCount);
        gridOverlayActor.setVerticalLineCount(verticalCount);
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
        }
    }
}
