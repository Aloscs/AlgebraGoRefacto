package com.androide.algebrago.feature.exercise.logic;

import java.util.Map;

/**
 * Representa un nodo en la jerarquía matemática (Abstract Syntax Tree).
 */
public abstract class ASTNode {
    // Evalúa el nodo devolviendo un valor numérico.
    // Recibe un mapa con los valores de las variables (ej. x = 3).
    public abstract double evaluate(Map<String, Double> variables);
}

// ── Nodos Hoja (Valores y Variables) ──────────────────────────────────────

class ConstantNode extends ASTNode {
    private final double value;
    public ConstantNode(double value) { this.value = value; }

    @Override
    public double evaluate(Map<String, Double> variables) { return value; }
}

class VariableNode extends ASTNode {
    private final String name;
    public VariableNode(String name) { this.name = name; }

    @Override
    public double evaluate(Map<String, Double> variables) {
        // Si la variable (ej. "x") existe en el mapa, devuelve su valor, si no, asume 0
        return variables.containsKey(name) ? variables.get(name) : 0.0;
    }
}

// ── Nodos Rama (Operaciones Matemáticas) ──────────────────────────────────

class OperatorNode extends ASTNode {
    private final char operator;
    private final ASTNode left;
    private final ASTNode right;

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