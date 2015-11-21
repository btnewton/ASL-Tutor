package com.brandtnewtonsoftware.asle.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brandt on 11/16/15.
 */
public class Experience {

    public static final int MAX_LEVEL = 20;
    private int currentLevel;
    private int experience;
    private Integer[] levelRequirements = new Integer[MAX_LEVEL];

    public Experience() {
        this(0);
    }

    public Experience(int experience) {
        addExperience(experience);
    }

    public void addExperience(int experience) {
        this.experience += experience;
        updateLevel();
    }

    public int getExperience() {
        return experience;
    }

    /**
     * Performs a recursive call in case xp was enough to level more than once.
     */
    private void updateLevel() {
        if (experience >= requiredExperienceForLevel(currentLevel + 1)) {
            currentLevel++;
            System.out.println("Level Updated to " + getLevel());
            updateLevel();
        } else if (currentLevel > 0 && experience < requiredExperienceForLevel(currentLevel)) {
            currentLevel--;
            System.out.println("Level Updated to " + getLevel());
            updateLevel();
        }
    }

    public int requiredExperienceForLevel(int level) {
        if (levelRequirements[level] == null) {
            levelRequirements[level] = (int) ((Math.pow(level + 1, 1.5) * 500) + 500);
        }
        return levelRequirements[level];
    }

    public int getLevel() {
        return currentLevel;
    }
}
