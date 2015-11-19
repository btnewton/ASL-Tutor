package com.brandtnewtonsoftware.asle.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.brandtnewtonsoftware.asle.models.Experience;

public class ExperienceActor extends Actor implements Experience.ExperienceListener {

    private float experienceToAdd;
    private float currentExperience;
    private Experience experience;
    private ShapeRenderer shapeRenderer;

    public ExperienceActor(Experience experience) {
        this.experience = experience;
        this.experience.addExperienceListener(this);
        shapeRenderer = new ShapeRenderer();
        setSize(Gdx.graphics.getWidth() * .8f, 20);
        setPosition((Gdx.graphics.getWidth() - getWidth()) / 2, 20);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0,0,0,.5f));
        float percentWidth = experience.getExperience() / experience.requiredExperienceForLevel(experience.getLevel());
        shapeRenderer.rect(getX(), getY(), percentWidth, getHeight());
        shapeRenderer.end();
    }

    public void experienceTic(float deltaTime) {

        currentExperience +=
    }

    @Override
    public void experienceAdded(int experience) {
        experienceToAdd += experience;
    }

    @Override
    public void levelChanged(int newLevel) {
    }
}
