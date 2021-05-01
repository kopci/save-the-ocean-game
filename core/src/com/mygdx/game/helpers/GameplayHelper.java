package com.mygdx.game.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.util.Random;

public class GameplayHelper {

    public static int getClassicScore() {
        Preferences prefs = Gdx.app.getPreferences("SaveTheOceanPreferences");
        if (!prefs.contains("classicScore")) {
            prefs.putInteger("classicScore", 100); // default
            prefs.flush();
        }
        int trashRevealed = prefs.getInteger("classicScore");
        return trashRevealed;
    }

    public static void setClassicScore(final int newBestScore) {
        Preferences prefs = Gdx.app.getPreferences("SaveTheOceanPreferences");
        prefs.putInteger("classicScore", newBestScore);
        prefs.flush();
    }

    public static int getInfinityRunScore() {
        Preferences prefs = Gdx.app.getPreferences("SaveTheOceanPreferences");
        if (!prefs.contains("infinityRun")) {
            prefs.putInteger("infinityRun", 0); // default
            prefs.flush();
        }
        int trashRevealed = prefs.getInteger("infinityRun");
        return trashRevealed;
    }

    public static void setInfinityRunScore(final int newBestScore) {
        Preferences prefs = Gdx.app.getPreferences("SaveTheOceanPreferences");
        prefs.putInteger("infinityRun", newBestScore);
        prefs.flush();
    }

    public static int getStrandedAnimalsScore() {
        Preferences prefs = Gdx.app.getPreferences("SaveTheOceanPreferences");
        if (!prefs.contains("strandedAnimals")) {
            prefs.putInteger("strandedAnimals", 0); // default
            prefs.flush();
        }
        int trashRevealed = prefs.getInteger("strandedAnimals");
        return trashRevealed;
    }

    public static void setStrandedAnimalsScore(final int newStrandedAnimalsScore) {
        Preferences prefs = Gdx.app.getPreferences("SaveTheOceanPreferences");
        prefs.putInteger("strandedAnimals", newStrandedAnimalsScore);
        prefs.flush();
    }

    public static int getRandomElement(int[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }

}
