package com.androide.algebrago.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.androide.algebrago.models.Achievement;
import com.androide.algebrago.models.Block;
import com.androide.algebrago.patterns.facade.AppFacade;

import java.util.List;
import com.androide.algebrago.models.Achievement;
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
        blocks.setValue(facade.getAllBlocks());
        achievements.setValue(facade.getAchievements());
    }

    /**
     * Registra un observador del ScoreManager para mantener el score en tiempo real.
     * Se desregistra automáticamente cuando el ViewModel se destruye (onCleared).
     */
    public void startObservingScore() {
        facade.getScoreManager().addObserver(new com.androide.algebrago.patterns.observer.ProgressObserver() {
            @Override public void onScoreChanged(int newScore)        { score.postValue(newScore); }
            @Override public void onStreakChanged(int newStreak)      {}
            @Override public void onAchievementUnlocked(Achievement achievement)  { refresh(); }
            @Override public void onLevelCompleted(int l, int b)       { refresh(); }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        // El ScoreManager es Singleton; limpiar observers evita leaks
        // En una refactorización futura se puede guardar la referencia y removeObserver()
    }

    // ── LiveData getters ──────────────────────────────────────────────────────

    public LiveData<Integer> getScore()                  { return score; }
    public LiveData<List<Block>> getBlocks()             { return blocks; }
    public LiveData<List<Achievement>> getAchievements() { return achievements; }

    // ── Acciones ──────────────────────────────────────────────────────────────

    public AppFacade getFacade() { return facade; }
}
