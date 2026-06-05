package com.androide.algebrago.feature.exercise.logic;

import java.util.Map;

/**
 * Representa un nodo en el Árbol de Sintaxis Abstracta (AST) matemático.
 * Cada nodo puede evaluarse dado un mapa de variables (nombre → valor).
 */
public abstract class ASTNode {
    /**
     * Evalúa este nodo recursivamente y retorna su valor numérico.
     *
     * @param variables mapa de variables con sus valores actuales (ej. "x" → 3.0).
     * @return resultado numérico de la evaluación.
     */
    public abstract double evaluate(Map<String, Double> variables);
}

// ── Nodos Hoja (Valores y Variables) ──────────────────────────────────────

/**
 * Nodo hoja que representa una constante numérica (ej. 5, -3, 2.5).
 */
class ConstantNode extends ASTNode {
    private final double value;
    /** @param value valor constante de este nodo. */
    public ConstantNode(double value) { this.value = value; }

    @Override
    public double evaluate(Map<String, Double> variables) { return value; }
}

/**
 * Nodo hoja que representa una variable algebraica (ej. "x", "y").
 * Si la variable no está en el mapa, se asume valor 0.
 */
class VariableNode extends ASTNode {
    private final String name;
    /** @param name nombre de la variable (ej. "x"). */
    public VariableNode(String name) { this.name = name; }

    @Override
    public double evaluate(Map<String, Double> variables) {
        // Si la variable (ej. "x") existe en el mapa, devuelve su valor, si no, asume 0
        return variables.containsKey(name) ? variables.get(name) : 0.0;
    }
}

// ── Nodos Rama (Operaciones Matemáticas) ──────────────────────────────────

/**
 * Nodo rama que representa una operación binaria entre dos subárboles.
 * Operadores soportados: +, -, *, /, ^ (potencia).
 */
class OperatorNode extends ASTNode {
    private final char operator;
    private final ASTNode left;
    private final ASTNode right;

    /**
     * Construye un nodo de operación binaria.
     *
     * @param operator operador matemático (+, -, *, /, ^).
     * @param left     subárbol del operando izquierdo.
     * @param right    subárbol del operando derecho.
     */
    public OperatorNode(char operator, ASTNode left, ASTNode right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    @Override
    public double evaluate(Map<String, Double> variables) {
        double leftVal = left.evaluate(variables);
        double rightVal = right.evaluate(variables);

        switch (operator) {
            case '+': return leftVal + rightVal;
            case '-': return leftVal - rightVal;
            case '*': return leftVal * rightVal;
            case '/':
                if (rightVal == 0) throw new ArithmeticException("División por cero");
                return leftVal / rightVal;
            case '^': return Math.pow(leftVal, rightVal);
            default: throw new UnsupportedOperationException("Operador desconocido: " + operator);
        }
    }
}