package com.androide.algebrago.models;

import java.util.List;

/**
 * Represents a level within a block.
 * Holds exercises and tracks completion state.
 */
public class Level {
    private int id;
    private int blockId;
    private String name;
    private String description;
    private List<Exercise> exercises;
    private boolean completed;
    private int bestScore;
    private int attempts;

    public Level(int id, int blockId, String name, String description, List<Exercise> exercises) {
        this.id = id;
        this.blockId = blockId;
        this.name = name;
        this.description = description;
        this.exercises = exercises;
        this.completed = false;
        this.bestScore = 0;
        this.attempts = 0;
    }

    // ── Getters & Setters ──────────────────────────────────────────────────────

    public int getId() { return id; }
    public int getBlockId() { return blockId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public List<Exercise> getExercises() { return exercises; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    public int getBestScore() { return bestScore; }
    public void setBestScore(int bestScore) { this.bestScore = bestScore; }
    public int getAttempts() { return attempts; }
    public void setAttempts(int attempts) { this.attempts = attempts; }
}
