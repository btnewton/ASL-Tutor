package com.brandtnewtonsoftware.asle.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.brandtnewtonsoftware.asle.ASLTutorGame;

/**
 * Created by Brandt on 10/29/2015.
 */
public abstract class StageManager {

    protected Stage stage;
    private ASLTutorGame game;

    public StageManager(ASLTutorGame game) {
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
