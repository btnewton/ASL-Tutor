package com.brandtnewtonsoftware.asle.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.brandtnewtonsoftware.asle.ASLTutorGame;
import com.brandtnewtonsoftware.asle.leap.LeapHelper;
import com.brandtnewtonsoftware.asle.leap.PrimaryHandListener;
import com.leapmotion.leap.Hand;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Brandt on 10/31/2015.
 */
public class ProximityLeapActor extends Actor implements PrimaryHandListener, ActionListener {

    private Timer timer;
    private boolean learnedEnterStage;
    private List<StagePresenceListener> stagePresenceListeners = new ArrayList<>();


    Texture texture = new Texture(Gdx.files.internal("img/ic_leap.png"));

    public ProximityLeapActor() {
        setPosition(Gdx.graphics.getWidth() / 2 - texture.getWidth() / 2, Gdx.graphics.getHeight() / 3 - texture.getHeight() / 2);
        setBounds(getX(), getY(), texture.getWidth(), texture.getHeight());
        setScale(.78f);

        final int HAND_OVER_DURATION = 2 * 1000;
        timer = new Timer(HAND_OVER_DURATION, this);
    }

    @Override
    public void draw(Batch batch, float alpha) {
        batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(), getWidth(),
                getHeight(), getScaleX(), getScaleY(), getRotation(), 0, 0,
                texture.getWidth(), texture.getHeight(), false, false);
    }

    public void dispose() {

    }

    public void removeStagePresenceListener(StagePresenceListener listener) {
        stagePresenceListeners.remove(listener);
    }

    public void addStagePresenceListener(StagePresenceListener listener) {
        stagePresenceListeners.add(listener);
    }

    private void setStagePresence(boolean onStage) {
        if (onStage != learnedEnterStage) {
            learnedEnterStage = onStage;
            for (StagePresenceListener listener : stagePresenceListeners) {
                listener.onStagePresenceChange(learnedEnterStage);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.stop();
        setStagePresence(true);
    }

    @Override
    public void onPrimaryHandUpdated(Hand hand) {
        float scale;
        double distanceFromCenterStage = LeapHelper.getDistanceToCenterStage(hand.palmPosition());

        if (LeapHelper.inCenterStage(distanceFromCenterStage)) {
            scale = 1;
            if (!learnedEnterStage && !timer.isRunning()) {
                timer.restart();
                ASLTutorGame.getHandCalibrator().reset();
            }
        } else {
            scale = (float) ((LeapHelper.OUTER_RADIUS - distanceFromCenterStage) / ((LeapHelper.OUTER_RADIUS - LeapHelper.STAGE_RADIUS) * 2)) + .5f;
            if (timer.isRunning()) {
                timer.stop();
            }
            setStagePresence(false);
        }

        setScale(scale);
    }
}
