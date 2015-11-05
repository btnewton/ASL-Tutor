package com.brandtnewtonsoftware.asle;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.brandtnewtonsoftware.asle.knn.HandCalibrator;
import com.brandtnewtonsoftware.asle.leap.LeapListener;
import com.brandtnewtonsoftware.asle.simulation.state.GameState;
import com.brandtnewtonsoftware.asle.stage.PromptEntranceStage;
import com.brandtnewtonsoftware.asle.util.Database;
import com.leapmotion.leap.Controller;

import java.sql.Connection;

public class ASLEGame extends ApplicationAdapter implements Input.TextInputListener {

	public static User user;
	private Controller controller;
	private final LeapListener listener = new LeapListener();

	private GameState gameState;
	private static HandCalibrator handCalibrator = new HandCalibrator();

	@Override
	public void create() {
		Gdx.input.getTextInput(this, "Welcome to ASL Tutor!", null, "Name");
		controller = new Controller();
		controller.addListener(listener);
		listener.addPrimaryHandListener(handCalibrator::calibrate);
	}

	@Override
	public void dispose() {
		controller.removeListener(listener);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(.93f, .41f, .31f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (gameState != null) {
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

	public static HandCalibrator getHandCalibrator() {
		return handCalibrator;
	}

	@Override
	public void input(String text) {
		Database database = new Database();
		text = database.sanitizeString(text.trim());

		if (text.isEmpty())
			Gdx.input.getTextInput(this, "Welcome to ASL Tutor!", null, "Name");
		else {
			User user = database.getUser(text);
			if (user == null) {
				user = database.newUser(text);
			}
			database.loginUser(user);
			ASLEGame.user = user;
			gameState = new PromptEntranceStage(this);
		}
	}

	@Override
	public void canceled() {

	}
}
