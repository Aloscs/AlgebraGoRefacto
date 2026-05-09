package com.androide.algebrago.feature.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.androide.algebrago.domain.models.Achievement;
import com.androide.algebrago.domain.models.Block;
import com.androide.algebrago.domain.models.Level;
import com.androide.algebrago.data.memory.facade.AppFacade;

import java.util.List;

/**
 * ViewModel compartido para MainActivity, BlockSelectionActivity y ProgressActivity.
 *
 * Expone:
 *  - score: puntuación total actualizada cada vez que cambia.
 *  - blocks: lista de bloques temáticos con su progreso.
 *  - achievements: lista de logros del usuario.
 *
 * La Activity solo observa LiveData y llama métodos del ViewModel.
 * El ViewModel nunca sabe qué Activity lo usa.
 */
public class MainViewModel extends AndroidViewModel {

    private final MutableLiveData<Integer> score = new MutableLiveData<>(0);
    private final MutableLiveData<List<Block>> blocks = new MutableLiveData<>();
    private final MutableLiveData<List<Achievement>> achievements = new MutableLiveData<>();

    private final AppFacade facade;

    public MainViewModel(@NonNull Application application) {
        super(application);
        facade = AppFacade.getInstance(application);
        // Cargar datos iniciales
        refresh();
        facade.getScoreManager().saveDailyLogin();
    }

    /**
     * Actualiza todos los LiveData desde el AppFacade.
     * La Activity llama a esto en onResume() para mantener datos frescos.
     */

    public void refresh() {
        score.setValue(facade.getTotalScore());
// 1. Obtener la lista de bloques desde el Facade
        List<Block> loadedBlocks = facade.getAllBlocks();

        // 2. Calcular el progreso (La lógica que quitamos del modelo)
        if (loadedBlocks != null) {
            for (Block block : loadedBlocks) {
                if (block.getLevels() == null || block.getLevels().isEmpty()) {
                    block.setProgressPercent(0);
                } else {
                    int completed = 0;
                    for (int i = 0; i < block.getLevels().size(); i++) {
                        if (block.getLevels().get(i).isCompleted()) {
                            completed++;
                        }
                    }
                    int progress = (completed * 100) / block.getLevels().size();
                    block.setProgressPercent(progress);
                }
            }
        }

        blocks.setValue(facade.getAllBlocks());
        achievements.setValue(facade.getAchievements());
    }

    /**
     * Registra un observador del ScoreManager para mantener el score en tiempo real.
     * Se desregistra automáticamente cuando el ViewModel se destruye (onCleared).
     */
    public void startObservingScore() {
        facade.getScoreManager().addObserver(new com.androide.algebrago.shared.observer.ProgressObserver() {
            @Override public void onScoreChanged(int newScore)        { score.postValue(newScore); }
            @Override public void onStreakChanged(int newStreak)      {}
            @Override public void onAchievementUnlocked(String name)  { refresh(); }
            @Override public void onLevelCompleted(int l, int b)       { refresh(); }
        });
    }

    public Block getBlockById(int id) {
        List<Block> currentBlocks = blocks.getValue();
        if (currentBlocks != null) {
            for (Block b : currentBlocks) {
                if (b.getId() == id) return b;
            }
        }
        return null;
    }

    public Level getLevelById(int blockId, int levelId) {
        Block b = getBlockById(blockId);
        if (b != null && b.getLevels() != null) {
            for (Level l : b.getLevels()) {
                if (l.getId() == levelId) return l;
            }
        }
        return null;
    }


    public String buildExplanation(int blockId) {
        switch (blockId) {
            case 1: return "Una ecuación de primer grado contiene una incógnita (x). El objetivo es "
                    + "encontrar el valor que hace verdadera la igualdad.\n\n"
                    + "La clave: lo que hagas a un lado de la ecuación, hazlo también al otro.";
            case 2: return "Con dos incógnitas (x e y) hay infinitas soluciones. En este bloque "
                    + "verificarás si un par (x, y) satisface la ecuación sustituyendo directamente.";
            case 3: return "Para eliminar paréntesis aplica la propiedad distributiva.\n\n"
                    + "Ejemplo: 3(x + 2) = 3x + 6";
            case 4: return "Para resolver ecuaciones con fracciones, multiplica todos los términos "
                    + "por el MCM de los denominadores para eliminarlos.";
            case 5: return "Las ecuaciones combinadas mezclan paréntesis, fracciones y varios términos.\n"
                    + "Orden: 1.Quitar paréntesis · 2.Eliminar fracciones · "
                    + "3.Transponer · 4.Agrupar · 5.Despejar · 6.Simplificar.";
            default: return "Sigue los pasos del procedimiento para resolver la ecuación.";
        }
    }

    public String buildSteps(int blockId) {
        switch (blockId) {
            case 1: return "PASOS:\n① Identifica la operación de la incógnita.\n"
                    + "② Aplica la operación inversa a AMBOS lados.\n"
                    + "③ Simplifica y verifica.";
            case 2: return "PASOS:\n① Toma el par (x, y).\n"
                    + "② Sustituye en la ecuación.\n"
                    + "③ Verifica la igualdad.";
            case 3: return "PASOS:\n① Distribuye el factor exterior.\n"
                    + "② Agrupa términos semejantes.\n"
                    + "③ Despeja y verifica.";
            case 4: return "PASOS:\n① Identifica denominadores.\n"
                    + "② Calcula el MCM.\n"
                    + "③ Multiplica toda la ecuación por el MCM.\n"
                    + "④ Despeja y verifica.";
            case 5: return "PASOS:\n① Quita paréntesis.\n"
                    + "② Elimina fracciones.\n"
                    + "③ Transpón términos.\n"
                    + "④ Agrupa y despeja.\n"
                    + "⑤ Simplifica y verifica.";
            default: return "Sigue el procedimiento paso a paso.";
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        // El ScoreManager es Singleton; limpiar observers evita leaks
        // En una refactorización futura se puede guardar la referencia y removeObserver()
        score.setValue(0);
    }

    // ── LiveData getters ──────────────────────────────────────────────────────

    public LiveData<Integer> getScore()                  { return score; }
    public LiveData<List<Block>> getBlocks()             { return blocks; }
    public LiveData<List<Achievement>> getAchievements() { return achievements; }

    // ── Acciones ──────────────────────────────────────────────────────────────

    public AppFacade getFacade() { return facade; }
}
