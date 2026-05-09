package com.androide.algebrago.centro.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.androide.algebrago.centro.database.converter.StringListConverter;
import com.androide.algebrago.data.local.dao.EquationDao;
import com.androide.algebrago.data.local.dao.HistoryDao;
import com.androide.algebrago.data.local.entity.EquationEntity;
import com.androide.algebrago.data.local.entity.EquationHistoryEntity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Base de datos Room (SQLite) de AlgebraGo.
 *
 * Gestión de versiones:
 *   v1 → tabla 'equations' con todos los campos definidos en EquationEntity.
 *
 * Patrón aplicado: Singleton — una única instancia por proceso de la app,
 * garantizado por el bloque sincronizado en getInstance().
 *
 * TypeConverters registrados: StringListConverter (List<String> ↔ JSON).
 *
 * ExecutorService: pool de 4 hilos para operaciones de escritura en background,
 * evitando bloquear el hilo principal (requerimiento de Room).
 */
@Database(
    entities = {EquationEntity.class, EquationHistoryEntity.class},
    version = 2,
    exportSchema = false
)
@TypeConverters({StringListConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final String DB_NAME = "algebrago.db";

    /** Pool de hilos para operaciones de escritura off-main-thread. */
    public static final ExecutorService IO_EXECUTOR =
            Executors.newFixedThreadPool(4);

    private static volatile AppDatabase INSTANCE;

    // ── DAO públicos ──────────────────────────────────────────────────────────

    public abstract EquationDao equationDao();
    public abstract HistoryDao historyDao();

    // ── Singleton ─────────────────────────────────────────────────────────────

    /**
     * Retorna la instancia única de la base de datos.
     * El callback onCreate() se ejecuta solo la primera vez (BD nueva),
     * lo que permite hacer el seed inicial de ejercicios sin bloquear la UI.
     */
    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    DB_NAME
                            )
                            .addCallback(new RoomDatabase.Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    // Seed inicial en background: carga ecuaciones de Baldor
                                    IO_EXECUTOR.execute(() -> {
                                        AppDatabase database = getInstance(context);
                                        DatabaseSeeder.seedIfEmpty(database);
                                    });
                                }
                            })
                            .fallbackToDestructiveMigration() // En dev: re-crea BD si cambia versión
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
