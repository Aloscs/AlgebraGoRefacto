package com.androide.algebrago.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.androide.algebrago.R;

public class FeedbackActivity extends AppCompatActivity {

    public static final String EXTRA_BLOCK_ID    = "extra_block_id";
    public static final String EXTRA_LEVEL_ID    = "extra_level_id";
    public static final String EXTRA_EQUATIONS   = "extra_equations";
    public static final String EXTRA_CORRECTS    = "extra_corrects";
    public static final String EXTRA_MARKS       = "extra_marks";
    public static final String EXTRA_EXPLANATIONS= "extra_explanations";
    public static final String EXTRA_SCORE       = "extra_score";
    private static final int MAX_RETRIES = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        Intent in         = getIntent();
        int blockId       = in.getIntExtra(EXTRA_BLOCK_ID, 1);
        int levelId       = in.getIntExtra(EXTRA_LEVEL_ID, 11);
        String[] equations    = in.getStringArrayExtra(EXTRA_EQUATIONS);
        String[] corrects     = in.getStringArrayExtra(EXTRA_CORRECTS);
        String[] marks        = in.getStringArrayExtra(EXTRA_MARKS);
        String[] explanations = in.getStringArrayExtra(EXTRA_EXPLANATIONS);
        int score         = in.getIntExtra(EXTRA_SCORE, 0);
        int retryCount    = in.getIntExtra("retry_count", 0);

        // Referencias principales del activity_feedback.xml
        com.google.android.material.button.MaterialButton tvScore = findViewById(R.id.tv_score_feedback);
        ImageButton btnBack = findViewById(R.id.btn_back_feedback);
        com.google.android.material.button.MaterialButton btnRetry = findViewById(R.id.btn_retry_level);
        com.google.android.material.button.MaterialButton btnFinish = findViewById(R.id.btn_finish_feedback);
        LinearLayout container = findViewById(R.id.ll_feedback_cards);

        tvScore.setText(getString(R.string.score_label) + score);
        btnBack.setOnClickListener(v -> finish());

        if (equations != null) {
            for (int i = 0; i < equations.length; i++) {
                addResultCard(container, i, equations[i],
                        corrects != null && i < corrects.length ? corrects[i] : "",
                        marks    != null && i < marks.length    ? marks[i]    : "?",
                        explanations != null && i < explanations.length ? explanations[i] : "");
            }
        }

        if (retryCount < MAX_RETRIES) {
            btnRetry.setVisibility(View.VISIBLE);
            btnRetry.setText(getString(R.string.btn_retry) + " (" + (MAX_RETRIES - retryCount) + " restantes)");
            btnRetry.setOnClickListener(v -> {
                Intent intent = new Intent(this, ExerciseActivity.class);
                intent.putExtra(ExerciseActivity.EXTRA_BLOCK_ID, blockId);
                intent.putExtra(ExerciseActivity.EXTRA_LEVEL_ID, levelId);
                intent.putExtra("retry_count", retryCount + 1);
                startActivity(intent);
                finish();
            });
        } else {
            btnRetry.setVisibility(View.GONE);
        }

        btnFinish.setOnClickListener(v -> {
            Intent intent = new Intent(this, ScoreActivity.class);
            intent.putExtra(ScoreActivity.EXTRA_SCORE, score);
            startActivity(intent);
            finish();
        });
    }

    private void addResultCard(LinearLayout container, int idx, String equation,
                               String correct, String mark, String explanation) {
        View card = LayoutInflater.from(this)
                .inflate(R.layout.item_feedback_card, container, false);

        // ════════════════════════════════════════════════════════
        // MATCH EXACTO CON TU item_feedback_card.xml
        // ════════════════════════════════════════════════════════

        // Número de la pregunta
        ((TextView) card.findViewById(R.id.tv_number)).setText(String.valueOf(idx + 1));

        // Ecuación
        ((TextView) card.findViewById(R.id.tv_equation)).setText(equation);

        // Respuesta correcta
        ((TextView) card.findViewById(R.id.tv_correct_answer)).setText("Respuesta: " + correct);

        // Marca (la palomita o tache)
        TextView tvMark = card.findViewById(R.id.tv_result);
        tvMark.setText(mark);

        // Lógica visual si es correcto o no
        boolean isCorrect = "✓".equals(mark);

        // Opcional: Si quieres que la palomita sea verde y el tache rojo (Neo-Brutalismo)
        if (isCorrect) {
            tvMark.setTextColor(ContextCompat.getColor(this, R.color.black)); // Puedes cambiar a un verde sólido luego si gustas
        } else {
            tvMark.setTextColor(ContextCompat.getColor(this, R.color.black)); // Puedes cambiar a rojo
        }

        // Explicación
        TextView tvExpBody = card.findViewById(R.id.tv_explanation);
        tvExpBody.setVisibility(View.GONE);

        // Botón de expandir
        card.findViewById(R.id.btn_expand).setOnClickListener(v -> {
            if (tvExpBody.getVisibility() == View.GONE) {
                tvExpBody.setText(explanation);
                tvExpBody.setVisibility(View.VISIBLE);
            } else {
                tvExpBody.setVisibility(View.GONE);
            }
        });

        container.addView(card);
    }
}