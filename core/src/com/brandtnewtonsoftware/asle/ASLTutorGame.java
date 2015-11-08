package com.brandtnewtonsoftware.asle;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.brandtnewtonsoftware.asle.models.HandCalibrator;
import com.brandtnewtonsoftware.asle.leap.LeapListener;
import com.brandtnewtonsoftware.asle.models.User;
import com.brandtnewtonsoftware.asle.simulation.state.GameState;
import com.brandtnewtonsoftware.asle.simulation.state.PromptEntranceStage;
import com.brandtnewtonsoftware.asle.util.Database;
import com.leapmotion.leap.Controller;

import javax.swing.*;
import java.sql.SQLException;

public class ASLTutorGame extends ApplicationAdapter implements Input.TextInputListener {

	private static User user;
	private Controller controller;
	private final LeapListener listener = new LeapListener();

	private GameState gameState;
	private static HandCalibrator handCalibrator = new HandCalibrator();

	@Override
	public void create() {
		new Database();
		controller = new Controller();
		controller.addListener(listener);
		listener.addPrimaryHandListener(handCalibrator::calibrate);

//		Gdx.input.getTextInput(this, "Welcome to ASL Tutor!", null, "Name");
		input("Brandt");
	}

	@Override
	public void dispose() {
		controller.removeListener(listener);
		controller.delete();
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
		if (this.gameState != null)
			this.gameState.dispose();
		this.gameState = gameState;
	}

	public static HandCalibrator getHandCalibrator() {
		return handCalibrator;
	}

	@Override
	public void input(String text) {
		text = Database.sanitizeString(text.trim());

		if (!text.isEmpty()) {
			User user = null;
			try {
				user = User.getUser(text);
				if (user == null) {
					user = new User(text);
				}
				user.incrementLoginCount();
				user.save();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			if (user != null) {
				ASLTutorGame.user = user;
				Gdx.app.postRunnable(() -> setGameState(new PromptEntranceStage(this)));
				return;
			}
		}

		JOptionPane.showMessageDialog(null, "There was an error with your input.", "Error logging in!", JOptionPane.ERROR_MESSAGE);
		Gdx.input.getTextInput(this, "Welcome to ASL Tutor!", null, "Name");
	}

	public static User getUser() {
		return user;
	}

	@Override
	public void canceled() {
		System.exit(-1);
	}
}
