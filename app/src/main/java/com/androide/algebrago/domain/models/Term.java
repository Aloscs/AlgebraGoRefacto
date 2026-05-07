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

    public Term(String id, TermType type, String value) {
        this.id = id;
        this.type = type;
        this.value = value;
    }

    // ── Getters & Setters ──────────────────────────────────────────────────────

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public TermType getType() { return type; }
    public void setType(TermType type) { this.type = type; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
}