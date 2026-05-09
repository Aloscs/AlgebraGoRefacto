package com.androide.algebrago.shared.observer;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

/**
 * PATTERN: Observer (Subject / Publisher side)
 * PATTERN: Singleton
 *
 * Responsabilidad:
 * - Gestionar score global
 * - Gestionar racha actual
 * - Notificar cambios a observadores
 * - Persistir score y streak
 *
 * NO maneja:
 * - Achievements
 * - Progress de bloques
 * - Daily login
 */
public class ScoreManager {

    // ─────────────────────────────────────────────────────────────────────────
    // SharedPreferences
    // ─────────────────────────────────────────────────────────────────────────

    private static final String PREFS_NAME = "algebrago_prefs";

    private static final String KEY_SCORE = "total_score";
    private static final String KEY_STREAK = "current_streak";
    private static final String KEY_BEST_STREAK = "best_streak";
    private static final String KEY_DAILY = "last_day_played";

    // ─────────────────────────────────────────────────────────────────────────
    // Singleton
    // ─────────────────────────────────────────────────────────────────────────

    private static ScoreManager instance;

    public static synchronized ScoreManager getInstance(Context context) {
        if (instance == null) {
            instance = new ScoreManager(context);
        }
        return instance;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // State
    // ─────────────────────────────────────────────────────────────────────────

    private final SharedPreferences prefs;

    private int totalScore;
    private int currentStreak;
    private int bestStreak;

    // ─────────────────────────────────────────────────────────────────────────
    // Observer Pattern
    // ─────────────────────────────────────────────────────────────────────────

    private final List<ProgressObserver> observers = new ArrayList<>();

    // ─────────────────────────────────────────────────────────────────────────
    // Constructor
    // ─────────────────────────────────────────────────────────────────────────

    private ScoreManager(Context context) {

        prefs = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        totalScore = prefs.getInt(KEY_SCORE, 0);
        currentStreak = prefs.getInt(KEY_STREAK, 0);
        bestStreak = prefs.getInt(KEY_BEST_STREAK, 0);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Observer Registration
    // ─────────────────────────────────────────────────────────────────────────

    public void addObserver(ProgressObserver observer) {

        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(ProgressObserver observer) {
        observers.remove(observer);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Score Logic
    // ─────────────────────────────────────────────────────────────────────────

    public void addPoints(int points) {

        totalScore += points;

        save();

        notifyScoreChanged();
    }

    public void resetScore() {

        totalScore = 0;

        save();

        notifyScoreChanged();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Streak Logic
    // ─────────────────────────────────────────────────────────────────────────

    public void incrementStreak() {

        currentStreak++;

        if (currentStreak > bestStreak) {
            bestStreak = currentStreak;
        }

        save();

        notifyStreakChanged();
    }

    public void resetStreak() {

        currentStreak = 0;

        save();

        notifyStreakChanged();
    }

    public void saveDailyLogin() {

        long today = System.currentTimeMillis() / 86400000L;

        long lastDay = prefs.getLong(KEY_DAILY, -1);

        if (lastDay == today - 1) {

            int days = prefs.getInt("daily_streak_count", 0) + 1;

            prefs.edit()
                    .putInt("daily_streak_count", days)
                    .putLong(KEY_DAILY, today)
                    .apply();

        } else if (lastDay != today) {

            prefs.edit()
                    .putInt("daily_streak_count", 1)
                    .putLong(KEY_DAILY, today)
                    .apply();
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Level Events
    // ─────────────────────────────────────────────────────────────────────────

    public void notifyLevelCompleted(int levelId, int blockId) {

        for (ProgressObserver observer : observers) {
            observer.onLevelCompleted(levelId, blockId);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Notifications
    // ─────────────────────────────────────────────────────────────────────────

    private void notifyScoreChanged() {

        for (ProgressObserver observer : observers) {
            observer.onScoreChanged(totalScore);
        }
    }

    private void notifyStreakChanged() {

        for (ProgressObserver observer : observers) {
            observer.onStreakChanged(currentStreak);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Persistence
    // ─────────────────────────────────────────────────────────────────────────

    private void save() {

        prefs.edit()
                .putInt(KEY_SCORE, totalScore)
                .putInt(KEY_STREAK, currentStreak)
                .putInt(KEY_BEST_STREAK, bestStreak)
                .apply();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Getters
    // ─────────────────────────────────────────────────────────────────────────

    public int getTotalScore() {
        return totalScore;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public int getBestStreak() {
        return bestStreak;
    }
}