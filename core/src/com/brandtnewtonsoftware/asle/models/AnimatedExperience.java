package com.brandtnewtonsoftware.asle.models;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class AnimatedExperience extends Experience implements ActionListener {

    private int experienceToAdd;
    private int animatedExperience;
    private int totalExperienceNeeded;
    private Timer timer;

    public AnimatedExperience(){
        this(0);
    }
    public AnimatedExperience(int experience) {
        super(experience);
        timer = new Timer(200, this);
        animatedExperience = getExperience();
        totalExperienceNeeded = requiredExperienceForLevel(getLevel());
    }

    public int getProgress() {
        return (int) (animatedExperience / (double) totalExperienceNeeded * 100);
    }

    @Override
    public void addExperience(int experience) {
        super.addExperience(experience);
        experienceToAdd += experience;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int experienceIncrement = (int) (experienceToAdd * .2);
        experienceToAdd -= experienceIncrement;
        animatedExperience += experienceIncrement;

    }
}
