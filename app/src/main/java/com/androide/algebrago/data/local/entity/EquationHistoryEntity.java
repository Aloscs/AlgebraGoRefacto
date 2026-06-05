package com.androide.algebrago.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entidad que representa el historial de resolución de ecuaciones.
 * Cumple con el requisito de almacenar la ecuación, marcas de tiempo (timestamp)
 * y el resultado de la sesión.
 */
@Entity(tableName = "equation_history")
public class EquationHistoryEntity {

    /** Identificador único generado automáticamente por Room. */
    @PrimaryKey(autoGenerate = true)
    public int id;

    /** ID del bloque temático al que pertenece este intento. */
    public int blockId;

    /** ID del nivel dentro del bloque al que pertenece este intento. */
    public int levelId;

    /**
     * Ecuación serializada como cadena de texto (ej. "x+2=5").
     * Puede almacenarse como ecuación cruda o como JSON de List&lt;Term&gt;.
     */
    public String serializedEquation;

    /** {@code true} si el estudiante respondió correctamente; {@code false} en caso contrario. */
    public boolean isCorrect;

    /** Marca de tiempo Unix (epoch en milisegundos) de cuándo se resolvió el ejercicio. */
    public long timestamp;

    /**
     * Construye un registro de historial con todos sus campos.
     *
     * @param blockId            ID del bloque temático.
     * @param levelId            ID del nivel dentro del bloque.
     * @param serializedEquation ecuación serializada como texto.
     * @param isCorrect          {@code true} si la respuesta fue correcta.
     * @param timestamp          marca de tiempo en milisegundos (epoch).
     */
    public EquationHistoryEntity(int blockId, int levelId, String serializedEquation, boolean isCorrect, long timestamp) {
        this.blockId = blockId;
        this.levelId = levelId;
        this.serializedEquation = serializedEquation;
        this.isCorrect = isCorrect;
        this.timestamp = timestamp;
    }
}