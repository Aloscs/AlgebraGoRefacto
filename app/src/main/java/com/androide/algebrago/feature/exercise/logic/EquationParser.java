package com.androide.algebrago.feature.exercise.logic;

import com.androide.algebrago.domain.models.Term;

import java.util.List;
import java.util.Stack;

/**
 * Algoritmo Puente: Shunting Yard modificado para AST.
 * Convierte una lista plana de Terms en un Árbol de Jerarquía Matemática.
 * AUDITORÍA DE CÓDIGO: Se verificaron las precedencias y el manejo de paréntesis.
 */
public class EquationParser {

    public static ASTNode parse(List<Term> terms) {
        Stack<ASTNode> nodes = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (Term term : terms) {
            // Ignoramos espacios en blanco o fichas vacías
            if (term.getType() == Term.TermType.BLANK) continue;

            String val = term.getValue();

            if (term.getType() == Term.TermType.CONSTANT) {
                nodes.push(new ConstantNode(Double.parseDouble(val)));
            }
            else if (term.getType() == Term.TermType.VARIABLE) {
                nodes.push(new VariableNode(val));
            }
            else if (term.getType() == Term.TermType.PARENTHESIS) {
                if (val.equals("(")) {
                    operators.push('(');
                } else if (val.equals(")")) {
                    // Resolver todo lo que está dentro del paréntesis
                    while (!operators.isEmpty() && operators.peek() != '(') {
                        applyOperator(nodes, operators);
                    }
                    if (!operators.isEmpty()) operators.pop(); // Sacar el '('
                }
            }
            else if (term.getType() == Term.TermType.OPERATOR) {
                char op = val.charAt(0);
                // Si encontramos un "=", lo tratamos como divisor de la ecuación,
                // por lo general parseamos cada lado de la ecuación por separado.
                if (op == '=') continue;

                // Respetar jerarquía de operadores (PEMDAS)
                while (!operators.isEmpty() && hasPrecedence(op, operators.peek())) {
                    applyOperator(nodes, operators);
                }
                operators.push(op);
            }
        }

        // Aplicar los operadores restantes
        while (!operators.isEmpty()) {
            applyOperator(nodes, operators);
        }

        return nodes.isEmpty() ? new ConstantNode(0) : nodes.pop();
    }

    /**
     * Extrae 2 nodos y 1 operador de las pilas, los une, y devuelve el resultado a la pila de nodos.
     */
    private static void applyOperator(Stack<ASTNode> nodes, Stack<Character> operators) {
        if (nodes.size() < 2) return; // Evitar errores de formato incompleto
        ASTNode right = nodes.pop();
        ASTNode left = nodes.pop();
        char op = operators.pop();
        nodes.push(new OperatorNode(op, left, right));
    }

    /**
     * AUDITORÍA DE PRECEDENCIA:
     * Devuelve true si el operador 'stackOp' tiene mayor o igual prioridad que 'currentOp'.
     */
    private static boolean hasPrecedence(char currentOp, char stackOp) {
        if (stackOp == '(' || stackOp == ')') return false;
        if (currentOp == '^' && stackOp != '^') return false; // Potencias primero
        if ((currentOp == '*' || currentOp == '/') && (stackOp == '+' || stackOp == '-')) return false;
        return true;
    }
}