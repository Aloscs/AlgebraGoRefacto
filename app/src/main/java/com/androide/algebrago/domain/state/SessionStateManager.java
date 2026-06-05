package com.androide.algebrago.domain.state;

/**
 * Contexto del patrón State para gestionar los estados de la sesión del estudiante.
 * Controla las transiciones entre los estados definidos en {@link StudentState}.
 *
 * Los estados concretos son singletons compartidos (flyweight), ya que son
 * sin estado propio y pueden reutilizarse entre sesiones.
 */
public class SessionStateManager {

    private StudentState currentState;

    // Singleton states (flyweight — shared, stateless)
    private static final StudentState IDLE       = new IdleState();
    private static final StudentState EXPLAINING = new ExplainingState();
    private static final StudentState EXERCISING = new ExercisingState();
    private static final StudentState REVIEWING  = new ReviewingState();
    private static final StudentState COMPLETED  = new CompletedState();

    /**
     * Crea un nuevo gestor de estado iniciando en el estado {@code IDLE}.
     */
    public SessionStateManager() {
        currentState = IDLE;
    }

    /** Transiciona al estado de explicación (mostrando teoría previa). */
    public void transitionToExplaining() { currentState = EXPLAINING; }
    /** Transiciona al estado activo de resolución de ejercicio. */
    public void transitionToExercising() { currentState = EXERCISING; }
    /** Transiciona al estado de revisión de retroalimentación. */
    public void transitionToReviewing()  { currentState = REVIEWING; }
    /** Transiciona al estado de sesión completada. */
    public void transitionToCompleted()  { currentState = COMPLETED; }
    /** Reinicia la sesión al estado inicial {@code IDLE}. */
    public void reset()                  { currentState = IDLE; }

    /**
     * @return estado actual de la sesión.
     */
    public StudentState getState() { return currentState; }
    /**
     * @return nombre del estado actual (ej. "IDLE", "EXERCISING").
     */
    public String getStateName()   { return currentState.getStateName(); }

    /** @return {@code true} si el estado actual permite mostrar una pista. */
    public boolean canShowHint()      { return currentState.canShowHint(); }
    /** @return {@code true} si el estado actual permite enviar una respuesta. */
    public boolean canSubmitAnswer()  { return currentState.canSubmitAnswer(); }
    /** @return {@code true} si todos los ejercicios de la sesión han sido completados. */
    public boolean isSessionComplete(){ return currentState.isSessionComplete(); }
    /** @return {@code true} si el estado actual permite navegar hacia atrás. */
    public boolean canNavigateBack()  { return currentState.canNavigateBack(); }
}
