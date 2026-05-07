package com.androide.algebrago.domain.models;

import java.util.List;

/**
 * A block groups related levels around an algebraic topic.
 * Examples: "Ecuaciones simples", "Ecuaciones con dos variables", etc.
 */
public class Block {
    private int id;
    private String name;
    private String topic;
    private List<Level> levels;
    private int progressPercent; // 0–100

    public Block(int id, String name, String topic, List<Level> levels) {
        this.id = id;
        this.name = name;
        this.topic = topic;
        this.levels = levels;
        this.progressPercent = 0;
    }


    // ── Getters ────────────────────────────────────────────────────────────────

    public int getId() { return id; }
    public String getName() { return name; }
    public String getTopic() { return topic; }
    public List<Level> getLevels() { return levels; }
    public int getProgressPercent() { return progressPercent; }
    public void setProgressPercent(int progressPercent) { this.progressPercent = progressPercent; }
}
