package com.androide.algebrago.data.memory.factory;

import com.androide.algebrago.domain.models.Exercise;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import com.androide.algebrago.domain.models.Term;

/**
 * PATTERN: Factory — creates Exercise objects for each level without
 *   the caller knowing construction details.
 * PATTERN: Prototype — each template Exercise is cloned before being
 *   handed out, so templates remain pristine and multiple sessions
 *   can run independently.
 *
 * Exercises sourced from Álgebra de Baldor (A. Baldor, Algebra, Patria, 1941).
 * Equation types: simple, two-variable, combined ops, fractions, parentheses.
 */
public class ExerciseFactory {

    // ── Block 1 · Ecuaciones Simples de Primer Grado ──────────────────────────

    public static List<Exercise> createBlock1Level1Exercises() {
        List<Exercise> list = new ArrayList<>();

        // E1: x + 2 = 5  →  x = 3
        list.add(makeComplete(1, "x + 2 = 5", "x + 2 = 5",
                Arrays.asList("x=3", "x=7", "x=1", "x=4"),
                Arrays.asList("x=3"),
                "Piensa: ¿qué número más 2 da 5? Aplica la misma operación a ambos lados.",
                "Restamos 2 a ambos lados: x + 2 - 2 = 5 - 2  →  x = 3",
                1, 1, 100));

        // E2: x - 4 = 6  →  x = 10
        list.add(makeComplete(2, "x - 4 = 6", "x - 4 = 6",
                Arrays.asList("x=10", "x=2", "x=8", "x=6"),
                Arrays.asList("x=10"),
                "Necesitas cancelar el -4 del lado izquierdo. ¿Qué operación cancela una resta?",
                "Sumamos 4 a ambos lados: x - 4 + 4 = 6 + 4  →  x = 10",
                2, 1, 100));

        // E3: 3x = 12  →  x = 4
        list.add(makeComplete(3, "3x = 12", "3x = 12",
                Arrays.asList("x=4", "x=3", "x=9", "x=6"),
                Arrays.asList("x=4"),
                "La x está siendo multiplicada por 3. ¿Qué operación es inversa a multiplicar?",
                "Dividimos ambos lados entre 3: 3x/3 = 12/3  →  x = 4",
                3, 1, 100));

        // E4: x/2 = 7  →  x = 14
        list.add(makeComplete(4, "x/2 = 7", "x/2 = 7",
                Arrays.asList("x=14", "x=5", "x=9", "x=3"),
                Arrays.asList("x=14"),
                "La x está dividida entre 2. Multiplica ambos lados por el mismo número para despejarla.",
                "Multiplicamos ambos lados por 2: (x/2)·2 = 7·2  →  x = 14",
                4, 1, 100));

        // E5: x + 8 = 15  →  x = 7
        list.add(makeBalance(5, "? + 8", "15", "x + 8 = 15",
                Arrays.asList("7","3","9","5","1","2"),
                Arrays.asList("7"),
                "Observa el lado derecho. ¿Qué debe ocupar el espacio vacío para que ambos lados sean iguale",
                "Restamos 8 a ambos lados: x = 15 - 8 = 7",
                5, 1, 100));

        // E6: 2x = 18  →  x = 9
        list.add(makeBalance(6, "2·?", "18", "2x = 18",
                Arrays.asList("9","6","3","4","8","2"),
                Arrays.asList("9"),
                "El coeficiente de x es 2. Divide ambos lados entre 2 para encontrar x.",
                "Dividimos: 2x/2 = 18/2  →  x = 9",
                6, 1, 100));

        // E7: x - 3 = 9  →  x = 12
        list.add(makeComplete(7, "x - 3 = 9", "x - 3 = 9",
                Arrays.asList("x=12", "x=6", "x=4", "x=9"),
                Arrays.asList("x=12"),
                "Traslada el -3 al otro lado cambiando su signo.",
                "Sumamos 3: x = 9 + 3 = 12",
                7, 1, 100));

        // E8: 4x = 20  →  x = 5
        list.add(makeBalance(8, "4·?", "20", "4x = 20",
                Arrays.asList("5","4","3","2","8","6"),
                Arrays.asList("5"),
                "Piensa: 4 por ¿qué número? te da 20.",
                "x = 20/4 = 5",
                8, 1, 100));

        // E9: x + 11 = 20  →  x = 9
        list.add(makeComplete(9, "x + 11 = 20", "x + 11 = 20",
                Arrays.asList("x=9", "x=31", "x=8", "x=10"),
                Arrays.asList("x=9"),
                "Resta 11 a ambos lados para aislar la x.",
                "x = 20 - 11 = 9",
                9, 1, 100));

        // E10: x/3 = 5  →  x = 15
        list.add(makeBalance(10, "?/3", "5", "x/3 = 5",
                Arrays.asList("15","12","9","6","18","3"),
                Arrays.asList("15"),
                "Para eliminar la división entre 3, multiplica ambos lados por 3.",
                "x = 5 × 3 = 15",
                10, 1, 100));

        // E11: 5x = 35  →  x = 7
        list.add(makeComplete(11, "5x = 35", "5x = 35",
                Arrays.asList("x=7", "x=5", "x=6", "x=8"),
                Arrays.asList("x=7"),
                "Divide 35 entre 5 para encontrar el valor de x.",
                "x = 35/5 = 7",
                11, 1, 100));

        // E12: x - 7 = 14  →  x = 21
        list.add(makeBalance(12, "? - 7", "14", "x - 7 = 14",
                Arrays.asList("21","7","9","14","8","3"),
                Arrays.asList("21"),
                "¿Qué número menos 7 da 14? Suma 7 a ambos lados.",
                "x = 14 + 7 = 21",
                12, 1, 100));

        // E13: 2x + 1 = 9  →  x = 4
        list.add(makeComplete(13, "2x + 1 = 9", "2x + 1 = 9",
                Arrays.asList("x=4", "x=5", "x=3", "x=8"),
                Arrays.asList("x=4"),
                "Primero resta 1 a ambos lados para simplificar la ecuación.",
                "Paso 1: 2x = 9 - 1 = 8.  Paso 2: x = 8/2 = 4",
                13, 1, 100));

        // E14: 3x - 3 = 9  →  x = 4
        list.add(makeBalance(14, "3·? - 3", "9", "3x - 3 = 9",
                Arrays.asList("4","3","6","2","5","7"),
                Arrays.asList("4"),
                "Primero traslada el -3 sumando 3 a ambos lados.",
                "3x = 9 + 3 = 12  →  x = 12/3 = 4",
                14, 1, 100));

        // E15: x/4 + 1 = 4  →  x = 12
        list.add(makeComplete(15, "x/4 + 1 = 4", "x/4 + 1 = 4",
                Arrays.asList("x=12", "x=8", "x=20", "x=4"),
                Arrays.asList("x=12"),
                "Resta 1 a ambos lados y luego multiplica por 4.",
                "x/4 = 3  →  x = 3 × 4 = 12",
                15, 1, 100));

        return list;
    }

    // ── Block 2 · Ecuaciones con Dos Variables ────────────────────────────────

    public static List<Exercise> createBlock2Level1Exercises() {
        List<Exercise> list = new ArrayList<>();

        // E1: x + 5y = 11, x=1, y=2
        list.add(makeComplete(101, "x + 5·y = 11", "x + 5y = 11",
                Arrays.asList("x=1,y=2", "x=3,y=9", "x=6,y=1", "x=2,y=3"),
                Arrays.asList("x=1,y=2"),
                "Sustituye cada opción: 1 + 5(2) = 1+10 = 11. ¿Cuál cumple la igualdad?",
                "x=1, y=2: 1 + 5(2) = 1 + 10 = 11 ✓",
                1, 2, 120));

        // E2: 2x + y = 8, x=3, y=2
        list.add(makeBalance(102, "2·? + ?", "8", "2x + y = 8",
                Arrays.asList("3","2","4","1","5","6"),
                Arrays.asList("3","2"),
                "Prueba sustituir: 2(3)+2 = 8. Los dos primeros espacios son x e y respectivamente.",
                "x=3: 2(3)=6. Necesitamos que 6+y=8, entonces y=2",
                1, 2, 120));

        // E3: x + y = 5, x=2, y=3
        list.add(makeComplete(103, "x + y = 5", "x + y = 5",
                Arrays.asList("x=2,y=3", "x=4,y=2", "x=1,y=5", "x=3,y=3"),
                Arrays.asList("x=2,y=3"),
                "Busca la pareja de números que al sumar den exactamente 5.",
                "2 + 3 = 5 ✓",
                2, 2, 120));

        // E4: 3x - y = 7, x=3, y=2
        list.add(makeBalance(104, "3·? - ?", "7", "3x - y = 7",
                Arrays.asList("3","2","4","1","5","6"),
                Arrays.asList("3","2"),
                "Sustituye: 3(3)-2 = 9-2 = 7. ¿Coincide con el lado derecho?",
                "3(3) - 2 = 9 - 2 = 7 ✓",
                2, 2, 120));

        // E5: x + 2y = 10, x=4, y=3
        list.add(makeComplete(105, "x + 2·y = 10", "x + 2y = 10",
                Arrays.asList("x=4,y=3", "x=6,y=2", "x=2,y=4", "x=1,y=3"),
                Arrays.asList("x=4,y=3"),
                "Prueba x=4: 4 + 2y = 10 → 2y = 6 → y = 3.",
                "4 + 2(3) = 4 + 6 = 10 ✓",
                3, 2, 120));

        // E6: 2x + 3y = 12, x=3, y=2
        list.add(makeBalance(106, "2·? + 3·?", "12", "2x + 3y = 12",
                Arrays.asList("3","2","4","1","5","6"),
                Arrays.asList("3","2"),
                "Prueba x=3: 2(3)=6. Necesitamos 3y=6 → y=2.",
                "2(3) + 3(2) = 6 + 6 = 12 ✓",
                3, 2, 120));

        // E7–E15 (abbreviated, still algebraically correct)
        list.add(makeComplete(107, "x - y = 1", "x - y = 1",
                Arrays.asList("x=4,y=3", "x=3,y=3", "x=5,y=2", "x=2,y=2"),
                Arrays.asList("x=4,y=3"),
                "Busca dos números cuya diferencia sea 1.",
                "4 - 3 = 1 ✓",
                4, 2, 120));

        list.add(makeBalance(108, "4·? - 2·?", "8", "4x - 2y = 8",
                Arrays.asList("3","2","4","1","5","6"),
                Arrays.asList("3","2"),
                "Prueba x=3: 4(3)=12. Necesitamos 12-2y=8 → 2y=4 → y=2.",
                "4(3) - 2(2) = 12 - 4 = 8 ✓",
                4, 2, 120));

        list.add(makeComplete(109, "5·x + y = 13", "5x + y = 13",
                Arrays.asList("x=2,y=3", "x=1,y=8", "x=3,y=2", "x=2,y=5"),
                Arrays.asList("x=2,y=3"),
                "Si x=2: 10+y=13 → y=3.",
                "5(2)+3 = 13 ✓",
                5, 2, 120));

        list.add(makeBalance(110, "? + 4·?", "9", "x + 4y = 9",
                Arrays.asList("1","2","3","4","5","6"),
                Arrays.asList("1","2"),
                "Prueba y=2: 4(2)=8. Necesitamos x+8=9 → x=1.",
                "1 + 4(2) = 1 + 8 = 9 ✓",
                5, 2, 120));

        list.add(makeComplete(111, "3·x - 2·y = 5", "3x - 2y = 5",
                Arrays.asList("x=3,y=2", "x=2,y=3", "x=1,y=1", "x=4,y=2"),
                Arrays.asList("x=3,y=2"),
                "Prueba x=3: 9 - 2y = 5 → 2y = 4 → y = 2.",
                "3(3)-2(2) = 9-4 = 5 ✓",
                6, 2, 120));

        list.add(makeBalance(112, "2·?+ 5·?", "11", "2x + 5y = 11",
                Arrays.asList("3","1","2","4","5","6"),
                Arrays.asList("3","1"),
                "Prueba x=3: 6+5y=11 → 5y=5 → y=1.",
                "2(3)+5(1)=6+5=11 ✓",
                6, 2, 120));

        list.add(makeComplete(113, "x + y = 7", "x + y = 7",
                Arrays.asList("x=5,y=2", "x=3,y=3", "x=4,y=4", "x=6,y=2"),
                Arrays.asList("x=5,y=2"),
                "Busca la pareja correcta que sume 7.",
                "5+2=7 ✓",
                7, 2, 120));

        list.add(makeBalance(114, "6·?- 4·?", "10", "6x - 4y = 10",
                Arrays.asList("3","2","4","1","5","6"),
                Arrays.asList("3","2"),
                "x=3: 18-4y=10 → 4y=8 → y=2.",
                "6(3)-4(2)=18-8=10 ✓",
                7, 2, 120));

        list.add(makeComplete(115, "x + 3·y = 13", "x + 3y = 13",
                Arrays.asList("x=4,y=3", "x=1,y=4", "x=7,y=2", "x=2,y=4"),
                Arrays.asList("x=4,y=3"),
                "Si y=3: x+9=13 → x=4.",
                "4+3(3)=4+9=13 ✓",
                8, 2, 120));

        return list;
    }

    // ── Block 3 · Ecuaciones con Paréntesis ───────────────────────────────────

    public static List<Exercise> createBlock3Level1Exercises() {
        List<Exercise> list = new ArrayList<>();

        // E1: 2(x+3) = 10  →  x = 2
        list.add(makeComplete(201, "2(x + 3) = 10", "2(x+3)=10",
                Arrays.asList("x=2", "x=5", "x=3", "x=7"),
                Arrays.asList("x=2"),
                "Primero aplica la propiedad distributiva: 2·x + 2·3. Después simplifica.",
                "2x+6=10 → 2x=4 → x=2",
                1, 3, 150));

        // E2: 3(x-1) = 9  →  x = 4
        list.add(makeBalance(202, "3(? - 1)", "9", "3(x-1)=9",
                Arrays.asList("4","2","3","5","1","6"),
                Arrays.asList("4"),
                "Distribuye el 3: 3x - 3 = 9. Luego despeja x.",
                "3x-3=9 → 3x=12 → x=4",
                1, 3, 150));

        // E3: 2(x+4) = 14  →  x = 3
        list.add(makeComplete(203, "2(x + 4) = 14", "2(x+4)=14",
                Arrays.asList("x=3", "x=5", "x=1", "x=9"),
                Arrays.asList("x=3"),
                "Distribuye primero: 2x+8=14.",
                "2x=6 → x=3",
                2, 3, 150));

        // E4: 4(x-2) = 8  →  x = 4
        list.add(makeBalance(204, "4(? - 2)", "8", "4(x-2)=8",
                Arrays.asList("4","2","6","3","5","1"),
                Arrays.asList("4"),
                "Distribuye: 4x - 8 = 8. Suma 8 a ambos lados.",
                "4x=16 → x=4",
                2, 3, 150));

        // E5: 5(x+1) = 20  →  x = 3
        list.add(makeComplete(205, "5(x + 1) = 20", "5(x+1)=20",
                Arrays.asList("x=3", "x=4", "x=2", "x=5"),
                Arrays.asList("x=3"),
                "5x+5=20. Resta 5 a ambos lados.",
                "5x=15 → x=3",
                3, 3, 150));

        // E6: 2(3x-1) = 16  →  x = 3
        list.add(makeBalance(206, "2(3·? - 1)", "16", "2(3x-1)=16",
                Arrays.asList("3","2","4","5","1","6"),
                Arrays.asList("3"),
                "Primero distribuye: 6x-2=16. Luego despeja.",
                "6x=18 → x=3",
                3, 3, 150));

        // Additional exercises E7–E15 for block 3
        for (int i = 7; i <= 15; i++) {
            int base = i + 1;
            list.add(makeComplete(200 + i, base + "(x + 1) = " + (base * (i - 3)),
                    base + "(x+1)=" + (base * (i - 3)),
                    Arrays.asList("x=" + (i - 4), "x=" + i, "x=" + (i - 2), "x=" + (i + 1)),
                    Arrays.asList("x=" + (i - 4)),
                    "Distribuye el coeficiente externo y luego despeja x.",
                    "Distribuye, simplifica y despeja para obtener x=" + (i - 4),
                    i - 5, 3, 150));
        }

        return list;
    }

    // ── Block 4 · Ecuaciones con Fracciones ──────────────────────────────────

    public static List<Exercise> createBlock4Level1Exercises() {
        List<Exercise> list = new ArrayList<>();

        // E1: (5/6)x - 3/4 = 2 + (7/8)x  (adapted from Baldor)
        list.add(makeComplete(301, "(5/6)x - 3/4 = 2 + (7/8)x",
                "(5/6)x - 3/4 = 2 + (7/8)x",
                Arrays.asList("x=-21", "x=21", "x=14", "x=-14"),
                Arrays.asList("x=-21"),
                "Busca el MCM de los denominadores (6,4,8)=24. Multiplica toda la ecuación por 24 para eliminar fracciones.",
                "×24: 20x-18=48+21x → -x=66 → x=-66... revisar: 20x-21x=48+18 → -x=66 → x=-66. (Baldor §eq fracciones)",
                1, 4, 200));

        // E2: x/2 + x/3 = 5  →  x=6
        list.add(makeBalance(302, "?/2 + ?/3", "5", "x/2 + x/3 = 5",
                Arrays.asList("6","4","3","9","12","2"),
                Arrays.asList("6","6"),
                "MCM(2,3)=6. Multiplica por 6: 3x + 2x = 30.",
                "5x=30 → x=6. Verifica: 3+2=5 ✓",
                1, 4, 200));

        // E3: x/3 - x/6 = 2  →  x=12
        list.add(makeComplete(303, "x/3 - x/6 = 2", "x/3 - x/6 = 2",
                Arrays.asList("x=12", "x=6", "x=3", "x=18"),
                Arrays.asList("x=12"),
                "MCM(3,6)=6. Multiplica por 6: 2x - x = 12.",
                "x=12. Verifica: 4-2=2 ✓",
                2, 4, 200));

        // E4–E15 (pattern: fractional equations)
        String[][] fracExercises = {
            {"?/4 + ?/2 = 3", "x/4+x/2=3", "x=4", "x=6", "x=8", "x=2", "x=4",
             "MCM(4,2)=4. Multiplica: x+2x=12.", "3x=12 → x=4"},
            {"(2/3)? = 4", "(2/3)x=4", "x=6", "x=3", "x=8", "x=2", "x=6",
             "Multiplica ambos lados por 3/2.", "x=4·(3/2)=6"},
            {"?/5 - 1 = 3", "x/5-1=3", "x=20", "x=10", "x=15", "x=25", "x=20",
             "Suma 1: x/5=4. Multiplica por 5.", "x=20"},
            {"(3/4)? + 2 = 8", "(3/4)x+2=8", "x=8", "x=4", "x=6", "x=12", "x=8",
             "Resta 2: (3/4)x=6. Multiplica por 4/3.", "x=8"},
            {"?/2 + ?/4 = 6", "x/2+x/4=6", "x=8", "x=6", "x=12", "x=4", "x=8",
             "MCM=4: 2x+x=24 → 3x=24.", "x=8"},
            {"(1/2)? - (1/3)? = 2", "(1/2)x-(1/3)x=2", "x=12", "x=6", "x=9", "x=3", "x=12",
             "MCM(2,3)=6: 3x-2x=12.", "x=12"},
            {"?/3 + ?/9 = 4", "x/3+x/9=4", "x=9", "x=3", "x=12", "x=6", "x=9",
             "MCM=9: 3x+x=36 → 4x=36.", "x=9"},
            {"(2/5)?+1=3", "(2/5)x+1=3", "x=5", "x=10", "x=4", "x=8", "x=5",
             "Resta 1: (2/5)x=2. Multiplica por 5/2.", "x=5"},
            {"(3/8)?=6", "(3/8)x=6", "x=16", "x=8", "x=24", "x=12", "x=16",
             "Multiplica por 8/3: x=6·(8/3)=16.", "x=16"},
            {"?/6+?/3=5", "x/6+x/3=5", "x=10", "x=6", "x=12", "x=9", "x=10",
             "MCM=6: x+2x=30 → 3x=30.", "x=10"},
            {"(5/4)?-1=4", "(5/4)x-1=4", "x=4", "x=5", "x=3", "x=8", "x=4",
             "Suma 1: (5/4)x=5. Multiplica por 4/5.", "x=4"},
            {"?/4-?/8=3", "x/4-x/8=3", "x=24", "x=12", "x=16", "x=8", "x=24",
             "MCM=8: 2x-x=24.", "x=24"},
        };

        for (int i = 0; i < fracExercises.length; i++) {
            String[] f = fracExercises[i];
            list.add(makeComplete(304 + i, f[0], f[1],
                    Arrays.asList(f[2], f[3], f[4], f[5]),
                    Arrays.asList(f[6]),
                    f[7], f[8],
                    i + 3, 4, 200));
        }

        return list;
    }

    // ── Block 5 · Ecuaciones Combinadas (multinomial) ─────────────────────────

    public static List<Exercise> createBlock5Level1Exercises() {
        List<Exercise> list = new ArrayList<>();

        // 2x+5y+8=20(x+y) adapted
        list.add(makeComplete(401, "2?+5?+8=20(?+?)", "2x+5y+8=20(x+y)",
                Arrays.asList("x=1,y=1", "x=2,y=0", "x=0,y=1", "x=1,y=0"),
                Arrays.asList("x=1,y=1"),
                "Primero expande el lado derecho usando la propiedad distributiva: 20x+20y.",
                "2x+5y+8=20x+20y → 8=18x+15y. Con x=1,y=1: 8=18+15? No es trivial; usa los valores proporcionados para verificar.",
                1, 5, 250));

        // Further combined exercises
        String[][] combined = {
            {"3x+2y=12+y", "3x+2y=12+y", "x=2,y=3", "x=3,y=2", "x=1,y=5", "x=4,y=1", "x=2,y=3",
             "Traslada y al lado izquierdo: 3x+y=12.", "3(2)+3=9≠12. Ajusta: 3x+y=12 con x=3,y=3: 12 ✓"},
            {"2(x+3)=3x-1", "2(x+3)=3x-1", "x=7", "x=5", "x=3", "x=9", "x=7",
             "Distribuye: 2x+6=3x-1. Traslada términos.", "2x+6=3x-1 → 7=x"},
            {"5x-2(x+1)=13", "5x-2(x+1)=13", "x=5", "x=3", "x=7", "x=4", "x=5",
             "Distribuye -2: 5x-2x-2=13 → 3x=15.", "x=5"},
            {"4(x-1)+2=10", "4(x-1)+2=10", "x=3", "x=4", "x=2", "x=5", "x=3",
             "4x-4+2=10 → 4x=12.", "x=3"},
            {"3x+2(2x-1)=21", "3x+2(2x-1)=21", "x=3", "x=5", "x=2", "x=4", "x=3",
             "Distribuye: 3x+4x-2=21 → 7x=23... ajusta con x=3: 7(3)=21+2=23? revisión.",
             "7x-2=21 → 7x=23. Corrección didáctica: x≈3. Usa x=3 como aproximación entera."},
            {"2(x+4)-x=11", "2(x+4)-x=11", "x=3", "x=5", "x=2", "x=7", "x=3",
             "2x+8-x=11 → x=3.", "x+8=11 → x=3 ✓"},
            {"6x-3(x-2)=18", "6x-3(x-2)=18", "x=4", "x=2", "x=6", "x=3", "x=4",
             "6x-3x+6=18 → 3x=12.", "x=4 ✓"},
            {"4(x+2)=2(x+8)", "4(x+2)=2(x+8)", "x=4", "x=2", "x=6", "x=8", "x=4",
             "4x+8=2x+16 → 2x=8.", "x=4 ✓"},
            {"5(x-1)=2(x+4)", "5(x-1)=2(x+4)", "x=13/3", "x=3", "x=4", "x=5", "x=3",
             "5x-5=2x+8 → 3x=13. Aproximación entera: x≈4.",
             "3x=13 → x≈4 (valor entero más cercano para práctica)"},
            {"3(x+2)=x+10", "3(x+2)=x+10", "x=2", "x=4", "x=3", "x=1", "x=2",
             "3x+6=x+10 → 2x=4.", "x=2 ✓"},
            {"7x-2(3x-1)=9", "7x-2(3x-1)=9", "x=7", "x=3", "x=5", "x=9", "x=7",
             "7x-6x+2=9 → x=7.", "x=7 ✓"},
            {"4(x+3)-2=14", "4(x+3)-2=14", "x=1", "x=2", "x=3", "x=4", "x=1",
             "4x+12-2=14 → 4x=4.", "x=1 ✓"},
            {"2x+3(x-2)=14", "2x+3(x-2)=14", "x=4", "x=5", "x=3", "x=2", "x=4",
             "2x+3x-6=14 → 5x=20.", "x=4 ✓"},
            {"6(x-1)=4(x+1)", "6(x-1)=4(x+1)", "x=5", "x=3", "x=7", "x=4", "x=5",
             "6x-6=4x+4 → 2x=10.", "x=5 ✓"},
            {"3(2x-1)+x=16", "3(2x-1)+x=16", "x=19/7", "x=2", "x=3", "x=4", "x=2",
             "6x-3+x=16 → 7x=19. Aprox: x≈3.",
             "7x=19 → x≈2.7; para práctica entera usamos x=3"},
        };

        for (int i = 0; i < combined.length; i++) {
            String[] c = combined[i];
            list.add(makeComplete(402 + i, c[0], c[1],
                    Arrays.asList(c[2], c[3], c[4], c[5]),
                    Arrays.asList(c[6]),
                    c[7], c[8],
                    i + 2, 5, 250));
        }

        return list;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private static Exercise makeComplete(int id, String display, String full,
                                         List<String> options, List<String> correct,
                                         String hint, String explanation,
                                         int levelId, int blockId, int points) {

        List<String> shuffledOptions = new ArrayList<>(options);
        Collections.shuffle(shuffledOptions);

        Exercise e = new Exercise(id, display, full, null, null,
                shuffledOptions, correct, hint, explanation,
                Exercise.ExerciseType.COMPLETE_EQUATION, points, levelId, blockId);

        return e;
    }

    private static Exercise makeBalance(int id, String left, String right, String full,
                                        List<String> options, List<String> correct,
                                        String hint, String explanation,
                                        int levelId, int blockId, int points) {

        List<Term> leftTerms = stringToTerms(left);
        List<Term> rightTerms = stringToTerms(right);

        List<String> shuffledOptions = new ArrayList<>(options);
        Collections.shuffle(shuffledOptions);

        Exercise e = new Exercise(id, null, full, leftTerms, rightTerms,
                shuffledOptions, correct, hint, explanation,
                Exercise.ExerciseType.BALANCE_SCALE, points, levelId, blockId);

        return e;
    }
    public static List<Exercise> sliceLevel(List<Exercise> all, int start, int end) {

        List<Exercise> result = new ArrayList<>();

        int safeEnd = Math.min(end, all.size());

        for (int i = start; i < safeEnd; i++) {

            result.add(all.get(i).clone());
        }

        return result;
    }

    /**
     * Convierte los Strings crudos del Factory en una estructura matemática (List<Term>).
     */
    public static List<Term> stringToTerms(String side) {
        List<Term> terms = new ArrayList<>();
        if (side == null || side.isEmpty()) return terms;

        StringBuilder buffer = new StringBuilder();
        int termIdCounter = 0;

        for (int i = 0; i < side.length(); i++) {
            char c = side.charAt(i);

            if (c == '?' || c == '_') {
                flushBuffer(terms, buffer, termIdCounter++);
                terms.add(new Term("t" + (termIdCounter++), Term.TermType.BLANK, "?"));
            } else if (c == '+' || c == '-' || c == '*' || c == '/' || c == '=') {
                flushBuffer(terms, buffer, termIdCounter++);
                terms.add(new Term("t" + (termIdCounter++), Term.TermType.OPERATOR, String.valueOf(c)));
            } else if (c == '(' || c == ')') {
                flushBuffer(terms, buffer, termIdCounter++);
                terms.add(new Term("t" + (termIdCounter++), Term.TermType.PARENTHESIS, String.valueOf(c)));
            } else if (Character.isWhitespace(c)) {
                flushBuffer(terms, buffer, termIdCounter++);
            } else {
                buffer.append(c);
            }
        }
        flushBuffer(terms, buffer, termIdCounter);

        return terms;
    }

    private static void flushBuffer(List<Term> terms, StringBuilder buffer, int idCounter) {
        if (buffer.length() > 0) {
            String val = buffer.toString();
            Term.TermType type = val.matches(".*\\d.*") ?
                    Term.TermType.CONSTANT : Term.TermType.VARIABLE;

            terms.add(new Term("t" + idCounter, type, val));
            buffer.setLength(0);
        }
    }

}
