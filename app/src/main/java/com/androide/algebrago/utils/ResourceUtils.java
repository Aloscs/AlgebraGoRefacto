package com.androide.algebrago.utils;

/**
 * Clase de utilidades estáticas reutilizables.
 * Evita duplicación de lógica entre Activities y ViewModels.
 */
public final class ResourceUtils {

    private ResourceUtils() { /* No instanciar */ }

    /**
     * Formatea un puntaje con cero a la izquierda si es necesario.
     * Ejemplo: 42 → "0042", 1500 → "1500"
     */
    public static String formatScore(int score) {
        return String.format("%04d", score);
    }

    /**
     * Determina el nivel de dificultad en texto legible.
     */
    public static String difficultyLabel(int difficulty) {
        switch (difficulty) {
            case 1:  return "Fácil";
            case 2:  return "Medio";
            case 3:  return "Difícil";
            default: return "—";
        }
    }

    /**
     * Valida que un String no sea nulo ni vacío.
     */
    public static boolean isNullOrEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    /**
     * Retorna el valor si no es nulo, o el fallback si es nulo.
     */
    public static String orDefault(String value, String fallback) {
        return value != null ? value : fallback;
    }
}
