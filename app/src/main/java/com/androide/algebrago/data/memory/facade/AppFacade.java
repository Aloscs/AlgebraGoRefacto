package com.androide.algebrago.data.memory.facade;

import android.content.Context;

import com.androide.algebrago.domain.models.Achievement;
import com.androide.algebrago.domain.models.Block;
import com.androide.algebrago.domain.models.Exercise;
import com.androide.algebrago.domain.models.Level;
import com.androide.algebrago.data.memory.factory.ExerciseFactory;
import com.androide.algebrago.shared.observer.AchievementManager;
import com.androide.algebrago.shared.observer.ScoreManager;

import java.util.ArrayList;
import java.util.List;

/**
 * PATTERN: Facade
 * Provides a single simplified interface to the entire subsystem:
 * ExerciseFactory, ScoreManager, AchievementManager.
 *
 * Activities only talk to AppFacade — they never touch factories
 * or managers directly.
 */
public class AppFacade {

    private static AppFacade instance;

    private final ScoreManager scoreManager;
    private final AchievementManager achievementManager;

    private final List<Block> blocks;

    private AppFacade(Context context) {

        scoreManager = ScoreManager.getInstance(context);

        achievementManager = new AchievementManager(context);

        // Observer Pattern connection
        scoreManager.addObserver(achievementManager);

        blocks = buildAllBlocks();

        restoreProgress();
    }

    public static synchronized AppFacade getInstance(Context context) {

        if (instance == null) {
            instance = new AppFacade(context.getApplicationContext());
        }

        return instance;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Public API used by Activities / ViewModels
    // ─────────────────────────────────────────────────────────────────────────

    public List<Block> getAllBlocks() {
        return blocks;
    }

    public Block getBlock(int blockId) {

        for (Block block : blocks) {

            if (block.getId() == blockId) {
                return block;
            }
        }

        return null;
    }

    public Level getLevel(int blockId, int levelId) {

        Block block = getBlock(blockId);

        if (block == null) {
            return null;
        }

        for (Level level : block.getLevels()) {

            if (level.getId() == levelId) {
                return level;
            }
        }

        return null;
    }

    public List<Exercise> getExercisesForLevel(int blockId, int levelId) {

        Level level = getLevel(blockId, levelId);

        if (level == null) {
            return new ArrayList<>();
        }

        // Prototype Pattern
        List<Exercise> clonedExercises = new ArrayList<>();

        for (Exercise exercise : level.getExercises()) {
            clonedExercises.add(exercise.clone());
        }

        return clonedExercises;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Score / Progress
    // ─────────────────────────────────────────────────────────────────────────

    public void submitCorrectAnswer(int points, boolean firstAttempt) {

        int awardedPoints = firstAttempt
                ? points
                : Math.max(10, points / 3);

        scoreManager.addPoints(awardedPoints);

        scoreManager.incrementStreak();
    }

    public void submitWrongAnswer() {

        scoreManager.resetStreak();
    }

    public void markLevelComplete(int blockId, int levelId) {

        Level level = getLevel(blockId, levelId);

        if (level != null) {

            level.setCompleted(true);
        }

        Block block = getBlock(blockId);

        if (block != null) {

            // block.recalculateProgress();

            // Si luego haces ProgressManager,
            // esto se movería ahí.
        }

        // Observer notification
        scoreManager.notifyLevelCompleted(levelId, blockId);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Getters
    // ─────────────────────────────────────────────────────────────────────────

    public int getTotalScore() {
        return scoreManager.getTotalScore();
    }

    public int getCurrentStreak() {
        return scoreManager.getCurrentStreak();
    }

    public ScoreManager getScoreManager() {
        return scoreManager;
    }

    public List<Achievement> getAchievements() {
        return achievementManager.getAchievements();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Blocks / Levels setup
    // ─────────────────────────────────────────────────────────────────────────

    private List<Block> buildAllBlocks() {

        List<Block> list = new ArrayList<>();

        // ── Block 1 ─────────────────────────────────────────────────────────

        List<Level> b1Levels = new ArrayList<>();

        b1Levels.add(new Level(
                11,
                1,
                "Nivel 1",
                "Ecuaciones básicas de una incógnita",
                ExerciseFactory.createBlock1Level1Exercises()
        ));

        b1Levels.add(new Level(
                12,
                1,
                "Nivel 2",
                "Ecuaciones con multiplicación y división",
                ExerciseFactory.createBlock1Level1Exercises()
        ));

        b1Levels.add(new Level(
                13,
                1,
                "Nivel 3",
                "Combinación suma/resta/mult",
                ExerciseFactory.createBlock1Level1Exercises()
        ));

        list.add(new Block(
                1,
                "Ecuaciones Simples",
                "Ecuaciones de primer grado básicas",
                b1Levels
        ));

        // ── Block 2 ─────────────────────────────────────────────────────────

        List<Level> b2Levels = new ArrayList<>();

        b2Levels.add(new Level(
                21,
                2,
                "Nivel 1",
                "Verificar solución en dos variables",
                ExerciseFactory.createBlock2Level1Exercises()
        ));

        b2Levels.add(new Level(
                22,
                2,
                "Nivel 2",
                "Despejar una variable dada la otra",
                ExerciseFactory.createBlock2Level1Exercises()
        ));

        b2Levels.add(new Level(
                23,
                2,
                "Nivel 3",
                "Combinaciones con coeficientes",
                ExerciseFactory.createBlock2Level1Exercises()
        ));

        list.add(new Block(
                2,
                "Dos Variables",
                "Ecuaciones con x e y",
                b2Levels
        ));

        // ── Block 3 ─────────────────────────────────────────────────────────

        List<Level> b3Levels = new ArrayList<>();

        b3Levels.add(new Level(
                31,
                3,
                "Nivel 1",
                "Ecuaciones con paréntesis simples",
                ExerciseFactory.createBlock3Level1Exercises()
        ));

        b3Levels.add(new Level(
                32,
                3,
                "Nivel 2",
                "Doble distribución",
                ExerciseFactory.createBlock3Level1Exercises()
        ));

        b3Levels.add(new Level(
                33,
                3,
                "Nivel 3",
                "Paréntesis anidados",
                ExerciseFactory.createBlock3Level1Exercises()
        ));

        list.add(new Block(
                3,
                "Paréntesis",
                "Distributividad y agrupación",
                b3Levels
        ));

        // ── Block 4 ─────────────────────────────────────────────────────────

        List<Level> b4Levels = new ArrayList<>();

        b4Levels.add(new Level(
                41,
                4,
                "Nivel 1",
                "Fracciones simples",
                ExerciseFactory.createBlock4Level1Exercises()
        ));

        b4Levels.add(new Level(
                42,
                4,
                "Nivel 2",
                "MCM y eliminación de denominadores",
                ExerciseFactory.createBlock4Level1Exercises()
        ));

        b4Levels.add(new Level(
                43,
                4,
                "Nivel 3",
                "Fracciones combinadas",
                ExerciseFactory.createBlock4Level1Exercises()
        ));

        list.add(new Block(
                4,
                "Fracciones",
                "Ecuaciones con coeficientes fraccionarios",
                b4Levels
        ));

        // ── Block 5 ─────────────────────────────────────────────────────────

        List<Level> b5Levels = new ArrayList<>();

        b5Levels.add(new Level(
                51,
                5,
                "Nivel 1",
                "Ecuaciones multinomiales",
                ExerciseFactory.createBlock5Level1Exercises()
        ));

        b5Levels.add(new Level(
                52,
                5,
                "Nivel 2",
                "Combinaciones avanzadas",
                ExerciseFactory.createBlock5Level1Exercises()
        ));

        b5Levels.add(new Level(
                53,
                5,
                "Nivel 3",
                "Ecuaciones complejas",
                ExerciseFactory.createBlock5Level1Exercises()
        ));

        list.add(new Block(
                5,
                "Combinadas",
                "Ecuaciones con múltiples operaciones",
                b5Levels
        ));

        return list;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Progress restore
    // ─────────────────────────────────────────────────────────────────────────

    private void restoreProgress() {

        // Aquí luego puedes usar ProgressManager
        // si decides separarlo del ScoreManager.
    }
}