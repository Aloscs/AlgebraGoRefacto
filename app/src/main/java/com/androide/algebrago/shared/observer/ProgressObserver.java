package com.androide.algebrago.shared.observer;

/**
 * PATTERN: Observer
 * Any UI component that needs to react to score/progress changes
 * implements this interface. The ScoreManager (subject) notifies all
 * registered observers whenever state changes.
 *
 * Tidwell: "Immediate Feedback" — the interface updates the moment
 * something changes, giving the student instant acknowledgment.
 */
public interface ProgressObserver {
    void onScoreChanged(int newScore);
    void onStreakChanged(int newStreak);
    void onAchievementUnlocked(String achievementName);
    void onLevelCompleted(int levelId, int blockId);
}
