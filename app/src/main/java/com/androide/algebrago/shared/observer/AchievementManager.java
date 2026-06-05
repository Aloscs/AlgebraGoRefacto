package com.androide.algebrago.shared.observer;

import android.content.Context;
import android.content.SharedPreferences;

import com.androide.algebrago.domain.models.Achievement;
import com.androide.algebrago.shared.observer.ProgressObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * PATRÓN: Observer
 *
 * {@code AchievementManager} actúa como OBSERVADOR del {@link ScoreManager}.
 *
 * Escucha:
 * <ul>
 *   <li>cambios de puntuación</li>
 *   <li>cambios de racha</li>
 *   <li>niveles completados</li>
 * </ul>
 * y decide cuándo desbloquear logros.
 *
 * Responsabilidades:
 * <ul>
 *   <li>Gestionar y evaluar el estado de los logros.</li>
 *   <li>Persistir el estado de desbloqueo en {@link android.content.SharedPreferences}.</li>
 *   <li>Exponer la lista de logros para la UI.</li>
 * </ul>
 */
public class AchievementManager implements ProgressObserver {

    // ─────────────────────────────────────────────────────────────────────────
    // SharedPreferences
    // ─────────────────────────────────────────────────────────────────────────

    private static final String PREFS_NAME = "algebrago_achievements";

    private final SharedPreferences prefs;

    // ─────────────────────────────────────────────────────────────────────────
    // Achievements
    // ─────────────────────────────────────────────────────────────────────────

    private final List<Achievement> achievements;

    // ─────────────────────────────────────────────────────────────────────────
    // Constructor
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Crea un nuevo {@code AchievementManager}, inicializa los logros por defecto
     * y carga su estado de desbloqueo desde las preferencias.
     *
     * @param context contexto de la aplicación para acceder a SharedPreferences.
     */
    public AchievementManager(Context context) {

        prefs = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        achievements = buildDefaultAchievements();

        loadAchievementState();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Observer Events
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Recibe eventos de score global.
     *
     * @param newScore nuevo puntaje acumulado.
     */
    @Override
    public void onScoreChanged(int newScore) {
        // Actualmente no hay achievements por score
    }

    /**
     * Evalúa logros asociados a rachas de respuestas correctas.
     *
     * @param streak racha actual de aciertos consecutivos.
     */
    @Override
    public void onStreakChanged(int streak) {

        checkStreakAchievements(streak);
    }

    /**
     * Evalúa logros asociados a finalización de nivel.
     *
     * @param levelId identificador del nivel completado.
     * @param blockId identificador del bloque del nivel.
     */
    @Override
    public void onLevelCompleted(int levelId, int blockId) {

        checkLevelAchievements(levelId, blockId);
    }

    /**
     * Callback requerido por la interfaz; no se utiliza en esta implementación.
     *
     * @param achievementName nombre del logro desbloqueado.
     */
    @Override
    public void onAchievementUnlocked(String achievementName) {
        // No se usa aquí
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Achievement Logic
    // ─────────────────────────────────────────────────────────────────────────

    private void checkScoreAchievements() {
        // hook for score-based achievements in the future
    }

    private void checkStreakAchievements(int streak) {

        for (Achievement achievement : achievements) {

            if (!achievement.isUnlocked()
                    && achievement.getType() == Achievement.AchievementType.STREAK_CORRECT
                    && streak >= achievement.getRequiredValue()) {

                unlockAchievement(achievement);
            }
        }
    }

    private void checkLevelAchievements(int levelId, int blockId) {

        for (Achievement achievement : achievements) {

            if (!achievement.isUnlocked()
                    && achievement.getType() == Achievement.AchievementType.LEVEL_COMPLETE) {

                unlockAchievement(achievement);
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Unlock Logic
    // ─────────────────────────────────────────────────────────────────────────

    private void unlockAchievement(Achievement achievement) {

        achievement.setUnlocked(true);

        achievement.setUnlockedAt(System.currentTimeMillis());

        saveAchievementState(achievement);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Persistence
    // ─────────────────────────────────────────────────────────────────────────

    private void saveAchievementState(Achievement achievement) {

        prefs.edit()
                .putBoolean("achievement_" + achievement.getId(), true)
                .putLong("achievement_time_" + achievement.getId(),
                        achievement.getUnlockedAt())
                .apply();
    }

    private void loadAchievementState() {

        for (Achievement achievement : achievements) {

            boolean unlocked = prefs.getBoolean(
                    "achievement_" + achievement.getId(),
                    false
            );

            if (unlocked) {

                achievement.setUnlocked(true);

                achievement.setUnlockedAt(
                        prefs.getLong(
                                "achievement_time_" + achievement.getId(),
                                0
                        )
                );
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Default Achievements
    // ─────────────────────────────────────────────────────────────────────────

    private List<Achievement> buildDefaultAchievements() {

        List<Achievement> list = new ArrayList<>();

        list.add(new Achievement(
                1,
                "Aprendiz de Álgebra",
                "Completa tu primer ejercicio",
                Achievement.AchievementType.LEVEL_COMPLETE,
                1
        ));

        list.add(new Achievement(
                2,
                "Primer Nivel Completo",
                "Completa el primer nivel",
                Achievement.AchievementType.LEVEL_COMPLETE,
                1
        ));

        list.add(new Achievement(
                3,
                "Racha x5",
                "5 respuestas correctas seguidas",
                Achievement.AchievementType.STREAK_CORRECT,
                5
        ));

        list.add(new Achievement(
                4,
                "Racha x10",
                "10 respuestas correctas seguidas",
                Achievement.AchievementType.STREAK_CORRECT,
                10
        ));

        list.add(new Achievement(
                5,
                "Nivel Perfecto",
                "Resuelve un nivel sin errores",
                Achievement.AchievementType.PERFECT_LEVEL,
                1
        ));

        return list;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Public API
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * @return lista de todos los logros con su estado de desbloqueo actual.
     */
    public List<Achievement> getAchievements() {

        return achievements;
    }
}