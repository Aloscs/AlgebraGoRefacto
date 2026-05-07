package com.androide.algebrago.feature.achivements;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.androide.algebrago.R;
import com.androide.algebrago.domain.models.Achievement;
import com.androide.algebrago.feature.home.MainViewModel;
import com.androide.algebrago.shared.viewmodel.ViewModelFactory;

/**
 * MVVM — Vista de logros.
 * Observa MainViewModel.getAchievements() y MainViewModel.getScore().
 */
public class AchievementsActivity extends AppCompatActivity {

    private MainViewModel viewModel;
    private LinearLayout llGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        viewModel = new ViewModelProvider(this, new ViewModelFactory(getApplication()))
                .get(MainViewModel.class);

        TextView tvScore    = findViewById(R.id.tv_score_achievements);
        ImageButton btnBack = findViewById(R.id.btn_back_achievements);
        llGrid              = findViewById(R.id.ll_achievements_grid);

        btnBack.setOnClickListener(v -> finish());

        viewModel.getScore().observe(this, score ->
                tvScore.setText(getString(R.string.score_label) + score));

        viewModel.getAchievements().observe(this, achievements -> {
            if (achievements == null || llGrid == null) return;
            llGrid.removeAllViews();
            for (Achievement a : achievements) {
                View card = LayoutInflater.from(this)
                        .inflate(R.layout.item_achievement_card, llGrid, false);
                ((TextView) card.findViewById(R.id.tv_achievement_icon)).setText(iconForType(a.getType()));
                ((TextView) card.findViewById(R.id.tv_achievement_name)).setText(a.getName());
                ((TextView) card.findViewById(R.id.tv_achievement_desc)).setText(a.getDescription());
                TextView status = card.findViewById(R.id.tv_achievement_status);
                if (a.isUnlocked()) { status.setText("✓ Obtenido"); card.setAlpha(1f); }
                else                { status.setText("🔒 Bloqueado"); card.setAlpha(0.5f); }
                llGrid.addView(card);
            }
        });
    }

    @Override protected void onResume() { super.onResume(); viewModel.refresh(); }

    private String iconForType(Achievement.AchievementType t) {
        switch (t) {
            case STREAK_CORRECT: return "⚡";
            case DAILY_STREAK:   return "📅";
            case PERFECT_LEVEL:  return "⭐";
            case BLOCK_COMPLETE: return "🏅";
            case TOPIC_MASTERY:  return "🎓";
            default:             return "🏆";
        }
    }
}
