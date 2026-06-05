package com.androide.algebrago.feature.feedback;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.androide.algebrago.R;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel para {@link FeedbackActivity}.
 *
 * MVVM — responsabilidades:
 * <ul>
 *   <li>Procesar los datos crudos recibidos del Intent y exponerlos como
 *       objetos tipados ({@link FeedbackItem}).</li>
 *   <li>Gestionar la lógica de reintentos (máx. {@value #MAX_RETRIES}).</li>
 *   <li>Exponer LiveData para que la Activity actualice la UI reactivamente.</li>
 *   <li>Liberar recursos al destruirse ({@link #onCleared()}).</li>
 * </ul>
 */
public class FeedbackViewModel extends AndroidViewModel {

    /**
     * Representa un ítem de retroalimentación ya procesado, listo para mostrarse en la UI.
     * Encapsula los datos de un ejercicio: ecuación, respuesta correcta, marca y explicación.
     */
    public static class FeedbackItem {
        /** Texto de la ecuación presentada al estudiante. */
        public final String equation;
        /** Respuesta correcta de la ecuación. */
        public final String correctAnswer;
        /** Marca de resultado: "✓" si fue correcto, "✗" si fue incorrecto. */
        public final String mark;
        /** Explicación paso a paso que se muestra al expandir la tarjeta. */
        public final String explanation;
        /** {@code true} si la respuesta del estudiante fue correcta. */
        public final boolean isCorrect;

        /**
         * Construye un ítem de retroalimentación a partir de los datos en bruto.
         *
         * @param equation      texto de la ecuación.
         * @param correctAnswer respuesta correcta.
         * @param mark          marca de resultado ("✓" o "✗").
         * @param explanation   explicación paso a paso.
         */
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

    /**
     * Crea una nueva instancia del ViewModel.
     *
     * @param application contexto de la aplicación requerido por {@link AndroidViewModel}.
     */
    public FeedbackViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * Procesa los datos crudos recibidos del Intent y actualiza los LiveData.
     * Convierte los arrays paralelos (equations, corrects, marks, explanations)
     * en una lista tipada de {@link FeedbackItem}.
     *
     * @param blockId      ID del bloque del nivel completado.
     * @param levelId      ID del nivel completado.
     * @param score        puntuación obtenida en la sesión.
     * @param retryCount   número de reintentos ya realizados para este nivel.
     * @param equations    ecuaciones presentadas (una por ejercicio).
     * @param corrects     respuestas correctas de cada ejercicio.
     * @param marks        marcas de resultado ("✓" o "✗") de cada ejercicio.
     * @param explanations explicaciones paso a paso de cada ejercicio.
     */
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
    /** @return LiveData con la lista de ítems de retroalimentación procesados. */
    public LiveData<List<FeedbackItem>> getFeedbackItems() { return feedbackItems; }
    /** @return LiveData que indica si el botón de reintento debe mostrarse. */
    public LiveData<Boolean> getCanRetry() { return canRetry; }
    /** @return LiveData con el texto del botón de reintento. */
    public LiveData<String> getRetryButtonText() { return retryButtonText; }
    /** @return LiveData con la puntuación obtenida en la sesión. */
    public LiveData<Integer> getScore() { return score; }

    // 5. Getters para la navegación
    /** @return ID del bloque del nivel completado. */
    public int getBlockId() { return blockId; }
    /** @return ID del nivel completado. */
    public int getLevelId() { return levelId; }
    /** @return contador de reintentos incrementado en uno para el siguiente intento. */
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