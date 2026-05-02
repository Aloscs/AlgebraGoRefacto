package com.androide.algebrago.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import com.androide.algebrago.R;
import com.androide.algebrago.patterns.configManager;
import com.androide.algebrago.viewmodel.MainViewModel;
import com.androide.algebrago.viewmodel.ViewModelFactory;
import com.google.android.material.navigation.NavigationView;

/**
 * MVVM — Vista (View).
 * Responsabilidades: inflar layout, obtener refs de Views,
 * instanciar ViewModel, observar LiveData y delegar acciones al ViewModel.
 * NO tiene lógica de negocio ni acceso directo a datos.
 */
public class MainActivity extends AppCompatActivity {

    private MainViewModel viewModel;
    private TextView tvScore;
    private DrawerLayout drawerLayout;
    private configManager configMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        configMgr = new configManager(this);
        configMgr.updateResource(this, configMgr.getLang());
        if (configMgr.isDarkMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ── ViewModel ────────────────────────────────────────────────────────
        viewModel = new ViewModelProvider(this, new ViewModelFactory(getApplication()))
                .get(MainViewModel.class);
        viewModel.startObservingScore();

        bindViews();
        observeViewModel();
    }

    private void bindViews() {
        tvScore                     = findViewById(R.id.tv_score_main);
        Button btnStart             = findViewById(R.id.btn_start);
        Button btnProgress          = findViewById(R.id.btn_progress);
        ImageButton btnAchievements = findViewById(R.id.btn_achievements_icon);
        ImageButton btnSettings     = findViewById(R.id.btn_settings);
        drawerLayout                = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        if (navigationView != null) navigationView.setItemIconTintList(null);

        tvScore.setOnClickListener(v -> {
            int score = viewModel.getScore().getValue() != null ? viewModel.getScore().getValue() : 0;
            Intent intent = new Intent(this, ScoreActivity.class);
            intent.putExtra(ScoreActivity.EXTRA_SCORE, score);
            startActivity(intent);
        });

        if (btnSettings != null && drawerLayout != null)
            btnSettings.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        btnStart.setOnClickListener(v -> startActivity(new Intent(this, BlockSelectionActivity.class)));
        btnProgress.setOnClickListener(v -> startActivity(new Intent(this, ProgressActivity.class)));
        if (btnAchievements != null)
            btnAchievements.setOnClickListener(v -> startActivity(new Intent(this, AchievementsActivity.class)));

        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(item -> {
                int id = item.getItemId();
                if (id == R.id.nav_language)  mostrarDialogoIdioma();
                else if (id == R.id.nav_theme) toggleTema();
                if (drawerLayout != null) drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            });
        }
    }

    /** Observa LiveData: la Activity solo actualiza la UI, nunca toca datos. */
    private void observeViewModel() {
        viewModel.getScore().observe(this, score ->
                tvScore.setText(getString(R.string.score_label) + score));
    }

    @Override protected void onResume() { super.onResume(); viewModel.refresh(); }

    private void mostrarDialogoIdioma() {
        String[] opciones = {"Español", "English"};
        new android.app.AlertDialog.Builder(this)
                .setTitle("Seleccionar Idioma")
                .setItems(opciones, (dialog, which) -> {
                    configMgr.setLocale(this, which == 0 ? "es" : "en");
                    recreate();
                }).show();
    }

    private void toggleTema() {
        boolean nuevo = !configMgr.isDarkMode();
        configMgr.setDarkMode(nuevo);
        AppCompatDelegate.setDefaultNightMode(
                nuevo ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
    }
}
