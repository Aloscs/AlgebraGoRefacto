package com.androide.algebrago.models;

/**
 * Represents a gamification achievement badge.
 */
public class Achievement {
    public enum AchievementType {
        LEVEL_COMPLETE,
        STREAK_CORRECT,
        DAILY_STREAK,
        PERFECT_LEVEL,
        BLOCK_COMPLETE,
        TOPIC_MASTERY
    }

    private int id;
    private String name;
    private String description;
    private AchievementType type;
    private boolean unlocked;
    private long unlockedAt;
    private int requiredValue; // e.g. 5 for "streak of 5"

    public Achievement(int id, String name, String description, AchievementType type, int requiredValue) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.requiredValue = requiredValue;
        this.unlocked = false;
        this.unlockedAt = 0;
    }

    // ── Getters ────────────────────────────────────────────────────────────────

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public AchievementType getType() { return type; }
    public boolean isUnlocked() { return unlocked; }
    public void setUnlocked(boolean unlocked) { this.unlocked = unlocked; }
    public long getUnlockedAt() { return unlockedAt; }
    public void setUnlockedAt(long unlockedAt) { this.unlockedAt = unlockedAt; }
    public int getRequiredValue() { return requiredValue; }
}
