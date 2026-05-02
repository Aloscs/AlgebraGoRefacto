package com.androide.algebrago.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.androide.algebrago.database.entity.EquationEntity;

import java.util.List;

/**
 * DAO (Data Access Object) para la tabla 'equations'.
 *
 * Define el contrato de acceso a datos: Room genera la implementación
 * en tiempo de compilación a partir de las anotaciones SQL.
 *
 * Principios aplicados:
 *  - Los métodos que devuelven LiveData se ejecutan en un hilo de BD
 *    y notifican automáticamente a los observadores cuando los datos cambian.
 *  - Los métodos de escritura (insert/update/delete) deben llamarse
 *    fuera del hilo principal (se delega al EquationRepository con ExecutorService).
 *  - Separación de responsabilidades: el DAO no conoce nada del ViewModel ni la UI.
 */
@Dao
public interface EquationDao {

    // ── Inserción ─────────────────────────────────────────────────────────────

    /**
     * Inserta una ecuación. Si ya existe el mismo ID, la reemplaza.
     * Retorna el rowId asignado por SQLite.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(EquationEntity equation);

    /**
     * Inserta una lista de ecuaciones en una sola transacción.
     * Eficiente para la carga inicial (seed) de ejercicios desde ExerciseFactory.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<EquationEntity> equations);

    // ── Consultas de lectura ──────────────────────────────────────────────────

    /**
     * Retorna todas las ecuaciones activas ordenadas por bloque, nivel y dificultad.
     * LiveData garantiza que la UI se actualice automáticamente ante cambios.
     */
    @Query("SELECT * FROM equations WHERE is_active = 1 ORDER BY block_id, level_id, difficulty ASC")
    LiveData<List<EquationEntity>> getAllActive();

    /**
     * Retorna ecuaciones de un nivel específico, activas y ordenadas por dificultad.
     * Usado por ExerciseRepository al cargar ejercicios para una sesión.
     */
    @Query("SELECT * FROM equations WHERE block_id = :blockId AND level_id = :levelId " +
           "AND is_active = 1 ORDER BY difficulty ASC")
    LiveData<List<EquationEntity>> getByLevel(int blockId, int levelId);

    /**
     * Versión síncrona de getByLevel para carga en background thread.
     * El ViewModel la usa a través del Repository con ExecutorService.
     */
    @Query("SELECT * FROM equations WHERE block_id = :blockId AND level_id = :levelId " +
           "AND is_active = 1 ORDER BY difficulty ASC")
    List<EquationEntity> getByLevelSync(int blockId, int levelId);

    /**
     * Retorna ecuaciones filtradas por tipo de ejercicio.
     * Permite mostrar solo ejercicios de balanza o solo de completar.
     */
    @Query("SELECT * FROM equations WHERE exercise_type = :type AND is_active = 1 " +
           "ORDER BY block_id, level_id ASC")
    LiveData<List<EquationEntity>> getByType(String type);

    /**
     * Retorna ecuaciones de un bloque completo (todos sus niveles).
     */
    @Query("SELECT * FROM equations WHERE block_id = :blockId AND is_active = 1 " +
           "ORDER BY level_id, difficulty ASC")
    LiveData<List<EquationEntity>> getByBlock(int blockId);

    /**
     * Busca ecuaciones cuya representación completa contenga el texto indicado.
     * Útil para una futura pantalla de búsqueda.
     */
    @Query("SELECT * FROM equations WHERE equation_full LIKE '%' || :query || '%' " +
           "AND is_active = 1")
    LiveData<List<EquationEntity>> search(String query);

    /**
     * Cuenta cuántas ecuaciones existen en la BD (activas e inactivas).
     * Usado para determinar si ya se realizó el seed inicial.
     */
    @Query("SELECT COUNT(*) FROM equations")
    int count();

    /**
     * Retorna una sola ecuación por su ID primario.
     */
    @Query("SELECT * FROM equations WHERE id = :id LIMIT 1")
    EquationEntity getById(int id);

    // ── Actualización ─────────────────────────────────────────────────────────

    /**
     * Actualiza todos los campos de una ecuación existente.
     * Room hace match por clave primaria (id).
     */
    @Update
    void update(EquationEntity equation);

    /**
     * Desactiva una ecuación lógicamente (soft delete).
     * Preferible a borrarla para conservar historial.
     */
    @Query("UPDATE equations SET is_active = 0 WHERE id = :id")
    void softDelete(int id);

    // ── Eliminación ───────────────────────────────────────────────────────────

    /**
     * Elimina físicamente una ecuación de la BD.
     */
    @Delete
    void delete(EquationEntity equation);

    /**
     * Elimina todas las ecuaciones de la tabla (útil para reset o tests).
     */
    @Query("DELETE FROM equations")
    void deleteAll();
}
