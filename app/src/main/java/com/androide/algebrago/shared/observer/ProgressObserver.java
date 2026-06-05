package com.androide.algebrago.shared.observer;

/**
 * PATRÓN: Observer
 * Cualquier componente de la UI que necesite reaccionar a cambios de
 * puntuación o progreso implementa esta interfaz.
 * El {@link ScoreManager} (sujeto) notifica a todos los observadores
 * registrados cada vez que su estado cambia.
 *
 * Referencia Tidwell: "Retroalimentación inmediata" — la interfaz se actualiza
 * en el momento en que algo cambia, dando al estudiante un reconocimiento instantáneo.
 */
public interface ProgressObserver {
    /**
     * Se invoca cuando la puntuación total cambia.
     *
     * @param newScore nueva puntuación total.
     */
    void onScoreChanged(int newScore);

    /**
     * Se invoca cuando la racha de respuestas correctas cambia.
     *
     * @param newStreak nuevo valor de la racha.
     */
    void onStreakChanged(int newStreak);

    /**
     * Se invoca cuando se desbloquea un logro.
     *
     * @param achievementName nombre del logro desbloqueado.
     */
    void onAchievementUnlocked(String achievementName);

    /**
     * Se invoca cuando el estudiante completa un nivel.
     *
     * @param levelId ID del nivel completado.
     * @param blockId ID del bloque al que pertenece el nivel.
     */
    void onLevelCompleted(int levelId, int blockId);
}
