package com.androide.algebrago.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.androide.algebrago.R;
import com.androide.algebrago.adapters.BlockAdapter;
import com.androide.algebrago.viewmodel.MainViewModel;
import com.androide.algebrago.viewmodel.ViewModelFactory;

/**
 * MVVM — Vista para selección de bloque.
 * Observa MainViewModel.getBlocks() y MainViewModel.getScore().
 */
public class BlockSelectionActivity extends AppCompatActivity {

    private MainViewModel viewModel;
    private TextView tvScore;
    private RecyclerView rvBlocks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_selection);

        viewModel = new ViewModelProvider(this, new ViewModelFactory(getApplication()))
                .get(MainViewModel.class);

        tvScore             = findViewById(R.id.tv_score_block);
        ImageButton btnBack = findViewById(R.id.btn_back_block);
        rvBlocks            = findViewById(R.id.rv_blocks);

        btnBack.setOnClickListener(v -> finish());
        tvScore.setOnClickListener(v -> {
            int score = viewModel.getScore().getValue() != null ? viewModel.getScore().getValue() : 0;
            Intent intent = new Intent(this, ScoreActivity.class);
            intent.putExtra(ScoreActivity.EXTRA_SCORE, score);
            startActivity(intent);
        });

        setupRecyclerView();
        observeViewModel();
    }

    private void setupRecyclerView() {
        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvBlocks.setLayoutManager(lm);
        rvBlocks.setClipToPadding(false);
        rvBlocks.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        SnapHelper snap = new PagerSnapHelper();
        snap.attachToRecyclerView(rvBlocks);

        rvBlocks.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView rv, int dx, int dy) {
                float cx = rv.getWidth() / 2f;
                for (int i = 0; i < rv.getChildCount(); i++) {
                    View child = rv.getChildAt(i);
                    float dist  = Math.abs(cx - (child.getLeft() + child.getRight()) / 2f);
                    float scale = Math.max(0.82f, 1f - (dist / rv.getWidth()) * 0.35f);
                    child.setScaleX(scale);
                    child.setScaleY(scale);
                    child.setAlpha(scale);
                }
            }
        });
    }

    private void observeViewModel() {
        viewModel.getScore().observe(this, score ->
                tvScore.setText(getString(R.string.score_label) + score));

        viewModel.getBlocks().observe(this, blocks -> {
            if (blocks == null) return;
            rvBlocks.setAdapter(new BlockAdapter(blocks, block -> {
                Intent intent = new Intent(this, LevelSelectionActivity.class);
                intent.putExtra(LevelSelectionActivity.EXTRA_BLOCK_ID, block.getId());
                startActivity(intent);
            }));
        });
    }

    @Override protected void onResume() { super.onResume(); viewModel.refresh(); }
}
