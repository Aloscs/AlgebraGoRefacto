package com.androide.algebrago.feature.feedback;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.androide.algebrago.R;

import java.util.ArrayList;
import java.util.List;

public class FeedbackViewModel extends AndroidViewModel {

    // 1. Clase interna para encapsular los datos limpios y procesados
    public static class FeedbackItem {
        public final String equation;
        public final String correctAnswer;
        public final String mark;
        public final String explanation;
        public final boolean isCorrect;

        public FeedbackItem(String equation, String correctAnswer, String mark, String explanation) {
            this.equation = equation;
            this.correctAnswer = correctAnswer;
            this.mark = mark;
            this.explanation = explanation;
            // ¡La lógica de evaluación vive ahora en el modelo/vista-modelo!
            this.isCorrect = "✓".equals(mark);
        }
    }

    // ¡La regla de negocio ahora está protegida aquí!
    private static final int MAX_RETRIES = 2;

    // 2. LiveData expuestos a la vista
    private final MutableLiveData<List<FeedbackItem>> feedbackItems = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> canRetry = new MutableLiveData<>(false);
    private final MutableLiveData<String> retryButtonText = new MutableLiveData<>("");
    private final MutableLiveData<Integer> score = new MutableLiveData<>(0);

    private int blockId;
    private int levelId;
    private int currentRetryCount;

    public FeedbackViewModel(@NonNull Application application) {
        super(application);
    }

    // 3. Inicializador: Recibe los datos crudos del Intent y los procesa
    public void processIntentData(int blockId, int levelId, int score, int retryCount,
                                  String[] equations, String[] corrects, String[] marks, String[] explanations) {
        this.blockId = blockId;
        this.levelId = levelId;
        this.currentRetryCount = retryCount;
        this.score.setValue(score);

        List<FeedbackItem> itemsList = new ArrayList<>();
        if (equations != null) {
            for (int i = 0; i < equations.length; i++) {
                String corr = (corrects != null && i < corrects.length) ? corrects[i] : "";
                String m = (marks != null && i < marks.length) ? marks[i] : "?";
                String exp = (explanations != null && i < explanations.length) ? explanations[i] : "";

                itemsList.add(new FeedbackItem(equations[i], corr, m, exp));
            }
        }
        this.feedbackItems.setValue(itemsList);

        // Lógica de reintentos
       /* if (retryCount < MAX_RETRIES) {
            canRetry.setValue(true);
            retryButtonText.setValue(R.string.btn_retry);
        } else {
            canRetry.setValue(false);
        }*/
    }

    // 4. Getters para la UI
    public LiveData<List<FeedbackItem>> getFeedbackItems() { return feedbackItems; }
    public LiveData<Boolean> getCanRetry() { return canRetry; }
    public LiveData<String> getRetryButtonText() { return retryButtonText; }
    public LiveData<Integer> getScore() { return score; }

    // 5. Getters para la navegación
    public int getBlockId() { return blockId; }
    public int getLevelId() { return levelId; }
    public int getNextRetryCount() { return currentRetryCount + 1; }


    /**
     * PREVENCIÓN DE FUGAS DE MEMORIA:
     * Este método se ejecuta automáticamente cuando el usuario cierra la pantalla de ejercicios.
     * Aquí liberamos cualquier recurso, limpiamos listas y detenemos procesos.
     */
    @Override
    protected void onCleared() {
        super.onCleared();
        // Limpiar referencias a listas pesadas
        if (feedbackItems.getValue() != null) {
            feedbackItems.getValue().clear();
        }
        // Restablecer los LiveData para que no mantengan estados viejos si se vuelve a abrir
        canRetry.setValue(false);
    }
}