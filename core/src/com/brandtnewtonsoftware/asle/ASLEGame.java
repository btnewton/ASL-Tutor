package com.brandtnewtonsoftware.asle;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.brandtnewtonsoftware.asle.leap.LeapListener;
import com.brandtnewtonsoftware.asle.screens.AsleScreen;
import com.brandtnewtonsoftware.asle.simulation.state.GameState;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Listener;

import javax.naming.ldap.Control;
import java.util.Random;
import java.util.logging.Logger;

public class ASLEGame extends ApplicationAdapter {


	SpriteBatch batch;
	private Controller controller;
	private LeapListener listener;

	@Override
	public void create () {
		batch = new SpriteBatch();
		listener = new LeapListener();

		controller = new Controller();
		controller.addListener(listener);
	}

	@Override
	public void dispose() {
		controller.removeListener(listener);
		batch.dispose();
	}

	@Override
	public void render() {

		Gdx.gl.glClearColor(.93f, .41f, .31f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		GameState gameState = listener.getCurrentGameState();
		if (gameState != null)
			gameState.render(batch);
		batch.end();
	}
}
