package com.androide.algebrago.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.androide.algebrago.data.local.entity.EquationHistoryEntity;

import java.util.List;

/**
 * DAO para la gestión del historial de sesiones.
 * Incluye consultas optimizadas con ordenamiento por marca de tiempo.
 */
@Dao
public interface HistoryDao {

    /**
     * Inserta un registro de historial. Si ya existe uno con el mismo ID, lo reemplaza.
     *
     * @param history entidad de historial a persistir.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(EquationHistoryEntity history);

    /**
     * Retorna todos los registros históricos, del más reciente al más antiguo.
     * LiveData garantiza que la UI se actualice automáticamente ante cambios.
     *
     * @return LiveData con la lista completa de registros de historial.
     */
    @Query("SELECT * FROM equation_history ORDER BY timestamp DESC")
    LiveData<List<EquationHistoryEntity>> getAllHistory();

    /**
     * Retorna de forma síncrona el historial de un nivel específico, del más reciente
     * al más antiguo. Debe llamarse desde un hilo background (IO_EXECUTOR).
     *
     * @param blockId ID del bloque temático.
     * @param levelId ID del nivel dentro del bloque.
     * @return lista de registros de historial para el nivel indicado.
     */
    @Query("SELECT * FROM equation_history WHERE blockId = :blockId AND levelId = :levelId ORDER BY timestamp DESC")
    List<EquationHistoryEntity> getHistoryForLevelSync(int blockId, int levelId);

    /**
     * Elimina todos los registros del historial.
     * Útil para reiniciar el progreso o en pruebas.
     */
    @Query("DELETE FROM equation_history")
    void deleteAllHistory();
}