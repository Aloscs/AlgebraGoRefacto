package com.androide.algebrago.domain.state;

/**
 * PATRÓN: State
 * Define el contrato de comportamiento para cada fase de la sesión del estudiante:
 * inactivo, explicando, resolviendo, revisando y completado.
 *
 * Referencia Tidwell: patrón "Wizard" — flujo lineal paso a paso donde cada
 * estado representa una etapa por la que el usuario avanza.
 */
public interface StudentState {
    /**
     * @return nombre identificador del estado (ej. "IDLE", "EXERCISING").
     */
    String getStateName();

    /**
     * @return {@code true} si en este estado se puede iniciar un ejercicio.
     */
    boolean canStartExercise();

    /**
     * @return {@code true} si en este estado se puede solicitar una pista.
     */
    boolean canShowHint();

    /**
     * @return {@code true} si en este estado se puede enviar una respuesta.
     */
    boolean canSubmitAnswer();

    /**
     * @return {@code true} si en este estado se puede navegar hacia atrás.
     */
    boolean canNavigateBack();

    /**
     * @return {@code true} si todos los ejercicios de la sesión han sido completados.
     */
    boolean isSessionComplete();
}
