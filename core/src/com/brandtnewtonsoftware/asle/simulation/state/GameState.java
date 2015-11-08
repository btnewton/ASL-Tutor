package com.brandtnewtonsoftware.asle.simulation.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.brandtnewtonsoftware.asle.ASLTutorGame;

/**
 * Created by Brandt on 10/29/2015.
 */
public abstract class GameState {

    protected Stage stage;
    private ASLTutorGame game;

    public GameState(ASLTutorGame game) {
        this.game = game;
        stage = new Stage();
    }

    public void render() {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    // Remove listeners and deallocate resources
    public abstract void dispose();

    protected ASLTutorGame getGame() {
        return game;
    }
}
