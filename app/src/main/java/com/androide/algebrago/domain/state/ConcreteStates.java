package com.androide.algebrago.domain.state;

// ── Idle ──────────────────────────────────────────────────────────────────────
class IdleState implements StudentState {
    @Override public String getStateName() { return "IDLE"; }
    @Override public boolean canStartExercise() { return true; }
    @Override public boolean canShowHint() { return false; }
    @Override public boolean canSubmitAnswer() { return false; }
    @Override public boolean canNavigateBack() { return true; }
    @Override public boolean isSessionComplete() { return false; }
}

// ── Explaining ────────────────────────────────────────────────────────────────
class ExplainingState implements StudentState {
    @Override public String getStateName() { return "EXPLAINING"; }
    @Override public boolean canStartExercise() { return true; }
    @Override public boolean canShowHint() { return false; }
    @Override public boolean canSubmitAnswer() { return false; }
    @Override public boolean canNavigateBack() { return true; }
    @Override public boolean isSessionComplete() { return false; }
}

// ── Exercising ────────────────────────────────────────────────────────────────
class ExercisingState implements StudentState {
    @Override public String getStateName() { return "EXERCISING"; }
    @Override public boolean canStartExercise() { return false; }
    @Override public boolean canShowHint() { return true; }
    @Override public boolean canSubmitAnswer() { return true; }
    @Override public boolean canNavigateBack() { return false; }
    @Override public boolean isSessionComplete() { return false; }
}

// ── Reviewing ─────────────────────────────────────────────────────────────────
class ReviewingState implements StudentState {
    @Override public String getStateName() { return "REVIEWING"; }
    @Override public boolean canStartExercise() { return false; }
    @Override public boolean canShowHint() { return false; }
    @Override public boolean canSubmitAnswer() { return false; }
    @Override public boolean canNavigateBack() { return true; }
    @Override public boolean isSessionComplete() { return false; }
}

// ── Completed ─────────────────────────────────────────────────────────────────
class CompletedState implements StudentState {
    @Override public String getStateName() { return "COMPLETED"; }
    @Override public boolean canStartExercise() { return false; }
    @Override public boolean canShowHint() { return false; }
    @Override public boolean canSubmitAnswer() { return false; }
    @Override public boolean canNavigateBack() { return true; }
    @Override public boolean isSessionComplete() { return true; }
}
