package com.androide.algebrago.feature.levels.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androide.algebrago.R;
import com.androide.algebrago.domain.models.Level;
import com.google.android.material.button.MaterialButton;

import java.util.List;

/**
 * Adaptador de RecyclerView para la pantalla de selección de niveles.
 *
 * Muestra cada {@link Level} como una tarjeta con nombre, descripción y estado,
 * alternando la posición horizontal (izquierda/derecha) para un efecto visual
 * en zigzag.
 *
 * Patrón UI: "Wizard steps" (Tidwell) — lista de pasos progresivos donde cada
 * ítem representa una etapa del aprendizaje.
 */
public class LevelAdapter extends RecyclerView.Adapter<LevelAdapter.LevelVH> {

    /**
     * Contrato de callback para notificar la selección de un nivel.
     */
    public interface OnLevelClick {
        /**
         * Se invoca cuando el usuario pulsa sobre un nivel.
         *
         * @param level nivel seleccionado.
         */
        void onClick(Level level);
    }

    private final List<Level> levels;
    private final OnLevelClick listener;

    /**
     * Construye el adaptador con la lista de niveles y el listener de selección.
     *
     * @param levels   lista de niveles a mostrar.
     * @param listener callback invocado al pulsar un nivel.
     */
    public LevelAdapter(List<Level> levels, OnLevelClick listener) {
        this.levels = levels;
        this.listener = listener;
    }

    /**
     * Infla el layout de la tarjeta de nivel y crea su ViewHolder.
     *
     * @param parent   grupo de vistas padre.
     * @param viewType tipo de vista (único en este adaptador).
     * @return nuevo {@link LevelVH} con la vista inflada.
     */
    @NonNull
    @Override
    public LevelVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_level_card, parent, false);
        return new LevelVH(v);
    }

    /**
     * Vincula los datos del nivel al ViewHolder en la posición indicada.
     * Aplica un desplazamiento alternado (zigzag) según la paridad de la posición.
     *
     * @param h   ViewHolder a rellenar.
     * @param pos posición del elemento en la lista.
     */
    @Override
    public void onBindViewHolder(@NonNull LevelVH h, int pos) {
        Level level = levels.get(pos);

        h.btnLevelCircle.setText(String.valueOf(pos + 1));

        h.tvName.setText("Nivel " + (pos + 1));

        h.tvDesc.setText(level.getDescription());

        h.tvStatus.setText("Nuevo");
        h.tvName.setText(level.getName());
        h.tvDesc.setText(level.getDescription());
        h.tvStatus.setText("Nuevo");

        float leftOffset = 24 * h.itemView.getResources().getDisplayMetrics().density;
        float rightOffset = 50 * h.itemView.getResources().getDisplayMetrics().density;

        if (pos % 2 == 0) {
            h.container.setTranslationX(-leftOffset);
        } else {
            h.container.setTranslationX(rightOffset);
        }

        h.itemView.setOnClickListener(v -> listener.onClick(level));
    }

    /**
     * @return número total de niveles en la lista.
     */
    @Override
    public int getItemCount() {
        return levels.size();
    }

    static class LevelVH extends RecyclerView.ViewHolder {
        MaterialButton btnLevelCircle;
        TextView tvName, tvDesc, tvStatus;
        View container;

        LevelVH(View v) {
            super(v);

            btnLevelCircle = v.findViewById(R.id.btn_level_circle);
            container = v.findViewById(R.id.container_level);
            tvName = v.findViewById(R.id.tv_level_name);
            tvDesc = v.findViewById(R.id.tv_level_desc);
            tvStatus = v.findViewById(R.id.tv_level_status);
        }
    }
}