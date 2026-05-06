package com.androide.algebrago.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.androide.algebrago.database.entity.EquationHistoryEntity;

import java.util.List;

/**
 * DAO para la gestión del historial de sesiones.
 * Incluye consultas optimizadas con ordenamiento por marca de tiempo.
 */
@Dao
public interface HistoryDao {

    // Inserción optimizada
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(EquationHistoryEntity history);

    // Consulta de todos los registros históricos, del más reciente al más antiguo
    @Query("SELECT * FROM equation_history ORDER BY timestamp DESC")
    LiveData<List<EquationHistoryEntity>> getAllHistory();

    // Consulta optimizada para buscar el historial de un nivel en específico
    @Query("SELECT * FROM equation_history WHERE blockId = :blockId AND levelId = :levelId ORDER BY timestamp DESC")
    List<EquationHistoryEntity> getHistoryForLevelSync(int blockId, int levelId);

    // Método útil para limpiar la base de datos o reiniciar el progreso
    @Query("DELETE FROM equation_history")
    void deleteAllHistory();
}