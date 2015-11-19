package com.brandtnewtonsoftware.asle.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brandt on 11/16/15.
 */
public class Experience {

    private int currentLevel;
    private int experience;
    private List<ExperienceListener> experienceListeners;

    public Experience() {
        this(0);
    }

    public Experience(int experience) {
        experienceListeners = new ArrayList<>();
        addExperience(experience);
    }

    public void addExperience(int experience) {
        this.experience += experience;
        onExperienceAdded(experience);
    }

    public int getExperience() {
        return experience;
    }

    private void updateLevel() {
        if (experience >= requiredExperienceForLevel(currentLevel + 1)) {
            currentLevel++;
            onLevelChanged();
        } else if (experience < requiredExperienceForLevel(currentLevel)) {
            currentLevel--;
            onLevelChanged();
        }
    }

    private double getProgressToNextLevel() {
        return (experience / requiredExperienceForLevel(currentLevel + 1)) * 100;
    }

    public int requiredExperienceForLevel(int level) {
        return (int) ((Math.pow(level, 1.5) * 500) + 500);
    }
    public int getLevel() {
        return currentLevel;
    }

    public void addExperienceListener(ExperienceListener experienceListener) {
        experienceListeners.add(experienceListener);
    }
    public void removeExperienceListener(ExperienceListener experienceListener) {
        experienceListeners.remove(experienceListener);
    }
    public void onExperienceAdded(int experience) {
        for (ExperienceListener listener : experienceListeners){
            listener.experienceAdded(experience);
        }
    }
    public void onLevelChanged() {
        for (ExperienceListener listener : experienceListeners) {
            listener.levelChanged(currentLevel);
        }
    }

    public interface ExperienceListener {
        void experienceAdded(int experience);
        void levelChanged(int newLevel);
    }
}
