package com.brandtnewtonsoftware.asle.stage.game;

import com.badlogic.gdx.Gdx;
import com.brandtnewtonsoftware.asle.ASLTutorGame;
import com.brandtnewtonsoftware.asle.actor.ArcTimerActor;
import com.brandtnewtonsoftware.asle.actor.ExperienceActor;
import com.brandtnewtonsoftware.asle.leap.HandCountListener;
import com.brandtnewtonsoftware.asle.models.Attempt;
import com.brandtnewtonsoftware.asle.models.Experience;
import com.brandtnewtonsoftware.asle.models.SignPerformance;
import com.brandtnewtonsoftware.asle.models.User;
import com.brandtnewtonsoftware.asle.models.sign.Sign;
import com.brandtnewtonsoftware.asle.models.sign.SignAssignment;
import com.brandtnewtonsoftware.asle.models.sign.SignFactory;
import com.brandtnewtonsoftware.asle.util.Stopwatch;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

/**
 * Created by Brandt on 11/8/2015.
 */
public final class NormalGame extends GameStage implements ActionListener, HandCountListener {

    public static final int INITIAL_DELAY = 5000;
    private Timer timer;
    private Stopwatch stopwatch;
    private Attempt attempt;
    private int streak;
    private Sign[] signValues;
    private ArcTimerActor timerActor;
    private List<SignPerformance> signPerformances;

    public NormalGame(ASLTutorGame game) {
        super(game);
        stopwatch = new Stopwatch();
        timer = new Timer(INITIAL_DELAY, this);

        timerActor = new ArcTimerActor(stopwatch, timer.getDelay());

        game.getListener().addHandCountListener(this);

        signPerformances = SignPerformance.getSignPerformance(this);
        signValues = new Sign[signPerformances.size()];
        for (int i = 0; i < signValues.length; i++) {
            signValues[i] = SignFactory.make(signPerformances.get(i).getSignValue());
        }

        signActor.changeSign(getNextSign());

        ExperienceActor xpActor = new ExperienceActor(ASLTutorGame.getUser().getExperience());

        // Order actors are added determines render order
        stage.addActor(timerActor);
        stage.addActor(signActor);
        stage.addActor(xpActor);

        stage.addActor(gridOverlayActor);
    }

    private void addExperience(int xp) {
        User user = ASLTutorGame.getUser();
        user.getExperience().addExperience(xp);
    }

    private double getMultiplier() {
        return 1 + (streak * 0.1);
    }

    @Override
    protected synchronized void signComplete(boolean signCorrect) {
        signActor.setSignComplete();

        stopwatch.stop();
        timer.stop();

        if (signCorrect) {
            streak++;
            addExperience((int) (100 * getMultiplier()));
            attempt.setTimeToComplete((int) stopwatch.getTimeElapsed());
        } else {
            streak = 0;
            addExperience(-50);
        }

        User user = ASLTutorGame.getUser();
        try {
            attempt.save(user, this);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Gdx.app.postRunnable(() -> signActor.changeSign(getNextSign()));
    }

    @Override
    protected SignAssignment getNextSign() {
        Experience experience = ASLTutorGame.getUser().getExperience();
        int level = experience.getLevel();

        int delay = INITIAL_DELAY - level * 100;
        timer.setInitialDelay(delay);
        timerActor.setTimeLimit(delay);

        int maxSignIndex = level + 2;
        if (maxSignIndex > signValues.length) {
            maxSignIndex = signValues.length;
        }
        Sign sign = getNewSign(maxSignIndex);
        attempt = new Attempt(sign);

        stopwatch.reset();
        stopwatch.start();

        if (timer.isRunning()) {
            timer.restart();
        } else {
            timer.start();
        }

        return new SignAssignment(getSignPerformance(sign));
    }

    private SignPerformance getSignPerformance(Sign sign) {
        SignPerformance signPerformance = null;
        for (SignPerformance performance : signPerformances) {
            if (performance.getSignValue() == sign.getValue()) {
                signPerformance = performance;
                break;
            }
        }
        return signPerformance;
    }

    private Sign getNewSign(int maxSignIndex) {
        Random random = new Random();
        int selectedIndex = random.nextInt(maxSignIndex);
        Sign sign = signValues[selectedIndex];

        if (attempt != null && sign.equals(attempt.getSign())) {
            if (selectedIndex > 0) {
                selectedIndex--;
            } else {
                selectedIndex++;
            }
            sign = signValues[selectedIndex];
        }
        return sign;
    }

    @Override
    public void onSignRegistered(Sign sign, boolean signCorrect) {
        super.onSignRegistered(sign, signCorrect);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        signComplete(false);
    }

    @Override
    public void onHandCountChange(int handCount) {
        if (handCount == 0) {
            streak = 0;
        }
        signActor.setShowSign(handCount == 0);
    }
}
