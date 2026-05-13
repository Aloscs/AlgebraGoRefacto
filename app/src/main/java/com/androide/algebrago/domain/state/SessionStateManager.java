package com.androide.algebrago.domain.state;

/**
 * Context class for the State pattern.
 * Controls transitions between student session states.
 */
public class SessionStateManager {

    private StudentState currentState;

    // Singleton states (flyweight — shared, stateless)
    private static final StudentState IDLE       = new IdleState();
    private static final StudentState EXPLAINING = new ExplainingState();
    private static final StudentState EXERCISING = new ExercisingState();
    private static final StudentState REVIEWING  = new ReviewingState();
    private static final StudentState COMPLETED  = new CompletedState();

    public SessionStateManager() {
        currentState = IDLE;
    }

    public void transitionToExplaining() { currentState = EXPLAINING; }
    public void transitionToExercising() { currentState = EXERCISING; }
    public void transitionToReviewing()  { currentState = REVIEWING; }
    public void transitionToCompleted()  { currentState = COMPLETED; }
    public void reset()                  { currentState = IDLE; }

    public StudentState getState() { return currentState; }
    public String getStateName()   { return currentState.getStateName(); }

    public boolean canShowHint()      { return currentState.canShowHint(); }
    public boolean canSubmitAnswer()  { return currentState.canSubmitAnswer(); }
    public boolean isSessionComplete(){ return currentState.isSessionComplete(); }
    public boolean canNavigateBack()  { return currentState.canNavigateBack(); }
}
