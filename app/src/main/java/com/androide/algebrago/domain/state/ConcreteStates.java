package com.androide.algebrago.domain.state;

// ── Idle ──────────────────────────────────────────────────────────────────────

/**
 * Estado inicial de la sesión: el estudiante no ha comenzado ningún ejercicio.
 * Permite iniciar un ejercicio y navegar hacia atrás, pero no enviar respuestas.
 */
class IdleState implements StudentState {
    @Override public String getStateName() { return "IDLE"; }
    @Override public boolean canStartExercise() { return true; }
    @Override public boolean canShowHint() { return false; }
    @Override public boolean canSubmitAnswer() { return false; }
    @Override public boolean canNavigateBack() { return true; }
    @Override public boolean isSessionComplete() { return false; }
}

// ── Explaining ────────────────────────────────────────────────────────────────

/**
 * Estado de explicación: se muestra al estudiante la teoría previa al ejercicio.
 * Permite avanzar a un ejercicio, pero no enviar respuestas todavía.
 */
class ExplainingState implements StudentState {
    @Override public String getStateName() { return "EXPLAINING"; }
    @Override public boolean canStartExercise() { return true; }
    @Override public boolean canShowHint() { return false; }
    @Override public boolean canSubmitAnswer() { return false; }
    @Override public boolean canNavigateBack() { return true; }
    @Override public boolean isSessionComplete() { return false; }
}

// ── Exercising ────────────────────────────────────────────────────────────────

/**
 * Estado activo de resolución: el estudiante está respondiendo un ejercicio.
 * Habilita el envío de respuestas y la visualización de pistas.
 * Bloquea la navegación hacia atrás para evitar abandonar la sesión accidentalmente.
 */
class ExercisingState implements StudentState {
    @Override public String getStateName() { return "EXERCISING"; }
    @Override public boolean canStartExercise() { return false; }
    @Override public boolean canShowHint() { return true; }
    @Override public boolean canSubmitAnswer() { return true; }
    @Override public boolean canNavigateBack() { return false; }
    @Override public boolean isSessionComplete() { return false; }
}

// ── Reviewing ─────────────────────────────────────────────────────────────────

/**
 * Estado de revisión: se muestra la retroalimentación tras responder un ejercicio.
 * Solo permite navegar hacia atrás; no se pueden enviar nuevas respuestas.
 */
class ReviewingState implements StudentState {
    @Override public String getStateName() { return "REVIEWING"; }
    @Override public boolean canStartExercise() { return false; }
    @Override public boolean canShowHint() { return false; }
    @Override public boolean canSubmitAnswer() { return false; }
    @Override public boolean canNavigateBack() { return true; }
    @Override public boolean isSessionComplete() { return false; }
}

// ── Completed ─────────────────────────────────────────────────────────────────

/**
 * Estado de sesión completada: todos los ejercicios del nivel han sido respondidos.
 * Habilita la navegación hacia atrás y señala que la sesión terminó.
 */
class CompletedState implements StudentState {
    @Override public String getStateName() { return "COMPLETED"; }
    @Override public boolean canStartExercise() { return false; }
    @Override public boolean canShowHint() { return false; }
    @Override public boolean canSubmitAnswer() { return false; }
    @Override public boolean canNavigateBack() { return true; }
    @Override public boolean isSessionComplete() { return true; }
}
