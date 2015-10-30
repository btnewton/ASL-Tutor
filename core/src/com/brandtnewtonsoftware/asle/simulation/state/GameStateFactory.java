package com.brandtnewtonsoftware.asle.simulation.state;

import com.leapmotion.leap.Frame;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brandt on 10/30/2015.
 */
public class GameStateFactory {

    private static List<GameState> gameStates = new ArrayList<>();

    public static void initFactory() {
        gameStates.add(new NoHandsState());
        gameStates.add(new OuterHandState());
    }

    public static GameState make(Frame frame) {
        GameState currentGameState = null;
        for (GameState gameState : gameStates) {
            if (gameState.checkState(frame)) {
                currentGameState = gameState;
                break;
            }
        }
        return currentGameState;
    }

}
