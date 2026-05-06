package com.androide.algebrago.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.androide.algebrago.R;
import com.androide.algebrago.adapters.LevelAdapter;
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

            // 1. Le pedimos al ViewModel que nos dé los datos masticados
            Block currentBlock = viewModel.getBlockById(blockId);
            Level currentLevel = viewModel.getLevelById(blockId, levelId);

            // 2. Pintamos los textos de explicación pidiéndoselos al ViewModel
            if (currentBlock != null) {
                tvExplanation.setText(viewModel.buildExplanation(blockId));
                tvSteps.setText(viewModel.buildSteps(blockId));
            }

            // 3. Pintamos el título del nivel
            if (currentLevel != null) {
                tvLevelTitle.setText(currentLevel.getName());
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
}