package com.androide.algebrago.shared.observer;

import android.content.Context;
import android.content.SharedPreferences;

import com.androide.algebrago.domain.models.Achievement;

import java.util.ArrayList;
import java.util.List;

/**
 * PATTERN: Observer (Subject / Publisher side).
 * Also acts as the single source of truth for score and streak data.
 * Persists to SharedPreferences for offline support.
 *
 * PATTERN: Singleton — one ScoreManager per app process ensures
 * all screens share the same score state.
 */
public class ScoreManager {

    private static final String PREFS_NAME  = "algebrago_prefs";
    private static final String KEY_SCORE   = "total_score";
    private static final String KEY_STREAK  = "current_streak";
    private static final String KEY_STREAK_BEST = "best_streak";
    private static final String KEY_DAILY   = "last_day_played";

    private static ScoreManager instance;

    private final SharedPreferences prefs;
    private int totalScore;
    private int currentStreak;
    private int bestStreak;
    private final List<ProgressObserver> observers = new ArrayList<>();
    private final List<Achievement> achievements;

    private ScoreManager(Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        totalScore    = prefs.getInt(KEY_SCORE, 0);
        currentStreak = prefs.getInt(KEY_STREAK, 0);
        bestStreak    = prefs.getInt(KEY_STREAK_BEST, 0);
        achievements  = buildDefaultAchievements();
        loadAchievementState();
    }

    public static synchronized ScoreManager getInstance(Context context) {
        if (instance == null) instance = new ScoreManager(context);
        return instance;
    }

    // ── Observer registration ─────────────────────────────────────────────────

    public void addObserver(ProgressObserver o)    { if (!observers.contains(o)) observers.add(o); }
    public void removeObserver(ProgressObserver o) { observers.remove(o); }

    // ── Score mutations ───────────────────────────────────────────────────────

    public void addPoints(int points) {
        totalScore += points;
        save();
        for (ProgressObserver o : observers) o.onScoreChanged(totalScore);
        checkScoreAchievements();
    }

    public void incrementStreak() {
        currentStreak++;
        if (currentStreak > bestStreak) bestStreak = currentStreak;
        save();
        for (ProgressObserver o : observers) o.onStreakChanged(currentStreak);
        checkStreakAchievements();
    }

    public void resetStreak() {
        currentStreak = 0;
        save();
        for (ProgressObserver o : observers) o.onStreakChanged(currentStreak);
    }

    public void notifyLevelCompleted(int levelId, int blockId) {
        for (ProgressObserver o : observers) o.onLevelCompleted(levelId, blockId);
    }

    // ── Achievement management ────────────────────────────────────────────────

    private void checkStreakAchievements() {
        for (Achievement a : achievements) {
            if (!a.isUnlocked() && a.getType() == Achievement.AchievementType.STREAK_CORRECT
                    && currentStreak >= a.getRequiredValue()) {
                unlockAchievement(a);
            }
        }
    }

    private void checkScoreAchievements() {
        // hook for score-based achievements in the future
    }

    public void unlockAchievement(Achievement achievement) {
        if (!achievement.isUnlocked()) {
            achievement.setUnlocked(true);
            achievement.setUnlockedAt(System.currentTimeMillis());
            saveAchievementState(achievement);
            for (ProgressObserver o : observers) o.onAchievementUnlocked(achievement.getName());
        }
    }

    public void checkAndUnlockAchievement(Achievement.AchievementType type, int value) {
        for (Achievement a : achievements) {
            if (!a.isUnlocked() && a.getType() == type && value >= a.getRequiredValue()) {
                unlockAchievement(a);
            }
        }
    }

    // ── Persistence ───────────────────────────────────────────────────────────

    private void save() {
        prefs.edit()
             .putInt(KEY_SCORE, totalScore)
             .putInt(KEY_STREAK, currentStreak)
             .putInt(KEY_STREAK_BEST, bestStreak)
             .apply();
    }

    private void saveAchievementState(Achievement a) {
        prefs.edit()
             .putBoolean("ach_" + a.getId(), true)
             .putLong("ach_ts_" + a.getId(), a.getUnlockedAt())
             .apply();
    }

    private void loadAchievementState() {
        for (Achievement a : achievements) {
            boolean unlocked = prefs.getBoolean("ach_" + a.getId(), false);
            if (unlocked) {
                a.setUnlocked(true);
                a.setUnlockedAt(prefs.getLong("ach_ts_" + a.getId(), 0));
            }
        }
    }

    // ── Default achievements ──────────────────────────────────────────────────

    private List<Achievement> buildDefaultAchievements() {
        List<Achievement> list = new ArrayList<>();
        list.add(new Achievement(1, "Aprendiz de Álgebra",
                "Completa tu primer ejercicio", Achievement.AchievementType.LEVEL_COMPLETE, 1));
        list.add(new Achievement(2, "Primer Nivel Completo",
                "Completa el primer nivel", Achievement.AchievementType.LEVEL_COMPLETE, 1));
        list.add(new Achievement(3, "Racha x5",
                "5 respuestas correctas seguidas", Achievement.AchievementType.STREAK_CORRECT, 5));
        list.add(new Achievement(4, "Racha x10",
                "10 respuestas correctas seguidas", Achievement.AchievementType.STREAK_CORRECT, 10));
        list.add(new Achievement(5, "Nivel Perfecto",
                "Resuelve un nivel sin errores", Achievement.AchievementType.PERFECT_LEVEL, 1));
        list.add(new Achievement(6, "3 Días Seguidos",
                "Practica 3 días consecutivos", Achievement.AchievementType.DAILY_STREAK, 3));
        list.add(new Achievement(7, "7 Días Seguidos",
                "Practica 7 días consecutivos", Achievement.AchievementType.DAILY_STREAK, 7));
        list.add(new Achievement(8, "Maestro del Despeje",
                "Completa el bloque de ecuaciones de primer grado",
                Achievement.AchievementType.BLOCK_COMPLETE, 1));
        list.add(new Achievement(9, "Dominando Fracciones",
                "Completa el bloque de fracciones",
                Achievement.AchievementType.TOPIC_MASTERY, 1));
        list.add(new Achievement(10, "Todos los Bloques",
                "Completa todos los bloques disponibles",
                Achievement.AchievementType.BLOCK_COMPLETE, 5));
        return list;
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    public int getTotalScore()    { return totalScore; }
    public int getCurrentStreak() { return currentStreak; }
    public int getBestStreak()    { return bestStreak; }
    public List<Achievement> getAchievements() { return achievements; }

    public void saveBlockProgress(int blockId, int percent) {
        prefs.edit().putInt("block_progress_" + blockId, percent).apply();
    }

    public int getBlockProgress(int blockId) {
        return prefs.getInt("block_progress_" + blockId, 0);
    }

    public void saveLevelCompleted(int levelId) {
        prefs.edit().putBoolean("level_done_" + levelId, true).apply();
    }

    public boolean isLevelCompleted(int levelId) {
        return prefs.getBoolean("level_done_" + levelId, false);
    }

    public void saveDailyLogin() {
        long today = System.currentTimeMillis() / 86400000L;
        long lastDay = prefs.getLong(KEY_DAILY, -1);
        if (lastDay == today - 1) {
            int days = prefs.getInt("daily_streak_count", 0) + 1;
            prefs.edit().putInt("daily_streak_count", days).putLong(KEY_DAILY, today).apply();
            checkAndUnlockAchievement(Achievement.AchievementType.DAILY_STREAK, days);
        } else if (lastDay != today) {
            prefs.edit().putInt("daily_streak_count", 1).putLong(KEY_DAILY, today).apply();
        }
    }
}
