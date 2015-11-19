package com.brandtnewtonsoftware.asle.stage.game;

import com.badlogic.gdx.Gdx;
import com.brandtnewtonsoftware.asle.ASLTutorGame;
import com.brandtnewtonsoftware.asle.actor.ArcTimerActor;
import com.brandtnewtonsoftware.asle.actor.ExperienceActor;
import com.brandtnewtonsoftware.asle.leap.HandCountListener;
import com.brandtnewtonsoftware.asle.models.Attempt;
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

    private Timer timer;
    private Stopwatch stopwatch;
    private Attempt attempt;
    private int streak;
    private int score;
    private Sign[] signValues;
    private ArcTimerActor timerActor;
    private ExperienceActor xpActor;

    public NormalGame(ASLTutorGame game) {
        super(game);
        stopwatch = new Stopwatch();
        timer = new Timer(3000, this);

        timerActor = new ArcTimerActor(stopwatch, timer.getDelay());

        game.getListener().addHandCountListener(this);

        List<SignPerformance> signPerformances = SignPerformance.getSignPerformance(this);
        signValues = new Sign[signPerformances.size()];
        for (int i = 0; i < signValues.length; i++) {
            signValues[i] = SignFactory.make(signPerformances.get(i).getSignValue());
        }

        signActor.changeSign(getNextSign());

        xpActor = new ExperienceActor(ASLTutorGame.getUser().getExperience());

        // Order actors are added determines render order
        stage.addActor(timerActor);
        stage.addActor(signActor);
        stage.addActor(hudActor);
        stage.addActor(xpActor);

        stage.addActor(gridOverlayActor);
    }

    private void addScore(int points) {
        User user = ASLTutorGame.getUser();
        // TODO remove ABS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        user.getExperience().addExperience(Math.abs(points));

        score += points;
        if (score < 0) {
            score = 0;
        }
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
            addScore((int) (100 * getMultiplier()));
            attempt.setTimeToComplete((int) stopwatch.getTimeElapsed());
        } else {
            streak = 0;
            addScore(-5);
        }

        hudActor.setScoreText(Integer.toString(score));
        hudActor.setStreakText(Integer.toString(streak));

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
        int level = getLevel();

        int delay = 3000 - level * 100;
        timer.setDelay(delay);
        timerActor.setTimeLimit(delay);

        Random random = new Random();
        Sign sign = signValues[random.nextInt(level)];
        attempt = new Attempt(sign);

        stopwatch.reset();
        stopwatch.start();

        if (timer.isRunning()) {
            timer.restart();
        } else {
            timer.start();
        }

        return new SignAssignment(sign, 500);
    }

    private void changeTimeLimit(int timeLimit) {
        timer.setDelay(timeLimit);

    }

    private int getLevel() {
        int level = score / 5000 + 3;
        if (level > signValues.length) {
            level = signValues.length;
        }
        System.out.println("LEVEL: " + level);

        return level;
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
