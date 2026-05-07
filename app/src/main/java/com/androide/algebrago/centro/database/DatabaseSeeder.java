package com.androide.algebrago.centro.database;

import com.androide.algebrago.centro.database.converter.StringListConverter;
import com.androide.algebrago.data.local.entity.EquationEntity;
import com.androide.algebrago.domain.models.Exercise;
import com.androide.algebrago.data.memory.factory.ExerciseFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Encargado de poblar (seed) la base de datos SQLite con los ejercicios
 * generados por ExerciseFactory la primera vez que se instala la app.
 *
 * Principio de separación de responsabilidades:
 *  - ExerciseFactory → crea objetos Exercise del dominio (sin BD).
 *  - DatabaseSeeder  → convierte esos objetos a EquationEntity y los persiste.
 *  - AppDatabase     → invoca al Seeder en el callback onCreate().
 *
 * Este seeder se ejecuta una sola vez en background (IO_EXECUTOR),
 * nunca en el hilo principal.
 */
public final class DatabaseSeeder {

    private DatabaseSeeder() { /* Utilidad estática, no instanciar */ }

    /**
     * Inserta todos los ejercicios de Baldor si la tabla está vacía.
     * Se llama desde AppDatabase.onCreate().
     */
    public static void seedIfEmpty(AppDatabase db) {
        if (db.equationDao().count() > 0) return; // ya tiene datos

        List<EquationEntity> entities = new ArrayList<>();

        // Bloque 1 — Ecuaciones Simples
        convertAndAdd(entities, ExerciseFactory.createBlock1Level1Exercises());

        // Bloque 2 — Dos Variables
        convertAndAdd(entities, ExerciseFactory.createBlock2Level1Exercises());

        // Bloque 3 — Paréntesis
        convertAndAdd(entities, ExerciseFactory.createBlock3Level1Exercises());

        // Bloque 4 — Fracciones
        convertAndAdd(entities, ExerciseFactory.createBlock4Level1Exercises());

        // Bloque 5 — Combinadas
        convertAndAdd(entities, ExerciseFactory.createBlock5Level1Exercises());

        db.equationDao().insertAll(entities);
    }

    /**
     * Convierte una lista de Exercise del dominio a EquationEntity para Room.
     */
    private static void convertAndAdd(List<EquationEntity> target,
                                      List<Exercise> exercises) {
        for (Exercise ex : exercises) {
            EquationEntity entity = new EquationEntity(
                    nullSafe(ex.getEquationFull()),
                    ex.getEquationDisplay(),
                    termsToString(ex.getLeftSideTerms()),   // 🟢 CORRECCIÓN
                    termsToString(ex.getRightSideTerms()),  // 🟢 CORRECCIÓN
                    StringListConverter.fromList(ex.getOptions()),
                    StringListConverter.fromList(ex.getCorrectValues()),
                    ex.getHint(),
                    ex.getExplanation(),
                    ex.getType() != null ? ex.getType().name() : "COMPLETE_EQUATION",
                    ex.getPointValue(),
                    ex.getLevelId(),
                    ex.getBlockId(),
                    deriveDifficulty(ex.getBlockId(), ex.getLevelId())
            );
            target.add(entity);
        }
    }

    /**
     * Asigna dificultad basándose en el bloque y nivel:
     *   Bloque 1-2, Nivel 1 → fácil (1)
     *   Bloque 3-4, Nivel 2 → medio (2)
     *   Bloque 5, Nivel 3   → difícil (3)
     */
    private static int deriveDifficulty(int blockId, int levelId) {
        if (blockId <= 2) return 1;
        if (blockId <= 4) return 2;
        return 3;
    }

    private static String nullSafe(String s) {
        return s != null ? s : "";
    }

    /**
     * Aplasta la estructura matemática (List<Term>) a un String ("?+5")
     * para poder guardarla en SQLite.
     */
    private static String termsToString(List<com.androide.algebrago.domain.models.Term> terms) {
        if (terms == null || terms.isEmpty()) return "";

        StringBuilder sb = new StringBuilder();
        for (com.androide.algebrago.domain.models.Term t : terms) {
            if (t.getType() == com.androide.algebrago.domain.models.Term.TermType.BLANK) {
                sb.append("?");
            } else {
                sb.append(t.getValue());
            }
        }
        return sb.toString();
    }
}
