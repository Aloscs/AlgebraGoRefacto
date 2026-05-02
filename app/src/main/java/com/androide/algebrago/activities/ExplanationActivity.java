package com.androide.algebrago.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.androide.algebrago.R;
import com.androide.algebrago.models.Block;
import com.androide.algebrago.models.Level;
import com.androide.algebrago.viewmodel.MainViewModel;
import com.androide.algebrago.viewmodel.ViewModelFactory;
// Importamos MaterialButton para que coincida con tu diseño Neo-Brutalista
import com.google.android.material.button.MaterialButton;

/**
 * MVVM — Vista de explicación.
 * Obtiene bloque y nivel desde MainViewModel para construir el texto explicativo.
 * Toda la navegación y contenido educativo sigue igual que la versión MVC.
 */
public class ExplanationActivity extends AppCompatActivity {

    public static final String EXTRA_BLOCK_ID = "extra_block_id";
    public static final String EXTRA_LEVEL_ID = "extra_level_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explanation);

        int blockId = getIntent().getIntExtra(EXTRA_BLOCK_ID, 1);
        int levelId = getIntent().getIntExtra(EXTRA_LEVEL_ID, 11);

        MainViewModel viewModel = new ViewModelProvider(this, new ViewModelFactory(getApplication()))
                .get(MainViewModel.class);

        // CORRECCIÓN: Actualizamos los tipos a MaterialButton y eliminamos el btnSkip fantasma
        MaterialButton tvScore          = findViewById(R.id.tv_score_explanation);
        TextView tvLevelTitle           = findViewById(R.id.tv_explanation_title);
        TextView tvExplanation          = findViewById(R.id.tv_explanation_body);
        TextView tvSteps                = findViewById(R.id.tv_explanation_steps);
        ImageButton btnBack             = findViewById(R.id.btn_back_explanation);
        MaterialButton btnStartExercise = findViewById(R.id.btn_start_exercise);

        viewModel.getScore().observe(this, score ->
                tvScore.setText(getString(R.string.score_label) + score));

        viewModel.getBlocks().observe(this, blocks -> {
            if (blocks == null) return;
            for (Block b : blocks) {
                if (b.getId() == blockId) {
                    tvExplanation.setText(buildExplanation(blockId));
                    tvSteps.setText(buildSteps(blockId));
                    for (Level l : b.getLevels()) {
                        if (l.getId() == levelId) {
                            tvLevelTitle.setText(l.getName());
                            break;
                        }
                    }
                    break;
                }
            }
        });

        btnBack.setOnClickListener(v -> finish());

        Intent exerciseIntent = new Intent(this, ExerciseActivity.class);
        exerciseIntent.putExtra(ExerciseActivity.EXTRA_BLOCK_ID, blockId);
        exerciseIntent.putExtra(ExerciseActivity.EXTRA_LEVEL_ID, levelId);

        // CORRECCIÓN: Ahora solo usamos el botón principal para avanzar
        btnStartExercise.setOnClickListener(v -> {
            startActivity(exerciseIntent);
            finish();
        });
    }

    private String buildExplanation(int blockId) {
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

    private String buildSteps(int blockId) {
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
}