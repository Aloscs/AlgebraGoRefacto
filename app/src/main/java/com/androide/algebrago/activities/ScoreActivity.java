package com.androide.algebrago.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.androide.algebrago.R;

/**
 * MVVM — Vista de puntaje final.
 * Recibe el score como Extra; no necesita ViewModel propio.
 * Patrón Data Spotlight (Tidwell): número grande centrado como recompensa.
 */
public class ScoreActivity extends AppCompatActivity {

    public static final String EXTRA_SCORE = "extra_score";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        int score = getIntent().getIntExtra(EXTRA_SCORE, 0);

        ((TextView) findViewById(R.id.tv_big_score)).setText(String.valueOf(score));

        ((Button) findViewById(R.id.btn_go_home)).setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }
}
