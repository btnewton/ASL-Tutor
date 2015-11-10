package com.brandtnewtonsoftware.asle.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.brandtnewtonsoftware.asle.leap.HandCountListener;

/**
 * Created by Brandt on 10/31/2015.
 */
public class HandNotPresentActor extends Actor implements HandCountListener {

    Texture texture = new Texture(Gdx.files.internal("img/ic_hand.png"));

    public HandNotPresentActor() {
        setPosition(getOriginalX(), getOriginalY());
        setBounds(getX(), getY(), texture.getWidth(), texture.getHeight());
        setUpAction();
    }

    @Override
    public void draw(Batch batch, float alpha){
        Color c = this.getColor();
        batch.setColor(c);
        // always make sure to only multiply by the parent alpha
        batch.getColor().a *= alpha;
        batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(), getWidth(),
                getHeight(), getScaleX(), getScaleY(), getRotation(), 0, 0,
                texture.getWidth(), texture.getHeight(), false, false);
        batch.setColor(Color.BLACK);
    }


    public float getOriginalX() {
        return Gdx.graphics.getWidth() / 2 - texture.getWidth() / 2;
    }
    public float getOriginalY() {
        return Gdx.graphics.getHeight() / 2 - texture.getHeight() / 2;
    }


    @Override
    public void onHandCountChange(int handCount) {
        if (handCount > 0) {
            setVisible(false);
            clearActions();
            setPosition(getOriginalX(), getOriginalY());
            setColor(Color.BLACK);
        } else {
            // Might be a second hand, do not interupt current action
            if (!hasActions()) {
                setUpAction();
            }
            setVisible(true);
        }
    }

    private void setUpAction() {
        SequenceAction sequenceAction = new SequenceAction();

        MoveToAction setToSide = new MoveToAction();
        setToSide.setPosition(getOriginalX() + texture.getWidth(), getOriginalY());

        MoveToAction moveOverLeap = new MoveToAction();
        moveOverLeap.setPosition(getOriginalX(), getOriginalY());
        moveOverLeap.setDuration(1f);

        AlphaAction alphaAction = new AlphaAction();
        alphaAction.setAlpha(0f);
        alphaAction.setDuration(.5f);

        sequenceAction.addAction(setToSide);
        sequenceAction.addAction(moveOverLeap);
        sequenceAction.addAction(alphaAction);
        sequenceAction.addAction(new Action() {
            @Override
            public boolean act(float delta) {
                setColor(Color.BLACK);
                setUpAction();
                return true;
            }
        });

        addAction(sequenceAction);
    }
}
