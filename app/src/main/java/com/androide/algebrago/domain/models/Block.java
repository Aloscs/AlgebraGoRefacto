package com.androide.algebrago.domain.models;

import java.util.List;

/**
 * Agrupa niveles relacionados bajo un mismo tema algebraico.
 * Ejemplos: "Ecuaciones simples", "Ecuaciones con dos variables", etc.
 *
 * El progreso del bloque (0–100) se recalcula en {@link com.androide.algebrago.feature.home.MainViewModel}
 * según cuántos niveles han sido completados.
 */
public class Block {
    private int id;
    private String name;
    private String topic;
    private List<Level> levels;
    private int progressPercent; // 0–100

    /**
     * Construye un bloque con su lista de niveles.
     * El progreso inicial es 0%.
     *
     * @param id     identificador único del bloque.
     * @param name   nombre del bloque (ej. "Ecuaciones Simples").
     * @param topic  descripción breve del tema algebraico.
     * @param levels lista de niveles que componen el bloque.
     */
    public Block(int id, String name, String topic, List<Level> levels) {
        this.id = id;
        this.name = name;
        this.topic = topic;
        this.levels = levels;
        this.progressPercent = 0;
    }


    // ── Getters ────────────────────────────────────────────────────────────────

    /** @return identificador único del bloque. */
    public int getId() { return id; }
    /** @return nombre del bloque. */
    public String getName() { return name; }
    /** @return descripción del tema algebraico del bloque. */
    public String getTopic() { return topic; }
    /** @return lista de niveles del bloque. */
    public List<Level> getLevels() { return levels; }
    /** @return porcentaje de progreso del bloque (0–100). */
    public int getProgressPercent() { return progressPercent; }
    /**
     * Actualiza el porcentaje de progreso del bloque.
     *
     * @param progressPercent nuevo porcentaje (0–100).
     */
    public void setProgressPercent(int progressPercent) { this.progressPercent = progressPercent; }
}
