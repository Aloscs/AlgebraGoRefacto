package com.androide.algebrago.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androide.algebrago.R;
import com.androide.algebrago.adapters.LevelAdapter;
import com.androide.algebrago.models.Block;
import com.androide.algebrago.viewmodel.MainViewModel;
import com.androide.algebrago.viewmodel.ViewModelFactory;
// Importación de MaterialButton
import com.google.android.material.button.MaterialButton;

/**
 * MVVM — Vista para selección de nivel.
 * Obtiene los niveles del bloque seleccionado a través de MainViewModel.
 */
public class LevelSelectionActivity extends AppCompatActivity {

    public static final String EXTRA_BLOCK_ID = "extra_block_id";

    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_selection);

        int blockId = getIntent().getIntExtra(EXTRA_BLOCK_ID, 1);

        viewModel = new ViewModelProvider(this, new ViewModelFactory(getApplication()))
                .get(MainViewModel.class);

        // CORRECCIÓN: Tipos y nombres de ID actualizados para coincidir con activity_level_selection.xml
        MaterialButton tvScore       = findViewById(R.id.tv_score_level);
        TextView tvBlockTitle        = findViewById(R.id.tv_block_name_level); // Cambiado a tv_block_name_level
        ImageButton btnBack          = findViewById(R.id.btn_back_level);
        RecyclerView rvLevels        = findViewById(R.id.rv_levels);

        btnBack.setOnClickListener(v -> finish());

        viewModel.getScore().observe(this, score ->
                tvScore.setText(getString(R.string.score_label) + score));

        viewModel.getBlocks().observe(this, blocks -> {
            Block currentBlock = viewModel.getBlockById(blockId);
            if (currentBlock != null) {
                tvBlockTitle.setText(currentBlock.getName());
                rvLevels.setLayoutManager(new LinearLayoutManager(this));
                rvLevels.setAdapter(new LevelAdapter(currentBlock.getLevels(), level -> {
                    Intent intent = new Intent(this, ExplanationActivity.class);
                    intent.putExtra(ExplanationActivity.EXTRA_BLOCK_ID, blockId);
                    intent.putExtra(ExplanationActivity.EXTRA_LEVEL_ID, level.getId());
                    startActivity(intent);
                }));
            }
        });

    }

    @Override protected void onResume() {
        super.onResume();
        viewModel.refresh();
        if (findViewById(R.id.rv_levels) instanceof RecyclerView) {
            RecyclerView rv = findViewById(R.id.rv_levels);
            if (rv.getAdapter() != null) rv.getAdapter().notifyDataSetChanged();
        }
    }
}