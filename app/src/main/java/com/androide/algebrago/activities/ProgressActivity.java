package com.androide.algebrago.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.androide.algebrago.R;
import com.androide.algebrago.models.Block;
import com.androide.algebrago.viewmodel.MainViewModel;
import com.androide.algebrago.viewmodel.ViewModelFactory;
// Importamos tu vista personalizada del círculo líquido
import com.androide.algebrago.views.LiquidProgressView;

/**
 * MVVM — Vista de progreso.
 * Observa MainViewModel.getBlocks() y MainViewModel.getScore().
 */
public class ProgressActivity extends AppCompatActivity {

    private MainViewModel viewModel;
    private LinearLayout llProgressBlocks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        viewModel = new ViewModelProvider(this, new ViewModelFactory(getApplication()))
                .get(MainViewModel.class);

        TextView tvScore      = findViewById(R.id.tv_score_progress);
        ImageButton btnBack   = findViewById(R.id.btn_back_progress);
        llProgressBlocks      = findViewById(R.id.ll_progress_blocks);

        btnBack.setOnClickListener(v -> finish());

        viewModel.getScore().observe(this, score ->
                tvScore.setText(getString(R.string.score_label) + score));

        viewModel.getBlocks().observe(this, blocks -> {
            if (blocks == null || llProgressBlocks == null) return;
            llProgressBlocks.removeAllViews();
            for (Block b : blocks) {
                View row = LayoutInflater.from(this)
                        .inflate(R.layout.item_progress_block, llProgressBlocks, false);

                // 1. Asignamos el nombre del bloque
                ((TextView) row.findViewById(R.id.tv_progress_block_name)).setText(b.getName());

                // 2. CORRECCIÓN: Usamos tu LiquidProgressView y su ID correcto
                LiquidProgressView pb = row.findViewById(R.id.liquid_progress);
                int pct = viewModel.getFacade().getScoreManager().getBlockProgress(b.getId());
                pb.setProgress(pct);

                // 3. Asignamos el porcentaje en texto
                ((TextView) row.findViewById(R.id.tv_progress_percent)).setText(pct + "%");

                llProgressBlocks.addView(row);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.refresh();
    }
}