package com.brandtnewtonsoftware.asle.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.brandtnewtonsoftware.asle.models.Experience;

public class ExperienceActor extends Actor {

    private Experience experience;
    private ShapeRenderer shapeRenderer;

    public ExperienceActor(Experience experience) {
        this.experience = experience;
        shapeRenderer = new ShapeRenderer();
        setSize(Gdx.graphics.getWidth() * .8f, 20);
        setPosition((Gdx.graphics.getWidth() - getWidth()) / 2, 20);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0,0,0,.5f));
        float totalToNextXp = experience.requiredExperienceForLevel(experience.getLevel() + 1) - experience.requiredExperienceForLevel(experience.getLevel());
        float currentXp = experience.getExperience() - experience.requiredExperienceForLevel(experience.getLevel());
        if (currentXp < 0) {
            currentXp = 0;
        }
        float percentWidth = currentXp / totalToNextXp;
        shapeRenderer.rect(getX(), getY(), percentWidth * getWidth(), getHeight());
        shapeRenderer.end();
    }
}
