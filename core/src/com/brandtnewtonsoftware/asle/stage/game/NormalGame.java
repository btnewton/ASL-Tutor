package com.brandtnewtonsoftware.asle.stage.game;

import com.badlogic.gdx.Gdx;
import com.brandtnewtonsoftware.asle.ASLTutorGame;
import com.brandtnewtonsoftware.asle.models.Attempt;
import com.brandtnewtonsoftware.asle.models.SignPerformance;
import com.brandtnewtonsoftware.asle.models.User;
import com.brandtnewtonsoftware.asle.models.sign.Sign;
import com.brandtnewtonsoftware.asle.models.sign.SignAssignment;
import com.brandtnewtonsoftware.asle.models.sign.server.ISignServer;
import com.brandtnewtonsoftware.asle.models.sign.server.RandomSignServer;
import com.brandtnewtonsoftware.asle.util.Stopwatch;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Brandt on 11/8/2015.
 */
public final class NormalGame extends GameStage implements ActionListener {

    private ISignServer signServer;
    private Timer timer;
    private Stopwatch stopwatch;
    private Attempt attempt;
    private int streak;
    private String[] signValues;

    public NormalGame(ASLTutorGame game) {
        super(game);
        signServer = new RandomSignServer();
        stopwatch = new Stopwatch();
        timer = new Timer(2000, this);

        List<SignPerformance> signPerformances = SignPerformance.getSignPerformance(this);
        signValues = new String[signPerformances.size()];
        for (int i = 0; i < signValues.length; i++) {
            signValues[i] = signPerformances.get(i).getSignValue();
        }

        signActor.changeSign(getNextSign());
    }



    @Override
    protected synchronized void signComplete(boolean signCorrect) {
        signActor.setSignComplete();

        stopwatch.stop();
        timer.stop();

        if (signCorrect) {
            streak++;
            attempt.setTimeToComplete((int) stopwatch.getTimeElapsed());
        } else {
            streak = 0;
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
        stopwatch.reset();
        stopwatch.start();

        if (timer.isRunning()) {
            timer.restart();
        } else {
            timer.start();
        }

        Sign sign = signServer.getSignValue();
        attempt = new Attempt(sign);

        return new SignAssignment(sign, 500);
    }

    @Override
    public void onSignRegistered(Sign sign, boolean signCorrect) {
        super.onSignRegistered(sign, signCorrect);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        signComplete(false);
    }
}
