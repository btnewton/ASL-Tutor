package com.brandtnewtonsoftware.asle.leap;

import com.brandtnewtonsoftware.asle.simulation.state.GameState;
import com.leapmotion.leap.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Created by Brandt on 10/29/2015.
 */
public class LeapListener extends Listener {

    public static final Logger logger = Logger.getLogger(LeapListener.class.getName());

    private GameState currentGameState;
    private List<HandCountListener> handCountListeners = new LinkedList<>();
    private List<PrimaryHandListener> primaryHandListeners = new LinkedList<>();
    private int handCount;
    private double maxIndexExtension = 0;

    public LeapListener() {
        logger.setLevel(Level.ALL);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new SimpleFormatter());
        handler.setLevel(Level.ALL);
        logger.addHandler(handler);
    }

    @Override
    public void onConnect(Controller controller) {
        super.onConnect(controller);
        controller.enableGesture(Gesture.Type.TYPE_SWIPE);
        logger.info("Controller Connected!");
    }

    public GameState getCurrentGameState() {
        return currentGameState;
    }

    private void setGameState(GameState gameState) {
        if (currentGameState == null || !currentGameState.equals(gameState)) {
            currentGameState = gameState;
            logger.info("Log state switched to " + gameState.getClass().getSimpleName());
        }
    }



    @Override
    public void onFrame(Controller controller) {
        super.onFrame(controller);
        Frame frame = controller.frame();

        Hand defaultHand = LeapHelper.getDefaultHand(frame.hands());
        if (defaultHand != null) {
            for (PrimaryHandListener listener : primaryHandListeners) {
                listener.onPrimaryHandUpdated(defaultHand);
            }
        }

        int handCount = frame.hands().count();
        if (this.handCount != handCount) {
            this.handCount = handCount;
            for (HandCountListener listener : handCountListeners) {
                listener.onHandCountChange(this.handCount);
            }
        }
    }


    public void addPrimaryHandListener(PrimaryHandListener listener) {
        primaryHandListeners.add(listener);
    }
    public void removePrimaryHandListener(PrimaryHandListener listener) {
        primaryHandListeners.remove(listener);
    }

    public void addHandCountListener(HandCountListener listener) {
        handCountListeners.add(listener);
    }
    public void removeHandCountListener(HandCountListener listener) {
        handCountListeners.remove(listener);
    }
}
