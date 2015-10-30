package com.brandtnewtonsoftware.asle.leap;

import com.brandtnewtonsoftware.asle.simulation.state.GameState;
import com.brandtnewtonsoftware.asle.simulation.state.InStageState;
import com.brandtnewtonsoftware.asle.simulation.state.NoHandsState;
import com.brandtnewtonsoftware.asle.simulation.state.OuterHandState;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.Listener;

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
        if (!currentGameState.equals(gameState)) {
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
    }
}
