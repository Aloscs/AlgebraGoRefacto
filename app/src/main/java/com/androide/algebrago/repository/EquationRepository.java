package com.androide.algebrago.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.androide.algebrago.database.AppDatabase;
import com.androide.algebrago.database.converter.StringListConverter;
import com.androide.algebrago.database.dao.EquationDao;
import com.androide.algebrago.database.entity.EquationEntity;
import com.androide.algebrago.models.Exercise;

import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio de ecuaciones.
 *
 * Responsabilidad: actuar como única fuente de verdad (Single Source of Truth)
 * para los datos de ejercicios. Oculta al ViewModel si los datos vienen
 * de Room (SQLite) o de memoria (ExerciseFactory en memoria).
 *
 * En MVVM:
 *   ViewModel → llama al Repository
 *   Repository → decide de dónde obtener los datos (Room / memoria)
 *   ViewModel expone LiveData → Activity observa y actualiza UI
 *
 * Buenas prácticas aplicadas:
 *  - Todas las operaciones de escritura se ejecutan en IO_EXECUTOR (background).
 *  - Singleton: una instancia por contexto de aplicación.
 *  - No tiene referencia directa a Context después del constructor.
 */
public class EquationRepository {

    private static EquationRepository instance;

    private final EquationDao equationDao;

    private EquationRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context.getApplicationContext());
        equationDao = db.equationDao();
    }

    public static synchronized EquationRepository getInstance(Context context) {
        if (instance == null) instance = new EquationRepository(context);
        return instance;
    }

    // ── Consultas LiveData (para observar desde ViewModel) ────────────────────

    /**
     * Retorna todas las ecuaciones activas como LiveData.
     * La UI se actualiza automáticamente cuando cambian los datos.
     */
    public LiveData<List<EquationEntity>> getAllActive() {
        return equationDao.getAllActive();
    }

    /**
     * Retorna ecuaciones de un nivel específico como LiveData.
     */
    public LiveData<List<EquationEntity>> getByLevel(int blockId, int levelId) {
        return equationDao.getByLevel(blockId, levelId);
    }

    /**
     * Retorna ecuaciones de un bloque como LiveData.
     */
    public LiveData<List<EquationEntity>> getByBlock(int blockId) {
        return equationDao.getByBlock(blockId);
    }

    /**
     * Busca ecuaciones que contengan el texto indicado.
     */
    public LiveData<List<EquationEntity>> search(String query) {
        return equationDao.search(query);
    }

    // ── Carga para sesión de ejercicios (síncrona en background) ─────────────

    /**
     * Obtiene ejercicios de un nivel como objetos Exercise del dominio.
     * Convierte EquationEntity → Exercise para que ExerciseActivity los use
     * exactamente igual que antes (sin cambios en su lógica de ejercicios).
     *
     * Debe llamarse desde un hilo background (IO_EXECUTOR).
     */
    public List<Exercise> getExercisesForLevelSync(int blockId, int levelId) {
        List<EquationEntity> entities = equationDao.getByLevelSync(blockId, levelId);
        return entitiesToExercises(entities);
    }

    /**
     * Carga ejercicios para una sesión y los expone vía MutableLiveData.
     * El ViewModel llama a este método; la Activity observa el LiveData resultante.
     */
    public void loadExercisesForLevel(int blockId, int levelId,
                                       MutableLiveData<List<Exercise>> target) {
        AppDatabase.IO_EXECUTOR.execute(() -> {
            List<Exercise> exercises = getExercisesForLevelSync(blockId, levelId);
            target.postValue(exercises);
        });
    }

    // ── Operaciones de escritura (background) ─────────────────────────────────

    /**
     * Inserta una nueva ecuación en la BD.
     */
    public void insert(EquationEntity entity) {
        AppDatabase.IO_EXECUTOR.execute(() -> equationDao.insert(entity));
    }

    /**
     * Actualiza una ecuación existente.
     */
    public void update(EquationEntity entity) {
        AppDatabase.IO_EXECUTOR.execute(() -> equationDao.update(entity));
    }

    /**
     * Desactiva lógicamente una ecuación (soft delete).
     */
    public void softDelete(int equationId) {
        AppDatabase.IO_EXECUTOR.execute(() -> equationDao.softDelete(equationId));
    }

    /**
     * Elimina físicamente una ecuación.
     */
    public void delete(EquationEntity entity) {
        AppDatabase.IO_EXECUTOR.execute(() -> equationDao.delete(entity));
    }

    // ── Conversión EquationEntity → Exercise ──────────────────────────────────

    /**
     * Convierte la lista de entidades BD a objetos Exercise del dominio.
     * Desacopla la capa de BD del modelo de dominio.
     */
    private List<Exercise> entitiesToExercises(List<EquationEntity> entities) {
        List<Exercise> result = new ArrayList<>();
        for (EquationEntity e : entities) {
            Exercise ex = new Exercise(
                    e.id,
                    e.equationDisplay,
                    e.equationFull,
                    e.leftSide,
                    e.rightSide,
                    StringListConverter.toList(e.optionsJson),
                    StringListConverter.toList(e.correctValuesJson),
                    e.hint,
                    e.explanation,
                    parseType(e.exerciseType),
                    e.pointValue,
                    e.levelId,
                    e.blockId
            );
            result.add(ex);
        }
        return result;
    }

    private Exercise.ExerciseType parseType(String type) {
        try {
            return Exercise.ExerciseType.valueOf(type);
        } catch (Exception e) {
            return Exercise.ExerciseType.COMPLETE_EQUATION;
        }
    }
}
