package com.androide.algebrago.feature.feedback;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.androide.algebrago.R;
import com.androide.algebrago.feature.score.ScoreActivity;
import com.androide.algebrago.feature.exercise.ExerciseActivity;
import com.androide.algebrago.shared.viewmodel.ViewModelFactory;

public class FeedbackActivity extends AppCompatActivity {

    public static final String EXTRA_BLOCK_ID    = "extra_block_id";
    public static final String EXTRA_LEVEL_ID    = "extra_level_id";
    public static final String EXTRA_EQUATIONS   = "extra_equations";
    public static final String EXTRA_CORRECTS    = "extra_corrects";
    public static final String EXTRA_MARKS       = "extra_marks";
    public static final String EXTRA_EXPLANATIONS= "extra_explanations";
    public static final String EXTRA_SCORE       = "extra_score";

    private FeedbackViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        viewModel = new ViewModelProvider(this, new ViewModelFactory(getApplication()))
                .get(FeedbackViewModel.class);

        // 1. Extraer Intent (Con programación defensiva)
        Intent in = getIntent();
        int blockId = in.getIntExtra(EXTRA_BLOCK_ID, -1);
        int levelId = in.getIntExtra(EXTRA_LEVEL_ID, -1);

        if (blockId == -1 || levelId == -1) {
            finish();
            return; // Evita cargar si los datos están corruptos
        }

        // 2. Mandamos la información cruda al ViewModel para que trabaje
        viewModel.processIntentData(
                blockId, levelId,
                in.getIntExtra(EXTRA_SCORE, 0),
                in.getIntExtra("retry_count", 0),
                in.getStringArrayExtra(EXTRA_EQUATIONS),
                in.getStringArrayExtra(EXTRA_CORRECTS),
                in.getStringArrayExtra(EXTRA_MARKS),
                in.getStringArrayExtra(EXTRA_EXPLANATIONS)
        );

        // 3. Referencias de UI
        com.google.android.material.button.MaterialButton tvScore = findViewById(R.id.tv_score_feedback);
        ImageButton btnBack = findViewById(R.id.btn_back_feedback);
        com.google.android.material.button.MaterialButton btnRetry = findViewById(R.id.btn_retry_level);
        com.google.android.material.button.MaterialButton btnFinish = findViewById(R.id.btn_finish_feedback);
        LinearLayout container = findViewById(R.id.ll_feedback_cards);

        // 4. Observadores
        viewModel.getScore().observe(this, score ->
                tvScore.setText(getString(R.string.score_label) + score));

        viewModel.getFeedbackItems().observe(this, items -> {
            container.removeAllViews();
            for (int i = 0; i < items.size(); i++) {
                addResultCard(container, i, items.get(i));
            }
        });

        viewModel.getCanRetry().observe(this, canRetry ->
                btnRetry.setVisibility(Boolean.TRUE.equals(canRetry) ? View.VISIBLE : View.GONE));

        viewModel.getRetryButtonText().observe(this, btnRetry::setText);

        // 5. Click Listeners para navegación
        btnBack.setOnClickListener(v -> finish());

        btnRetry.setOnClickListener(v -> {
            Intent intent = new Intent(this, ExerciseActivity.class);
            intent.putExtra(ExerciseActivity.EXTRA_BLOCK_ID, viewModel.getBlockId());
            intent.putExtra(ExerciseActivity.EXTRA_LEVEL_ID, viewModel.getLevelId());
            intent.putExtra("retry_count", viewModel.getNextRetryCount());
            startActivity(intent);
            finish();
        });

        btnFinish.setOnClickListener(v -> {
            Intent intent = new Intent(this, ScoreActivity.class);
            intent.putExtra(ScoreActivity.EXTRA_SCORE, viewModel.getScore().getValue());
            startActivity(intent);
            finish();
        });
    }

    // 6. El método UI ahora recibe el objeto procesado directamente
    private void addResultCard(LinearLayout container, int idx, FeedbackViewModel.FeedbackItem item) {
        View card = LayoutInflater.from(this)
                .inflate(R.layout.item_feedback_card, container, false);

        ((TextView) card.findViewById(R.id.tv_number)).setText(String.valueOf(idx + 1));
        ((TextView) card.findViewById(R.id.tv_equation)).setText(item.equation);
        ((TextView) card.findViewById(R.id.tv_correct_answer)).setText("Respuesta: " + item.correctAnswer);

        TextView tvMark = card.findViewById(R.id.tv_result);
        tvMark.setText(item.mark);

        // Usamos la variable booleana que ya calculó el ViewModel
        if (item.isCorrect) {
            tvMark.setTextColor(ContextCompat.getColor(this, R.color.black));
        } else {
            tvMark.setTextColor(ContextCompat.getColor(this, R.color.black));
        }

        TextView tvExpBody = card.findViewById(R.id.tv_explanation);
        tvExpBody.setVisibility(View.GONE);

        card.findViewById(R.id.btn_expand).setOnClickListener(v -> {
            if (tvExpBody.getVisibility() == View.GONE) {
                tvExpBody.setText(item.explanation);
                tvExpBody.setVisibility(View.VISIBLE);
            } else {
                tvExpBody.setVisibility(View.GONE);
            }
        });

        container.addView(card);
    }
}