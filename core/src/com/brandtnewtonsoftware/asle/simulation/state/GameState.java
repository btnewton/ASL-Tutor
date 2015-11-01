package com.brandtnewtonsoftware.asle.simulation.state;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.brandtnewtonsoftware.asle.ASLEGame;
import com.brandtnewtonsoftware.asle.StageManager;
import com.brandtnewtonsoftware.asle.leap.HandCountListener;
import com.brandtnewtonsoftware.asle.stage.PromptEntranceStage;
import com.leapmotion.leap.Frame;

/**
 * Created by Brandt on 10/29/2015.
 */
public abstract class GameState {

    protected Stage stage;
    private ASLEGame game;

    public GameState(ASLEGame game) {
        this.game = game;
        stage = new Stage();
    }

    public void render() {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    // Remove listeners and deallocate resources
    public abstract void dispose();

    protected ASLEGame getGame() {
        return game;
    }
}
