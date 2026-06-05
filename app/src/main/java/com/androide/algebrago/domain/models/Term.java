package com.androide.algebrago.domain.models;

/**
 * Representa un elemento individual dentro de una ecuación matemática.
 * Modelo de Dominio Anémico.
 */
public class Term {

    public enum TermType {
        VARIABLE,    // Ej: "x", "y"
        CONSTANT,    // Ej: "5", "11", "-3"
        OPERATOR,    // Ej: "+", "-", "*", "/", "="
        PARENTHESIS, // Ej: "(", ")"
        BLANK        // Representa un espacio vacío ("?") para el Drag & Drop
    }

    private String id; // Identificador único, muy útil para el Drag & Drop si hay números repetidos
    private TermType type;
    private String value;

    /**
     * Construye un término con identificador, tipo y valor.
     *
     * @param id    identificador único del término, útil en Drag &amp; Drop para distinguir
     *              términos con el mismo valor (ej. dos "5" distintos).
     * @param type  categoría del término ({@link TermType}).
     * @param value representación textual del término (ej. "x", "5", "+").
     */
    public Term(String id, TermType type, String value) {
        this.id = id;
        this.type = type;
        this.value = value;
    }

    // ── Getters & Setters ──────────────────────────────────────────────────────

    /** @return identificador único del término. */
    public String getId() { return id; }
    /** @param id nuevo identificador. */
    public void setId(String id) { this.id = id; }

    /** @return tipo del término. */
    public TermType getType() { return type; }
    /** @param type nuevo tipo. */
    public void setType(TermType type) { this.type = type; }

    /** @return valor textual del término. */
    public String getValue() { return value; }
    /** @param value nuevo valor textual. */
    public void setValue(String value) { this.value = value; }
}