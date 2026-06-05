package com.androide.algebrago.domain.models;

/**
 * Representa un logro de gamificación que el estudiante puede desbloquear.
 *
 * Cada logro tiene un tipo ({@link AchievementType}), un valor requerido para
 * desbloquearse (ej. racha de 5) y un estado de desbloqueo persistido en
 * {@link com.androide.algebrago.shared.observer.ScoreManager}.
 */
public class Achievement {
    /**
     * Categorías de logros disponibles en la aplicación.
     */
    public enum AchievementType {
        /** Completar un nivel. */
        LEVEL_COMPLETE,
        /** Acumular una racha de respuestas correctas. */
        STREAK_CORRECT,
        /** Iniciar sesión en días consecutivos. */
        DAILY_STREAK,
        /** Completar un nivel sin errores. */
        PERFECT_LEVEL,
        /** Completar todos los niveles de un bloque. */
        BLOCK_COMPLETE,
        /** Dominar un tema algebraico completo. */
        TOPIC_MASTERY
    }

    private int id;
    private String name;
    private String description;
    private AchievementType type;
    private boolean unlocked;
    private long unlockedAt;
    private int requiredValue; // e.g. 5 for "streak of 5"

    /**
     * Construye un logro con todos sus metadatos iniciales.
     * El logro se crea en estado bloqueado ({@code unlocked = false}).
     *
     * @param id            identificador único del logro.
     * @param name          nombre corto visible en la UI.
     * @param description   descripción legible del requisito de desbloqueo.
     * @param type          categoría del logro.
     * @param requiredValue valor mínimo para desbloquearse (ej. 5 para racha de 5).
     */
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

    /** @return identificador único del logro. */
    public int getId() { return id; }
    /** @return nombre corto del logro. */
    public String getName() { return name; }
    /** @return descripción del requisito de desbloqueo. */
    public String getDescription() { return description; }
    /** @return categoría del logro. */
    public AchievementType getType() { return type; }
    /** @return {@code true} si el logro ha sido desbloqueado. */
    public boolean isUnlocked() { return unlocked; }
    /**
     * Establece el estado de desbloqueo del logro.
     *
     * @param unlocked {@code true} para marcarlo como desbloqueado.
     */
    public void setUnlocked(boolean unlocked) { this.unlocked = unlocked; }
    /** @return marca de tiempo Unix (ms) en que se desbloqueó; 0 si aún no está desbloqueado. */
    public long getUnlockedAt() { return unlockedAt; }
    /**
     * Establece la marca de tiempo de desbloqueo.
     *
     * @param unlockedAt época Unix en milisegundos.
     */
    public void setUnlockedAt(long unlockedAt) { this.unlockedAt = unlockedAt; }
    /** @return valor mínimo requerido para desbloquear este logro. */
    public int getRequiredValue() { return requiredValue; }
}
