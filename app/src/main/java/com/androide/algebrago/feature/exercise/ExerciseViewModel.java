package com.androide.algebrago.feature.exercise;

import static com.androide.algebrago.data.memory.factory.ExerciseFactory.stringToTerms;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.androide.algebrago.domain.models.Exercise;
import com.androide.algebrago.domain.models.Term;
import com.androide.algebrago.data.memory.facade.AppFacade;
import com.androide.algebrago.domain.state.SessionStateManager;
import com.androide.algebrago.data.repository.EquationRepository;

import java.util.ArrayList;
import java.util.List;

import com.androide.algebrago.feature.exercise.logic.ASTNode;
import com.androide.algebrago.feature.exercise.logic.EquationParser;
import java.util.HashMap;
import java.util.Map;

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
    public enum AnswerResult {NONE, CORRECT, WRONG}

    // ── LiveData expuesto a la View ───────────────────────────────────────────

    private final MutableLiveData<List<Exercise>> exercises = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Exercise> currentExercise = new MutableLiveData<>();
    private final MutableLiveData<AnswerResult> answerResult = new MutableLiveData<>(AnswerResult.NONE);
    private final MutableLiveData<Integer> sessionScore = new MutableLiveData<>(0);
    private final MutableLiveData<Boolean> sessionComplete = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> achievementNotification = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isCurrentlyBalanced = new MutableLiveData<>(false);

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
        repository = EquationRepository.getInstance(application);
        facade = AppFacade.getInstance(application);
        stateManager = new SessionStateManager();

        facade.getScoreManager().addObserver(new com.androide.algebrago.shared.observer.ProgressObserver() {
            @Override
            public void onScoreChanged(int newScore) {
            }

            @Override
            public void onStreakChanged(int newStreak) {
            }

            @Override
            public void onLevelCompleted(int l, int b) {
            }

            @Override
            public void onAchievementUnlocked(String name) {
                // 3. Emite el nombre del logro a la Vista
                achievementNotification.postValue(name);
            }
        });
    }

    //para que el activity lo observe
    public LiveData<String> getAchievementNotification() {
        return achievementNotification;
    }

    //evitar que el toast vuelva a aparecer
    public void clearAchievementNotification() {
        achievementNotification.setValue(null);
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

        //creamos la estructura y guardamos en el historial y usamos el traductor que
        //hicimos para que la lista de Terms pase a un String
        String leftSerial = termsToString(ex.getLeftSideTerms());
        String rightSerial = termsToString(ex.getRightSideTerms());
        String serializedEquation = leftSerial + "=" + rightSerial;

        com.androide.algebrago.data.local.entity.EquationHistoryEntity historyRecord =
                new com.androide.algebrago.data.local.entity.EquationHistoryEntity(
                        blockId,
                        levelId,
                        serializedEquation,
                        isCorrect,
                        System.currentTimeMillis() // Marca de tiempo (Timestamp)
                );

        repository.insertHistory(historyRecord); // Lo mandamos a guardar a SQLite


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

    public void submitAnswerString(String userAnswer) {
        List<Exercise> list = exercises.getValue();
        if (list == null || currentIndex >= list.size()) return;

        Exercise ex = list.get(currentIndex);
        boolean isCorrect = ex.getCorrectValues().contains(userAnswer);

        submitAnswer(isCorrect); // Llama a tu método original que ya tenías
    }

    public void submitAnswerList(List<String> placedTokens) {
        // Obtenemos los términos crudos que puso el usuario
        List<Term> userTerms = stringsToTermsLocal(placedTokens);

        Exercise ex = exercises.getValue().get(currentIndex);

        // 1. Armar los árboles matemáticos (Jerarquía)
        ASTNode userTree = EquationParser.parse(userTerms);
        List<Term> correctTerms = stringsToTermsLocal(ex.getCorrectValues());
        ASTNode correctTree = EquationParser.parse(correctTerms); // * Necesitas crear getCorrectTerms() igual que stringToTerms *

        boolean isCorrect = true;
        Map<String, Double> variables = new HashMap<>();

        // 2. Auditoría Matemática: Probamos con dos valores distintos para 'x'
        // Si x=2 y x=5 funcionan en ambos árboles, las ecuaciones son equivalentes.
        double[] testValues = {2.0, 5.0};

        for (double testVal : testValues) {
            variables.put("x", testVal);
            try {
                double userResult = userTree.evaluate(variables);
                double correctResult = correctTree.evaluate(variables);

                // Si los resultados difieren (con un margen de error por decimales), está mal
                if (Math.abs(userResult - correctResult) > 0.001) {
                    isCorrect = false;
                    break;
                }
            } catch (Exception e) {
                isCorrect = false; // Error matemático (ej. ecuación mal formada por el usuario)
            }
        }

        submitAnswer(isCorrect);
    }

    private List<Term> stringsToTermsLocal(List<String> tokens) {
        // Aquí puedes usar la misma lógica de stringToTerms que vimos antes, concatenando los tokens
        return stringToTerms(String.join("", tokens));
    }

    public void evaluateBalanceRealTime(List<String> placedTokens) {
        List<Exercise> list = exercises.getValue();
        if (list == null || currentIndex >= list.size()) return;

        Exercise ex = list.get(currentIndex);
        List<String> correct = ex.getCorrectValues();

        boolean isCorrect = true;
        if (placedTokens.size() != correct.size()) {
            isCorrect = false;
        } else {
            for (String c : correct) {
                if (!placedTokens.contains(c)) {
                    isCorrect = false;
                    break;
                }
            }
        }

        // Solo actualizamos el estado visual, NO llamamos a submitAnswer()
        isCurrentlyBalanced.setValue(isCorrect);
    }

    // ── Traductor de Términos a Texto (Para la UI) ──────────────────────────

    /**
     * Convierte una lista de Términos matemáticos en un String para la vista.
     * Reemplaza los espacios vacíos con "?" para que la Activity pueda contarlos.
     */
    public String termsToString(List<com.androide.algebrago.domain.models.Term> terms) {
        if (terms == null || terms.isEmpty()) return null;

        StringBuilder sb = new StringBuilder();
        for (com.androide.algebrago.domain.models.Term t : terms) {
            if (t.getType() == com.androide.algebrago.domain.models.Term.TermType.BLANK) {
                sb.append("?"); // Mantiene la compatibilidad con tu UI actual
            } else {
                sb.append(t.getValue());
            }
        }
        return sb.toString();
    }

    // ── Getters de LiveData ───────────────────────────────────────────────────

    public LiveData<List<Exercise>> getExercises()       { return exercises; }
    public LiveData<Exercise> getCurrentExercise()        { return currentExercise; }
    public LiveData<AnswerResult> getAnswerResult()       { return answerResult; }
    public LiveData<Integer> getSessionScore()            { return sessionScore; }
    public LiveData<Boolean> getSessionComplete()         { return sessionComplete; }
    public LiveData<Boolean> getIsLoading()               { return isLoading; }
    public LiveData<Boolean> getIsCurrentlyBalanced() {
        return isCurrentlyBalanced;
    }

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

    /**
     * PREVENCIÓN DE FUGAS DE MEMORIA:
     * Este método se ejecuta automáticamente cuando el usuario cierra la pantalla de ejercicios.
     * Aquí liberamos cualquier recurso, limpiamos listas y detenemos procesos.
     */
    @Override
    protected void onCleared() {
        super.onCleared();
        // Limpiar referencias a listas pesadas
        if (exercises.getValue() != null) {
            exercises.getValue().clear();
        }
        // Restablecer los LiveData para que no mantengan estados viejos si se vuelve a abrir
        currentExercise.setValue(null);
        answerResult.setValue(AnswerResult.NONE);
        sessionComplete.setValue(false);
    }
}
