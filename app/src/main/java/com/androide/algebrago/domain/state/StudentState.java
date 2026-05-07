package com.androide.algebrago.domain.state;

/**
 * PATTERN: State
 * Manages the student's session state during an exercise session.
 * Each concrete state class defines behavior for that phase
 * (idle, explaining, exercising, reviewing, completed).
 *
 * Tidwell reference: "Wizard" pattern — a linear step-by-step flow
 * where each state represents a step the user progresses through.
 */
public interface StudentState {
    String getStateName();
    boolean canStartExercise();
    boolean canShowHint();
    boolean canSubmitAnswer();
    boolean canNavigateBack();
    boolean isSessionComplete();
}
