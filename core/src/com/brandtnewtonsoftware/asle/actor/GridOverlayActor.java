package com.brandtnewtonsoftware.asle.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import javafx.scene.Camera;

/**
 * Created by Brandt on 11/11/2015.
 */
public class GridOverlayActor extends Actor implements InputProcessor {

    private int verticalLineCount;
    private int horizontalLineCount;
    private ShapeRenderer shapeRenderer;
    private static boolean staticVisibleFlag = false;

    public GridOverlayActor(int verticalLineCount, int horizontalLineCount) {
        this.verticalLineCount = verticalLineCount;
        this.horizontalLineCount = horizontalLineCount;
        shapeRenderer = new ShapeRenderer();
        setVisible(staticVisibleFlag);
        setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.input.setInputProcessor(this);
    }

    public void setHorizontalLineCount(int horizontalLineCount) {
        this.horizontalLineCount = horizontalLineCount;
    }

    public void setVerticalLineCount(int verticalLineCount) {
        this.verticalLineCount = verticalLineCount;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        final int width = Gdx.graphics.getWidth();
        final int height = Gdx.graphics.getHeight();

        float vertOffset = ((float)width) / (verticalLineCount + 1);
        float horizOffset = ((float)height) / (horizontalLineCount + 1);

        Color color = getColor();
        color.a *= parentAlpha;
        batch.setColor(color);

        batch.end();

        Gdx.gl20.glEnable(GL20.GL_BLEND);
        Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(new Color(1,1,1,.5f));
        for (int i = 0; i < verticalLineCount; i++) {
            float offset = vertOffset * (i + 1);
            shapeRenderer.line(offset, 0, offset, height);
        }
        for (int i = 0; i < horizontalLineCount; i++) {
            float offset = horizOffset * (i + 1);
            shapeRenderer.line(0, offset, width, offset);
        }
        shapeRenderer.end();
        Gdx.gl20.glDisable(GL20.GL_BLEND);

        batch.begin();
    }

//region InputProcessors
    @Override
    public boolean keyDown(int keycode) {
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) && Gdx.input.isKeyPressed(Input.Keys.H)) {
            staticVisibleFlag = !staticVisibleFlag;
            setVisible(staticVisibleFlag);
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
//endregion
}
