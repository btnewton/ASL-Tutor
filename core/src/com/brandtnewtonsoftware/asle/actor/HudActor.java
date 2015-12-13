package com.brandtnewtonsoftware.asle.actor;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.brandtnewtonsoftware.asle.models.Experience;

/**
 * Created by Brandt on 12/13/2015.
 */
public class HudActor extends Actor {

    private final Experience experience;
    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    private final float XP_BAR_X = Gdx.graphics.getWidth() / 10;
    private final float XP_BAR_WIDTH = XP_BAR_X * 8;

    public HudActor(Experience experience) {
        this.experience = experience;

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        // Circle
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1, 1, 1, .3f);
        shapeRenderer.circle(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, (ArcTimerActor.DIAMETER + 25)/2);

        // XP Bar
        shapeRenderer.setColor(new Color(0,0,0,.5f));
        float totalToNextXp = experience.requiredExperienceForLevel(experience.getLevel() + 1) - experience.requiredExperienceForLevel(experience.getLevel());
        float currentXp = experience.getExperience() - experience.requiredExperienceForLevel(experience.getLevel());
        if (currentXp < 0) {
            currentXp = 0;
        }
        float percentWidth = currentXp / totalToNextXp;
        shapeRenderer.rect(XP_BAR_X, 40, percentWidth * XP_BAR_WIDTH, 20);


        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }
}
