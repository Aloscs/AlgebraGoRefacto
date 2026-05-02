package com.androide.algebrago.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Exercise model.
 * PATTERN: Prototype — cloneable so ExerciseFactory can stamp out copies
 * without rebuilding from scratch. Avoids one object per exercise at runtime.
 */
public class Exercise implements Cloneable {

    public enum ExerciseType {
        COMPLETE_EQUATION,  // Interfaz 1: select option that completes equation
        BALANCE_SCALE       // Interfaz 2: drag numbers onto balance pans
    }

    private int id;
    private String equationDisplay;       // e.g. "?+5?=11"  or "?+5?=11"
    private String equationFull;          // e.g. "x+5y=11"
    private String leftSide;              // For balance: "__+5__"
    private String rightSide;            // For balance: "11"
    private List<String> options;         // Selectable options
    private List<String> correctValues;   // Correct values to fill blanks
    private String hint;                  // Text hint (not the answer)
    private String explanation;           // Step-by-step after answer
    private ExerciseType type;
    private int pointValue;
    private int levelId;
    private int blockId;

    public Exercise() {
        options = new ArrayList<>();
        correctValues = new ArrayList<>();
    }

    // Full constructor
    public Exercise(int id, String equationDisplay, String equationFull,
                    String leftSide, String rightSide,
                    List<String> options, List<String> correctValues,
                    String hint, String explanation,
                    ExerciseType type, int pointValue, int levelId, int blockId) {
        this.id = id;
        this.equationDisplay = equationDisplay;
        this.equationFull = equationFull;
        this.leftSide = leftSide;
        this.rightSide = rightSide;
        this.options = options != null ? options : new ArrayList<>();
        this.correctValues = correctValues != null ? correctValues : new ArrayList<>();
        this.hint = hint;
        this.explanation = explanation;
        this.type = type;
        this.pointValue = pointValue;
        this.levelId = levelId;
        this.blockId = blockId;
    }

    @Override
    public Exercise clone() {
        try {
            Exercise copy = (Exercise) super.clone();
            copy.options = new ArrayList<>(this.options);
            copy.correctValues = new ArrayList<>(this.correctValues);
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Cannot clone Exercise", e);
        }
    }

    // ── Getters & Setters ──────────────────────────────────────────────────────

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getEquationDisplay() { return equationDisplay; }
    public void setEquationDisplay(String equationDisplay) { this.equationDisplay = equationDisplay; }

    public String getEquationFull() { return equationFull; }
    public void setEquationFull(String equationFull) { this.equationFull = equationFull; }

    public String getLeftSide() { return leftSide; }
    public void setLeftSide(String leftSide) { this.leftSide = leftSide; }

    public String getRightSide() { return rightSide; }
    public void setRightSide(String rightSide) { this.rightSide = rightSide; }

    public List<String> getOptions() { return options; }
    public void setOptions(List<String> options) { this.options = options; }

    public List<String> getCorrectValues() { return correctValues; }
    public void setCorrectValues(List<String> correctValues) { this.correctValues = correctValues; }

    public String getHint() { return hint; }
    public void setHint(String hint) { this.hint = hint; }

    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }

    public ExerciseType getType() { return type; }
    public void setType(ExerciseType type) { this.type = type; }

    public int getPointValue() { return pointValue; }
    public void setPointValue(int pointValue) { this.pointValue = pointValue; }

    public int getLevelId() { return levelId; }
    public void setLevelId(int levelId) { this.levelId = levelId; }

    public int getBlockId() { return blockId; }
    public void setBlockId(int blockId) { this.blockId = blockId; }
}
