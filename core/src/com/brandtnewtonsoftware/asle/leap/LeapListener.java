package com.brandtnewtonsoftware.asle.leap;

import com.brandtnewtonsoftware.asle.simulation.state.GameState;
import com.brandtnewtonsoftware.asle.simulation.state.InStageState;
import com.brandtnewtonsoftware.asle.simulation.state.NoHandsState;
import com.brandtnewtonsoftware.asle.simulation.state.OuterHandState;
import com.leapmotion.leap.*;

import java.util.ArrayList;
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
    private List<GameState> gameStates = new ArrayList<>();
    private List<PrimaryHandPositionListener> primaryHandPositionListeners = new ArrayList<>();
    private List<HandCountListener> handCountListeners = new ArrayList<>();
    private int handCount;

    public LeapListener() {
        logger.setLevel(Level.ALL);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new SimpleFormatter());
        handler.setLevel(Level.ALL);
        logger.addHandler(handler);

        gameStates.add(new NoHandsState());
        gameStates.add(new OuterHandState());
        gameStates.add(new InStageState());
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
        for (GameState gameState : gameStates) {
            if (gameState.checkState(frame)) {
                setGameState(gameState);
                break;
            }
        }

        Hand defaultHand = LeapHelper.getDefaultHand(frame.hands());
        if (defaultHand != null) {
            com.leapmotion.leap.Vector handPosition = defaultHand.palmPosition();
            for (PrimaryHandPositionListener listener : primaryHandPositionListeners) {
                listener.onPrimaryHandUpdated(handPosition);
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

    public void addPrimaryHandPositionListener(PrimaryHandPositionListener listener) {
        primaryHandPositionListeners.add(listener);
    }
    public void removePrimaryHandPositionListener(PrimaryHandPositionListener listener) {
        primaryHandPositionListeners.remove(listener);
    }

    public void addHandCountListener(HandCountListener listener) {
        handCountListeners.add(listener);
    }
    public void removeHandCountListener(HandCountListener listener) {
        handCountListeners.remove(listener);
    }
}
