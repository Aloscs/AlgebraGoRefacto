package com.androide.algebrago.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.androide.algebrago.models.Exercise;
import com.androide.algebrago.patterns.facade.AppFacade;
import com.androide.algebrago.patterns.state.SessionStateManager;
import com.androide.algebrago.repository.EquationRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel para ExerciseActivity.
 *
 * MVVM — responsabilidades del ViewModel:
 *  - Exponer datos como LiveData para que la View los observe.
 *  - Mantener el estado de la sesión (índice actual, resultados, intentos).
 *  - Delegar lógica de negocio al Repository y al AppFacade.
 *  - NO tener referencias a Context, Activity ni Views.
 *  - Sobrevivir a rotaciones de pantalla (cycle de vida de ViewModel).
 *
 * Datos que expone:
 *  - exercises: lista de ejercicios del nivel cargada desde el Repository.
 *  - currentExercise: ejercicio actualmente en pantalla.
 *  - answerResult: resultado de la última verificación (CORRECT / WRONG / NONE).
 *  - sessionScore: puntos acumulados en esta sesión.
 *  - sessionComplete: señal de que todos los ejercicios fueron respondidos.
 *
 * Flujo:
 *   Activity → llama loadExercises(blockId, levelId)
 *   ViewModel → llama Repository.loadExercisesForLevel()
 *   Repository → consulta Room en background
 *   ViewModel → postea lista via exercises LiveData
 *   Activity → observa y muestra el primer ejercicio
 */
public class ExerciseViewModel extends AndroidViewModel {

    // ── Enumeración de resultado de respuesta ─────────────────────────────────
    public enum AnswerResult { NONE, CORRECT, WRONG }

    // ── LiveData expuesto a la View ───────────────────────────────────────────

    private final MutableLiveData<List<Exercise>> exercises = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Exercise> currentExercise = new MutableLiveData<>();
    private final MutableLiveData<AnswerResult> answerResult = new MutableLiveData<>(AnswerResult.NONE);
    private final MutableLiveData<Integer> sessionScore = new MutableLiveData<>(0);
    private final MutableLiveData<Boolean> sessionComplete = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    // ── Estado interno de la sesión ───────────────────────────────────────────

    private int currentIndex = 0;
    private boolean firstAttempt = true;
    private final List<String[]> sessionResults = new ArrayList<>();

    // ── Dependencias ──────────────────────────────────────────────────────────

    private final EquationRepository repository;
    private final AppFacade facade;
    private final SessionStateManager stateManager;

    private int blockId;
    private int levelId;

    public ExerciseViewModel(@NonNull Application application) {
        super(application);
        repository   = EquationRepository.getInstance(application);
        facade       = AppFacade.getInstance(application);
        stateManager = new SessionStateManager();
    }

    // ── Inicialización ────────────────────────────────────────────────────────

    /**
     * Carga los ejercicios del nivel desde el Repository (Room → dominio).
     * Si la BD está vacía para ese nivel, cae al fallback del AppFacade (memoria).
     */
    public void loadExercises(int blockId, int levelId) {
        this.blockId = blockId;
        this.levelId = levelId;
        isLoading.setValue(true);
        stateManager.transitionToExercising();

        repository.loadExercisesForLevel(blockId, levelId, new MutableLiveData<List<Exercise>>() {
            @Override
            public void postValue(List<Exercise> value) {
                if (value == null || value.isEmpty()) {
                    // Fallback: obtener ejercicios del AppFacade (en memoria)
                    value = facade.getExercisesForLevel(blockId, levelId);
                }
                exercises.postValue(value);
                isLoading.postValue(false);

                if (value != null && !value.isEmpty()) {
                    currentIndex = 0;
                    firstAttempt = true;
                    currentExercise.postValue(value.get(0));
                }
            }
        });
    }

    // ── Lógica de respuesta ───────────────────────────────────────────────────

    /**
     * Procesa una respuesta del usuario.
     * @param isCorrect resultado de la verificación realizada en la Activity.
     */
    public void submitAnswer(boolean isCorrect) {
        if (!stateManager.canSubmitAnswer()) return;

        List<Exercise> list = exercises.getValue();
        if (list == null || currentIndex >= list.size()) return;

        Exercise ex = list.get(currentIndex);

        if (isCorrect) {
            answerResult.setValue(AnswerResult.CORRECT);
            facade.submitCorrectAnswer(ex.getPointValue(), firstAttempt);
            sessionScore.setValue(facade.getTotalScore());

            sessionResults.add(new String[]{
                    ex.getEquationFull(),
                    String.join(", ", ex.getCorrectValues()),
                    "✓"
            });
        } else {
            answerResult.setValue(AnswerResult.WRONG);
            facade.submitWrongAnswer();
            firstAttempt = false;

            // Registra el error solo si es el primer intento para no duplicar
            if (firstAttempt) {
                sessionResults.add(new String[]{
                        ex.getEquationFull(),
                        String.join(", ", ex.getCorrectValues()),
                        "✗"
                });
            }
        }
    }

    /**
     * Avanza al siguiente ejercicio o marca la sesión como completa.
     */
    public void nextExercise() {
        List<Exercise> list = exercises.getValue();
        if (list == null) return;

        currentIndex++;
        firstAttempt = true;
        answerResult.setValue(AnswerResult.NONE);

        if (currentIndex >= list.size()) {
            stateManager.transitionToCompleted();
            facade.markLevelComplete(blockId, levelId);
            sessionComplete.setValue(true);
        } else {
            currentExercise.setValue(list.get(currentIndex));
        }
    }

    // ── Getters de LiveData ───────────────────────────────────────────────────

    public LiveData<List<Exercise>> getExercises()       { return exercises; }
    public LiveData<Exercise> getCurrentExercise()        { return currentExercise; }
    public LiveData<AnswerResult> getAnswerResult()       { return answerResult; }
    public LiveData<Integer> getSessionScore()            { return sessionScore; }
    public LiveData<Boolean> getSessionComplete()         { return sessionComplete; }
    public LiveData<Boolean> getIsLoading()               { return isLoading; }

    // ── Getters de estado para FeedbackActivity ───────────────────────────────

    public List<String[]> getSessionResults()            { return sessionResults; }
    public List<Exercise> getExerciseList()              { return exercises.getValue(); }
    public int getTotalScore()                           { return facade.getTotalScore(); }
    public boolean canShowHint()                         { return stateManager.canShowHint(); }

    public int getExerciseCount() {
        List<Exercise> list = exercises.getValue();
        return list != null ? list.size() : 0;
    }

    public int getCurrentIndex() { return currentIndex; }
}
