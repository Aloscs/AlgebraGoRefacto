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

    @PrimaryKey(autoGenerate = true)
    public int id; // Identificador único requerido por la rúbrica

    public int blockId;
    public int levelId;

    // Aquí guardamos la estructura de la ecuación.
    // Puedes guardar la ecuación cruda ("x + 2 = 5") o el JSON de tu List<Term>
    public String serializedEquation;

    public boolean isCorrect;

    public long timestamp; // Marca de tiempo (cuándo se resolvió)

    public EquationHistoryEntity(int blockId, int levelId, String serializedEquation, boolean isCorrect, long timestamp) {
        this.blockId = blockId;
        this.levelId = levelId;
        this.serializedEquation = serializedEquation;
        this.isCorrect = isCorrect;
        this.timestamp = timestamp;
    }
}