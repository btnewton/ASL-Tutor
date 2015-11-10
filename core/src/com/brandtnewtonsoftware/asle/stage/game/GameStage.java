package com.brandtnewtonsoftware.asle.stage.game;

import com.badlogic.gdx.Gdx;
import com.brandtnewtonsoftware.asle.ASLTutorGame;
import com.brandtnewtonsoftware.asle.actor.BubbleTimerActor;
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
public abstract class GameStage extends StageManager implements PrimaryHandListener, HandCountListener, SignRegisteredListener {

    private List<PrimaryHandListener> primaryHandPositionListeners = new LinkedList<>();
    protected SuccessActor successActor;
    protected SignActor signActor;
    protected BubbleTimerActor timerActor;

    public GameStage(ASLTutorGame game) {
        super(game);

        LeapListener listener = game.getListener();
        listener.addHandCountListener(this);
        listener.addPrimaryHandListener(this);

        timerActor = new BubbleTimerActor();
        timerActor.setZIndex(0);
        stage.addActor(timerActor);
        timerActor.reset(2000);
        timerActor.start();

        signActor = new SignActor();
        signActor.setListener(this);
        signActor.setZIndex(5);
        primaryHandPositionListeners.add(signActor);
        stage.addActor(signActor);

        successActor = new SuccessActor();
        successActor.setVisible(false);
        successActor.setZIndex(5);
        stage.addActor(successActor);
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
            Gdx.app.postRunnable(() -> {
                ASLTutorGame game = getGame();
                game.setStageManager(new PromptEntranceStage(game));
            });
        }
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
