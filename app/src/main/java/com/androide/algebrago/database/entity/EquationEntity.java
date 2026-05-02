package com.androide.algebrago.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entidad Room que representa una ecuación matemática en la base de datos SQLite.
 *
 * Tabla: equations
 * Propósito: persistir las ecuaciones con todos sus metadatos para que puedan
 * consultarse, filtrarse y actualizarse sin depender de código hardcodeado.
 *
 * Relación con MVVM:
 *   Model → Entity (datos brutos de BD)
 *   Repository → accede a esta tabla vía EquationDao
 *   ViewModel → llama al Repository y expone LiveData<List<EquationEntity>>
 *   View (Activity) → observa el LiveData y actualiza la UI
 */
@Entity(tableName = "equations")
public class EquationEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    /** Ecuación completa: "x + 5y = 11" */
    @NonNull
    @ColumnInfo(name = "equation_full")
    public String equationFull = "";

    /** Representación con huecos para modo completar: "? + 5? = 11" */
    @ColumnInfo(name = "equation_display")
    public String equationDisplay;

    /** Lado izquierdo para modo balanza: "__+5__" */
    @ColumnInfo(name = "left_side")
    public String leftSide;

    /** Lado derecho para modo balanza: "11" */
    @ColumnInfo(name = "right_side")
    public String rightSide;

    /**
     * Opciones de respuesta serializadas como JSON.
     * Ejemplo: ["x=1,y=2","x=3,y=9","x=6,y=1","x=2,y=3"]
     * Se deserializa mediante StringListConverter.
     */
    @NonNull
    @ColumnInfo(name = "options_json")
    public String optionsJson = "[]";

    /**
     * Valores correctos serializados como JSON.
     * Ejemplo: ["x=1,y=2"]
     */
    @NonNull
    @ColumnInfo(name = "correct_values_json")
    public String correctValuesJson = "[]";

    /** Pista textual (no revela la respuesta). */
    @ColumnInfo(name = "hint")
    public String hint;

    /** Explicación paso a paso que se muestra en retroalimentación. */
    @ColumnInfo(name = "explanation")
    public String explanation;

    /**
     * Tipo de ejercicio: "COMPLETE_EQUATION" o "BALANCE_SCALE".
     * Almacenado como String para legibilidad en la BD.
     */
    @NonNull
    @ColumnInfo(name = "exercise_type")
    public String exerciseType = "COMPLETE_EQUATION";

    /** Puntos que otorga este ejercicio al responderse correctamente. */
    @ColumnInfo(name = "point_value")
    public int pointValue = 100;

    /** ID del nivel al que pertenece este ejercicio (FK lógica). */
    @ColumnInfo(name = "level_id")
    public int levelId;

    /** ID del bloque temático al que pertenece (FK lógica). */
    @ColumnInfo(name = "block_id")
    public int blockId;

    /** Dificultad del ejercicio: 1=fácil, 2=medio, 3=difícil. */
    @ColumnInfo(name = "difficulty")
    public int difficulty = 1;

    /** Indica si el ejercicio está activo y debe mostrarse al usuario. */
    @ColumnInfo(name = "is_active")
    public boolean isActive = true;

    /** Timestamp de creación/importación (epoch ms). */
    @ColumnInfo(name = "created_at")
    public long createdAt;

    // ── Constructores ─────────────────────────────────────────────────────────

    /** Constructor vacío requerido por Room. */
    public EquationEntity() {
        this.createdAt = System.currentTimeMillis();
    }

    /** Constructor completo para crear entidades desde el Factory. */
    public EquationEntity(@NonNull String equationFull, String equationDisplay,
                          String leftSide, String rightSide,
                          @NonNull String optionsJson, @NonNull String correctValuesJson,
                          String hint, String explanation,
                          @NonNull String exerciseType, int pointValue,
                          int levelId, int blockId, int difficulty) {
        this.equationFull      = equationFull;
        this.equationDisplay   = equationDisplay;
        this.leftSide          = leftSide;
        this.rightSide         = rightSide;
        this.optionsJson       = optionsJson;
        this.correctValuesJson = correctValuesJson;
        this.hint              = hint;
        this.explanation       = explanation;
        this.exerciseType      = exerciseType;
        this.pointValue        = pointValue;
        this.levelId           = levelId;
        this.blockId           = blockId;
        this.difficulty        = difficulty;
        this.isActive          = true;
        this.createdAt         = System.currentTimeMillis();
    }
}
