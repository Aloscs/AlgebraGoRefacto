package com.androide.algebrago.domain.models;

import java.util.List;

/**
 * Representa un nivel dentro de un bloque temático.
 * Contiene los ejercicios del nivel y registra el estado de completitud,
 * la mejor puntuación y el número de intentos.
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

    /**
     * Construye un nivel con su información y lista de ejercicios.
     * El nivel se crea en estado no completado, sin puntuación y sin intentos.
     *
     * @param id          identificador único del nivel.
     * @param blockId     ID del bloque al que pertenece.
     * @param name        nombre del nivel (ej. "Nivel 1").
     * @param description descripción breve del contenido del nivel.
     * @param exercises   lista de ejercicios que componen el nivel.
     */
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

    /** @return identificador único del nivel. */
    public int getId() { return id; }
    /** @return ID del bloque al que pertenece este nivel. */
    public int getBlockId() { return blockId; }
    /** @return nombre del nivel. */
    public String getName() { return name; }
    /** @return descripción breve del contenido del nivel. */
    public String getDescription() { return description; }
    /** @return lista de ejercicios del nivel. */
    public List<Exercise> getExercises() { return exercises; }
    /** @return {@code true} si el nivel ha sido completado. */
    public boolean isCompleted() { return completed; }
    /**
     * Actualiza el estado de completitud del nivel.
     *
     * @param completed {@code true} para marcar el nivel como completado.
     */
    public void setCompleted(boolean completed) { this.completed = completed; }
    /** @return mejor puntuación obtenida en este nivel. */
    public int getBestScore() { return bestScore; }
    /**
     * Actualiza la mejor puntuación del nivel.
     *
     * @param bestScore nueva mejor puntuación.
     */
    public void setBestScore(int bestScore) { this.bestScore = bestScore; }
    /** @return número total de intentos realizados en este nivel. */
    public int getAttempts() { return attempts; }
    /**
     * Actualiza el contador de intentos del nivel.
     *
     * @param attempts nuevo número de intentos.
     */
    public void setAttempts(int attempts) { this.attempts = attempts; }
}
