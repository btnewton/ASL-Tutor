package com.brandtnewtonsoftware.asle;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.brandtnewtonsoftware.asle.actor.HandNotPresentActor;
import com.brandtnewtonsoftware.asle.actor.ProximityLeapActor;
import com.brandtnewtonsoftware.asle.leap.LeapListener;
import com.leapmotion.leap.Controller;

//Gdx.gl.glClearColor(.93f, .41f, .31f, 1);
public class ASLEGame implements ApplicationListener {


	private Controller controller;
	private LeapListener listener;

	private Stage stage;

	@Override
	public void create() {
		listener = new LeapListener();
		controller = new Controller();
		controller.addListener(listener);


		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		final ProximityLeapActor proximityLeapActor = new ProximityLeapActor(listener);
		stage.addActor(proximityLeapActor);

		final HandNotPresentActor handNotPresentActor = new HandNotPresentActor(listener);
		stage.addActor(handNotPresentActor);
	}

	@Override
	public void dispose() {
		controller.removeListener(listener);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(.93f, .41f, .31f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
