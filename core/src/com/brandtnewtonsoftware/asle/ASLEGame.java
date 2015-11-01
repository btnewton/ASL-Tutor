package com.brandtnewtonsoftware.asle;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.brandtnewtonsoftware.asle.leap.LeapListener;
import com.brandtnewtonsoftware.asle.simulation.state.GameState;
import com.brandtnewtonsoftware.asle.stage.PromptEntranceStage;
import com.leapmotion.leap.Controller;

public class ASLEGame extends ApplicationAdapter {

	private Controller controller;
	private final LeapListener listener = new LeapListener();

	private GameState gameState;

	@Override
	public void create() {
		controller = new Controller();
		controller.addListener(listener);
		gameState = new PromptEntranceStage(this);
	}

	@Override
	public void dispose() {
		controller.removeListener(listener);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(.93f, .41f, .31f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (gameState != null){
			gameState.render();
		}
	}

	public LeapListener getListener() {
		return listener;
	}

	public void setGameState(GameState gameState) {
		this.gameState.dispose();
		this.gameState = gameState;
	}
}
