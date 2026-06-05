package com.androide.algebrago.domain.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Modelo de dominio que representa un ejercicio algebraico.
 *
 * PATRÓN: Prototype — implementa {@link Cloneable} para que {@link com.androide.algebrago.data.memory.factory.ExerciseFactory}
 * pueda estampar copias sin reconstruirlas desde cero, evitando un objeto por ejercicio en tiempo de ejecución.
 *
 * Soporta dos tipos de interfaz de usuario definidos en {@link ExerciseType}.
 */
public class Exercise implements Cloneable {

    /**
     * Tipo de interfaz de resolución del ejercicio.
     */
    public enum ExerciseType {
        /** Interfaz de selección: el estudiante elige la opción que completa la ecuación. */
        COMPLETE_EQUATION,
        /** Interfaz de balanza: el estudiante arrastra números a los platillos de la balanza. */
        BALANCE_SCALE
    }

    private int id;
    private String equationDisplay;       // e.g. "?+5?=11"  or "?+5?=11"
    private String equationFull;          // e.g. "x+5y=11"
    //private String leftSide;              // For balance: "__+5__"
    //private String rightSide;            // For balance: "11"

    private List<Term> leftSideTerms;
    private List<Term> rightSideTerms;
    private List<String> options;         // Selectable options
    private List<String> correctValues;   // Correct values to fill blanks
    private String hint;                  // Text hint (not the answer)
    private String explanation;           // Step-by-step after answer
    private ExerciseType type;
    private int pointValue;
    private int levelId;
    private int blockId;

    /** Constructor vacío. Inicializa todas las listas internas. */
    public Exercise() {
        options = new ArrayList<>();
        correctValues = new ArrayList<>();
        leftSideTerms = new ArrayList<>();
        rightSideTerms = new ArrayList<>();
    }

    /**
     * Constructor completo para crear ejercicios desde el Repository o la Factory.
     *
     * @param id               identificador único del ejercicio.
     * @param equationDisplay  ecuación con huecos para mostrar en la UI (ej. "?+5?=11").
     * @param equationFull     ecuación completa sin huecos (ej. "x+5y=11").
     * @param leftSideTerms    términos del lado izquierdo de la ecuación (para balanza).
     * @param rightSideTerms   términos del lado derecho de la ecuación (para balanza).
     * @param options          opciones de respuesta seleccionables.
     * @param correctValues    valores correctos que completan la ecuación.
     * @param hint             pista textual (no revela la respuesta).
     * @param explanation      explicación paso a paso para la retroalimentación.
     * @param type             tipo de interfaz de resolución.
     * @param pointValue       puntos que otorga este ejercicio al responderse correctamente.
     * @param levelId          ID del nivel al que pertenece.
     * @param blockId          ID del bloque al que pertenece.
     */
    // Full constructor
    public Exercise(int id, String equationDisplay, String equationFull,
                    List<Term> leftSideTerms, List<Term> rightSideTerms,
                    List<String> options, List<String> correctValues,
                    String hint, String explanation,
                    ExerciseType type, int pointValue, int levelId, int blockId) {
        this.id = id;
        this.equationDisplay = equationDisplay;
        this.equationFull = equationFull;
       // this.leftSide = leftSide;
        //this.rightSide = rightSide;
        this.leftSideTerms = leftSideTerms != null ? leftSideTerms : new ArrayList<>();
        this.rightSideTerms = rightSideTerms != null ? rightSideTerms : new ArrayList<>();
        this.options = options != null ? options : new ArrayList<>();
        this.correctValues = correctValues != null ? correctValues : new ArrayList<>();
        this.hint = hint;
        this.explanation = explanation;
        this.type = type;
        this.pointValue = pointValue;
        this.levelId = levelId;
        this.blockId = blockId;
    }

    /**
     * Crea una copia profunda del ejercicio (Patrón Prototype).
     * Las listas internas (options, correctValues, leftSideTerms, rightSideTerms)
     * se copian para que las modificaciones en la copia no afecten al original.
     *
     * @return copia independiente de este ejercicio.
     * @throws RuntimeException si la clonación no está soportada (nunca debería ocurrir).
     */
    @Override
    public Exercise clone() {
        try {
            Exercise copy = (Exercise) super.clone();

            copy.options = new ArrayList<>(this.options);
            copy.correctValues = new ArrayList<>(this.correctValues);

            copy.leftSideTerms = new ArrayList<>(this.leftSideTerms);
            copy.rightSideTerms = new ArrayList<>(this.rightSideTerms);

            return copy;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Cannot clone Exercise", e);
        }
    }

    // ── Getters & Setters ──────────────────────────────────────────────────────

    /** @return identificador único del ejercicio. */
    public int getId() { return id; }
    /** @param id nuevo identificador. */
    public void setId(int id) { this.id = id; }

    /** @return lista de términos del lado izquierdo de la ecuación. */
    public List<Term> getLeftSideTerms() { return leftSideTerms; }
    /** @param leftSideTerms términos del lado izquierdo. */
    public void setLeftSideTerms(List<Term> leftSideTerms) { this.leftSideTerms = leftSideTerms; }

    /** @return lista de términos del lado derecho de la ecuación. */
    public List<Term> getRightSideTerms() { return rightSideTerms; }
    /** @param rightSideTerms términos del lado derecho. */
    public void setRightSideTerms(List<Term> rightSideTerms) { this.rightSideTerms = rightSideTerms; }
    /** @return representación con huecos para la UI (ej. "?+5?=11"). */
    public String getEquationDisplay() { return equationDisplay; }
    /** @param equationDisplay representación con huecos. */
    public void setEquationDisplay(String equationDisplay) { this.equationDisplay = equationDisplay; }

    /** @return ecuación completa sin huecos (ej. "x+5y=11"). */
    public String getEquationFull() { return equationFull; }
    /** @param equationFull ecuación completa. */
    public void setEquationFull(String equationFull) { this.equationFull = equationFull; }

    /** @return opciones de respuesta seleccionables. */
    public List<String> getOptions() { return options; }
    /** @param options nuevas opciones de respuesta. */
    public void setOptions(List<String> options) { this.options = options; }

    /** @return valores correctos que completan la ecuación. */
    public List<String> getCorrectValues() { return correctValues; }
    /** @param correctValues nuevos valores correctos. */
    public void setCorrectValues(List<String> correctValues) { this.correctValues = correctValues; }

    /** @return pista textual (no revela la respuesta directamente). */
    public String getHint() { return hint; }
    /** @param hint nueva pista textual. */
    public void setHint(String hint) { this.hint = hint; }

    /** @return explicación paso a paso para la retroalimentación. */
    public String getExplanation() { return explanation; }
    /** @param explanation nueva explicación. */
    public void setExplanation(String explanation) { this.explanation = explanation; }

    /** @return tipo de interfaz de resolución del ejercicio. */
    public ExerciseType getType() { return type; }
    /** @param type nuevo tipo de interfaz. */
    public void setType(ExerciseType type) { this.type = type; }

    /** @return puntos que otorga este ejercicio al responderse correctamente. */
    public int getPointValue() { return pointValue; }
    /** @param pointValue nuevo valor de puntos. */
    public void setPointValue(int pointValue) { this.pointValue = pointValue; }

    /** @return ID del nivel al que pertenece este ejercicio. */
    public int getLevelId() { return levelId; }
    /** @param levelId nuevo ID de nivel. */
    public void setLevelId(int levelId) { this.levelId = levelId; }

    /** @return ID del bloque al que pertenece este ejercicio. */
    public int getBlockId() { return blockId; }
    /** @param blockId nuevo ID de bloque. */
    public void setBlockId(int blockId) { this.blockId = blockId; }
}
