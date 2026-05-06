package com.androide.algebrago.activities;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.androide.algebrago.R;
import com.androide.algebrago.models.Exercise;
import com.androide.algebrago.viewmodel.ExerciseViewModel;
import com.androide.algebrago.viewmodel.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * MVVM — Vista para la pantalla de ejercicios.
 *
 * La Activity solo:
 *  1. Observa LiveData del ExerciseViewModel y actualiza la UI.
 *  2. Delega las acciones del usuario (submitAnswer, nextExercise) al ViewModel.
 *  3. Mantiene la lógica de renderizado de modos (balance/completar).
 *
 * NO tiene acceso directo a AppFacade, ScoreManager ni Repository.
 */
public class ExerciseActivity extends AppCompatActivity {

    public static final String EXTRA_BLOCK_ID = "extra_block_id";
    public static final String EXTRA_LEVEL_ID = "extra_level_id";

    // ── ViewModel ─────────────────────────────────────────────────────────────
    private ExerciseViewModel viewModel;

    // ── Views ─────────────────────────────────────────────────────────────────
    private TextView tvScore, tvExerciseCounter, tvInstruction, tvEquation;
    private ImageButton btnHint;
    private Button btnSubmit, btnNext;
    private FrameLayout exerciseContentContainer;

    // Modo completar
    private RadioGroup rgOptions;

    // Modo balanza
    private LinearLayout layoutOptionsBalance, dropZoneLeft, dropZoneRight, balanceArmAssembly;
    private TextView tvLeftPan, tvRightPan;

    // Estado local de arrastre (sólo UI, no lógica de negocio)
    private String[] leftSlots;
    private String[] rightSlots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        int blockId = getIntent().getIntExtra(EXTRA_BLOCK_ID, 1);
        int levelId = getIntent().getIntExtra(EXTRA_LEVEL_ID, 11);

        // ── ViewModel ────────────────────────────────────────────────────────
        viewModel = new ViewModelProvider(this, new ViewModelFactory(getApplication()))
                .get(ExerciseViewModel.class);

        bindViews();
        observeViewModel();

        // Arrancar carga de ejercicios (consulta Room → dominio)
        viewModel.loadExercises(blockId, levelId);
    }

    // ── Binding ───────────────────────────────────────────────────────────────

    private void bindViews() {
        tvScore             = findViewById(R.id.tv_score_exercise);
        tvExerciseCounter   = findViewById(R.id.tv_exercise_counter);
        tvInstruction       = findViewById(R.id.tv_exercise_instruction);
        tvEquation          = findViewById(R.id.tv_equation);
        btnHint             = findViewById(R.id.btn_hint);
        btnSubmit           = findViewById(R.id.btn_submit);
        btnNext             = findViewById(R.id.btn_next_exercise);
        exerciseContentContainer = findViewById(R.id.exercise_content_container);

        btnHint.setOnClickListener(v -> showHintDialog());
        btnSubmit.setOnClickListener(v -> handleSubmitClick());
        btnNext.setOnClickListener(v -> viewModel.nextExercise());

        com.google.android.material.appbar.MaterialToolbar toolbar = findViewById(R.id.toolbar_exercise);
        if (toolbar != null) toolbar.setNavigationOnClickListener(v -> finish());
    }

    // ── Observadores LiveData ─────────────────────────────────────────────────

    private void observeViewModel() {
        // Score en tiempo real
        viewModel.getSessionScore().observe(this, score ->
                tvScore.setText(getString(R.string.score_label) + score));

        // Ejercicio actual → renderiza la UI del modo correspondiente
        viewModel.getCurrentExercise().observe(this, exercise -> {
            if (exercise == null) return;
            int index  = viewModel.getCurrentIndex();
            int total  = viewModel.getExerciseCount();
            tvExerciseCounter.setText(getString(R.string.exercise_of, index + 1, total));
            btnNext.setVisibility(View.GONE);
            btnSubmit.setVisibility(View.VISIBLE);
            tvEquation.setBackgroundColor(Color.TRANSPARENT);

            if (exercise.getType() == Exercise.ExerciseType.COMPLETE_EQUATION) {
                showCompleteMode(exercise);
            } else {
                showBalanceMode(exercise);
            }
        });

        // Resultado de respuesta → retroalimentación visual
        viewModel.getAnswerResult().observe(this, result -> {
            if (result == null) return;
            Exercise ex = viewModel.getCurrentExercise().getValue();
            switch (result) {
                case CORRECT:
                    if (ex != null) {
                        tvEquation.setBackgroundColor(
                                ContextCompat.getColor(this, R.color.correct_bg));
                        tvEquation.setText("✓  " + ex.getEquationFull());
                    }
                    btnNext.setVisibility(View.VISIBLE);
                    btnSubmit.setVisibility(View.GONE);
                    break;
                case WRONG:
                    if (ex != null) {
                        tvEquation.setBackgroundColor(
                                ContextCompat.getColor(this, R.color.gray_light));
                        tvEquation.setText("✗  " + ex.getEquationFull());
                    }
                    Toast.makeText(this, getString(R.string.incorrect_answer), Toast.LENGTH_SHORT).show();
                    break;
                case NONE:
                    tvEquation.setBackgroundColor(Color.TRANSPARENT);
                    break;
            }
        });

        // Sesión completa → navegar a Feedback
        viewModel.getSessionComplete().observe(this, complete -> {
            if (Boolean.TRUE.equals(complete)) navigateToFeedback();
        });

        // Loading indicator
        viewModel.getIsLoading().observe(this, loading -> {
            if (Boolean.TRUE.equals(loading)) {
                tvInstruction.setText(R.string.aviso_cargando_ejercicios);
            }
        });

        // Score inicial
        viewModel.getSessionScore().observe(this, score ->
                tvScore.setText(getString(R.string.score_label) + score));

        viewModel.getAchievementNotification().observe(this, achievementName -> {
            if (achievementName != null) {
                // Muestra el Toast, Snackbar, o Dialog. ¡Aquí sí es válido!
                Toast.makeText(this, R.string.texto_logro_desbloqueado + achievementName + R.string.signo_admiracion, Toast.LENGTH_LONG).show();

                // Limpiamos el evento para no repetir el Toast por accidente
                viewModel.clearAchievementNotification();
            }
        });

        viewModel.getIsCurrentlyBalanced().observe(this, isBalanced -> {
            if (isBalanced != null) {
                animateBalance(isBalanced);
            }
        });
    }

    // ── Acción: verificar respuesta ───────────────────────────────────────────

    private void handleSubmitClick() {
        Exercise ex = viewModel.getCurrentExercise().getValue();
        if (ex == null) return;
        if (ex.getType() == Exercise.ExerciseType.COMPLETE_EQUATION) {
            // Recolectar respuesta de RadioButtons
            if (rgOptions == null || rgOptions.getCheckedRadioButtonId() == -1) return;
            RadioButton rb = rgOptions.findViewById(rgOptions.getCheckedRadioButtonId());

            // Le mandamos el texto crudo al ViewModel
            viewModel.submitAnswerString(rb.getText().toString().trim());

        } else {
            // Recolectar respuesta de la Balanza
            List<String> placedTokens = new ArrayList<>();
            if (leftSlots != null)  for (String s : leftSlots)  if (s != null) placedTokens.add(s);
            if (rightSlots != null) for (String s : rightSlots) if (s != null) placedTokens.add(s);

            // Le mandamos la lista de fichas al ViewModel
            viewModel.submitAnswerList(placedTokens);
        }

    }

    /** La verificación de respuesta sigue siendo local (lógica de presentación). */
   /* private boolean checkAnswerLocally(Exercise ex) {
        if (ex.getType() == Exercise.ExerciseType.COMPLETE_EQUATION) {
            if (rgOptions == null) return false;
            int selectedId = rgOptions.getCheckedRadioButtonId();
            if (selectedId == -1) return false;
            RadioButton rb = rgOptions.findViewById(selectedId);
            if (rb == null) return false;
            return ex.getCorrectValues().contains(rb.getText().toString().trim());
        } else {
            List<String> placed = new ArrayList<>();
            if (leftSlots != null)  for (String s : leftSlots)  if (s != null) placed.add(s);
            if (rightSlots != null) for (String s : rightSlots) if (s != null) placed.add(s);
            List<String> correct = ex.getCorrectValues();
            if (placed.size() != correct.size()) return false;
            for (String c : correct) if (!placed.contains(c)) return false;
            return true;
        }
    }*/

    // ── Pista ─────────────────────────────────────────────────────────────────

    private void showHintDialog() {
        if (!viewModel.canShowHint()) return;
        Exercise ex = viewModel.getCurrentExercise().getValue();
        if (ex == null) return;
        new AlertDialog.Builder(this, R.style.Dialog_AlgebraGo)
                .setTitle(getString(R.string.hint_title) + " 💡")
                .setMessage(ex.getHint())
                .setPositiveButton(R.string.texto_boton_hint, null)
                .show();
    }

    // ── Modos de ejercicio ────────────────────────────────────────────────────

    private void showCompleteMode(Exercise ex) {
        exerciseContentContainer.removeAllViews();
        View optionsView = getLayoutInflater().inflate(
                R.layout.layout_exercise_options, exerciseContentContainer, false);
        exerciseContentContainer.addView(optionsView);
        rgOptions = optionsView.findViewById(R.id.rg_options);
        tvInstruction.setText(getString(R.string.exercise_instruction_complete));
        tvEquation.setText(ex.getEquationDisplay());
        rgOptions.removeAllViews();
        for (String opt : ex.getOptions()) {
            RadioButton rb = new RadioButton(this);
            rb.setText(opt);
            rb.setTextSize(16f);
            rb.setTextColor(ContextCompat.getColor(this, R.color.black));
            rb.setPadding(24, 16, 24, 16);
            rgOptions.addView(rb);
        }
    }

    private void showBalanceMode(Exercise ex) {
        exerciseContentContainer.removeAllViews();
        View balanceView = getLayoutInflater().inflate(
                R.layout.layout_exercise_balance, exerciseContentContainer, false);
        exerciseContentContainer.addView(balanceView);

        layoutOptionsBalance = balanceView.findViewById(R.id.layout_balance_options);
        tvLeftPan            = balanceView.findViewById(R.id.tv_left_pan_label);
        tvRightPan           = balanceView.findViewById(R.id.tv_right_pan_label);
        balanceArmAssembly   = balanceView.findViewById(R.id.balance_arm_assembly);
        dropZoneLeft         = balanceView.findViewById(R.id.drop_zone_left);
        dropZoneRight        = balanceView.findViewById(R.id.drop_zone_right);

        tvInstruction.setText(getString(R.string.exercise_instruction_balance));
        tvEquation.setText(ex.getEquationFull());
        tvLeftPan.setText(ex.getLeftSide() != null ? ex.getLeftSide() : "___");
        tvRightPan.setText(ex.getRightSide() != null ? ex.getRightSide() : "___");

        leftSlots  = new String[countBlanks(ex.getLeftSide())];
        rightSlots = new String[countBlanks(ex.getRightSide())];

        if (balanceArmAssembly != null) balanceArmAssembly.setRotation(-10f);
        buildDragOptions(ex);
        setupDropZones(ex);
    }

    private int countBlanks(String s) {
        if (s == null) return 1;
        int c = 0;
        for (char ch : s.toCharArray()) if (ch == '?') c++;
        return Math.max(c, 1);
    }

    private void buildDragOptions(Exercise ex) {
        layoutOptionsBalance.removeAllViews();
        for (String opt : ex.getOptions()) {
            Button chip = new Button(this);
            chip.setText(opt);
            chip.setTextSize(16f);
            chip.setBackgroundResource(R.drawable.bg_option_card);
            chip.setTextColor(ContextCompat.getColor(this, R.color.black));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(8, 0, 8, 0);
            chip.setLayoutParams(lp);
            chip.setTag(opt);
            chip.setOnLongClickListener(v -> {
                ClipData cd = ClipData.newPlainText("token", opt);
                v.startDragAndDrop(cd, new View.DragShadowBuilder(v), v, 0);
                return true;
            });
            layoutOptionsBalance.addView(chip);
        }
    }

    private void setupDropZones(Exercise ex) {
        dropZoneLeft.setOnDragListener((v, e) -> handleDrag(e, 0, ex));
        dropZoneRight.setOnDragListener((v, e) -> handleDrag(e, 1, ex));
    }

    private boolean handleDrag(DragEvent event, int side, Exercise ex) {
        LinearLayout zone = side == 0 ? dropZoneLeft : dropZoneRight;
        List<String> placedTokens = new ArrayList<>();
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:  return true;
            case DragEvent.ACTION_DRAG_ENTERED:
                zone.setBackgroundResource(R.drawable.bg_drop_active); return true;
            case DragEvent.ACTION_DRAG_EXITED:
                zone.setBackgroundResource(R.drawable.bg_drop_zone); return true;
            case DragEvent.ACTION_DROP:
                String token = event.getClipData().getItemAt(0).getText().toString();
                if (side == 0 && leftSlots.length > 0)  leftSlots[0] = token;
                if (side == 1 && rightSlots.length > 0) rightSlots[0] = token;
                updateBalanceDisplay(ex);
                if (leftSlots != null)  for (String s : leftSlots)  if (s != null) placedTokens.add(s);
                if (rightSlots != null) for (String s : rightSlots) if (s != null) placedTokens.add(s);
                viewModel.evaluateBalanceRealTime(placedTokens);
                zone.setBackgroundResource(R.drawable.bg_drop_zone);
                return true;
            case DragEvent.ACTION_DRAG_ENDED:
                if (!event.getResult()) {
                    View dragged = (View) event.getLocalState();
                    if (dragged != null) {
                        removeTokenFromSlots((String) dragged.getTag());
                        updateBalanceDisplay(ex);
                        if (leftSlots != null)  for (String s : leftSlots)  if (s != null) placedTokens.add(s);
                        if (rightSlots != null) for (String s : rightSlots) if (s != null) placedTokens.add(s);
                        viewModel.evaluateBalanceRealTime(placedTokens);
                    }
                }
                return true;
        }
        return false;
    }

    private void removeTokenFromSlots(String token) {
        if (leftSlots  != null) for (int i = 0; i < leftSlots.length;  i++) if (token.equals(leftSlots[i]))  leftSlots[i]  = null;
        if (rightSlots != null) for (int i = 0; i < rightSlots.length; i++) if (token.equals(rightSlots[i])) rightSlots[i] = null;
    }

    private void updateBalanceDisplay(Exercise ex) {
        tvLeftPan.setText(fillBlanks(ex.getLeftSide(), leftSlots));
        tvRightPan.setText(fillBlanks(ex.getRightSide(), rightSlots));
    }

    private String fillBlanks(String template, String[] slots) {
        if (template == null) return "";
        StringBuilder sb = new StringBuilder(template);
        int idx = 0;
        for (int i = 0; i < sb.length(); i++) {
            if (sb.charAt(i) == '?') {
                String val = (slots != null && idx < slots.length && slots[idx] != null) ? slots[idx] : "_";
                sb.replace(i, i + 1, val);
                i += val.length() - 1;
                idx++;
            }
        }
        return sb.toString();
    }

    private void animateBalance(boolean isBalanced) {
        if (balanceArmAssembly == null) return;

        // Ya no llamamos a checkAnswerLocally aquí
        balanceArmAssembly.animate()
                .rotation(isBalanced ? 0f : -10f)
                .setDuration(isBalanced ? 600 : 300)
                .setInterpolator(isBalanced
                        ? new android.view.animation.OvershootInterpolator()
                        : new android.view.animation.DecelerateInterpolator())
                .start();
    }

    // ── Navegación al finalizar ───────────────────────────────────────────────

    private void navigateToFeedback() {
        List<String[]> results   = viewModel.getSessionResults();
        List<Exercise> exercises = viewModel.getExerciseList();
        if (exercises == null) exercises = new ArrayList<>();

        String[] eqs     = new String[results.size()];
        String[] corrects = new String[results.size()];
        String[] marks   = new String[results.size()];
        String[] exps    = new String[results.size()];
        for (int i = 0; i < results.size(); i++) {
            eqs[i]      = results.get(i)[0];
            corrects[i] = results.get(i)[1];
            marks[i]    = results.get(i)[2];
            exps[i]     = i < exercises.size() ? exercises.get(i).getExplanation() : "";
        }

        Intent intent = new Intent(this, FeedbackActivity.class);
        intent.putExtra(FeedbackActivity.EXTRA_BLOCK_ID, getIntent().getIntExtra(EXTRA_BLOCK_ID, 1));
        intent.putExtra(FeedbackActivity.EXTRA_LEVEL_ID, getIntent().getIntExtra(EXTRA_LEVEL_ID, 11));
        intent.putExtra(FeedbackActivity.EXTRA_EQUATIONS,    eqs);
        intent.putExtra(FeedbackActivity.EXTRA_CORRECTS,     corrects);
        intent.putExtra(FeedbackActivity.EXTRA_MARKS,        marks);
        intent.putExtra(FeedbackActivity.EXTRA_EXPLANATIONS, exps);
        intent.putExtra(FeedbackActivity.EXTRA_SCORE,        viewModel.getTotalScore());
        startActivity(intent);
        finish();
    }
}
