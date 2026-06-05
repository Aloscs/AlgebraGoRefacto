package com.androide.algebrago.feature.blocks.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androide.algebrago.R;
import com.androide.algebrago.domain.models.Block;
import com.google.android.material.button.MaterialButton;

import java.util.List;

/**
 * Adaptador de RecyclerView para la pantalla de selección de bloques temáticos.
 *
 * PATRÓN UI: Carrusel / Lista horizontal desplazable (Tidwell).
 * Cada bloque se presenta como una tarjeta a pantalla completa en un
 * RecyclerView horizontal con efecto de escala al desplazar.
 */
public class BlockAdapter extends RecyclerView.Adapter<BlockAdapter.BlockVH> {

    /**
     * Contrato de callback para notificar la selección de un bloque.
     */
    public interface OnBlockClick {
        /**
         * Se invoca cuando el usuario pulsa sobre un bloque.
         *
         * @param block bloque seleccionado.
         */
        void onClick(Block block);
    }

    private final List<Block> blocks;
    private final OnBlockClick listener;

    /**
     * Construye el adaptador con la lista de bloques y el listener de selección.
     *
     * @param blocks   lista de bloques a mostrar.
     * @param listener callback invocado al pulsar un bloque.
     */
    public BlockAdapter(List<Block> blocks, OnBlockClick listener) {
        this.blocks = blocks;
        this.listener = listener;
    }

    /**
     * Infla el layout de la tarjeta de bloque y crea su ViewHolder.
     *
     * @param parent   grupo de vistas padre.
     * @param viewType tipo de vista (único en este adaptador).
     * @return nuevo {@link BlockVH} con la vista inflada.
     */
    @NonNull
    @Override
    public BlockVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_block_card, parent, false);
        return new BlockVH(v);
    }

    /**
     * Vincula los datos del bloque al ViewHolder en la posición indicada.
     * Actualiza los segmentos de progreso visual según el porcentaje del bloque.
     *
     * @param h   ViewHolder a rellenar.
     * @param pos posición del elemento en la lista.
     */
    @Override
    public void onBindViewHolder(@NonNull BlockVH h, int pos) {
        Block b = blocks.get(pos);

        h.tvNumber.setText(String.format("#%02d", b.getId()));
        h.tvTitle.setText(b.getName());
        h.tvDescription.setText(b.getTopic());

        // Progreso del bloque.
        // Si tu modelo Block ya tiene getProgressPercent(), esto funciona directo.
        // Si te marca error, dime qué métodos tiene tu clase Block y lo ajustamos.

        int percent = b.getProgressPercent();


        int filledSegments = Math.round((percent / 100f) * h.progressSegments.length);

        for (int i = 0; i < h.progressSegments.length; i++) {
            if (i < filledSegments) {
                h.progressSegments[i].setBackgroundResource(R.drawable.bg_loading_segment_fill);
            } else {
                h.progressSegments[i].setBackgroundResource(R.drawable.bg_loading_segment_empty);
            }
        }

        h.btnOpen.setOnClickListener(v -> listener.onClick(b));
        h.itemView.setOnClickListener(v -> listener.onClick(b));
    }

    /**
     * @return número total de bloques en la lista.
     */
    @Override
    public int getItemCount() {
        return blocks.size();
    }

    static class BlockVH extends RecyclerView.ViewHolder {

        TextView tvNumber, tvTitle, tvDescription;
        MaterialButton btnOpen;
        View[] progressSegments;

        BlockVH(View v) {
            super(v);

            tvNumber = v.findViewById(R.id.tv_block_number);
            tvTitle = v.findViewById(R.id.tv_block_title);
            tvDescription = v.findViewById(R.id.tv_block_description);
            btnOpen = v.findViewById(R.id.btn_open_block);

            progressSegments = new View[]{
                    v.findViewById(R.id.progress_seg_1),
                    v.findViewById(R.id.progress_seg_2),
                    v.findViewById(R.id.progress_seg_3),
                    v.findViewById(R.id.progress_seg_4),
                    v.findViewById(R.id.progress_seg_5),
                    v.findViewById(R.id.progress_seg_6)
            };
        }
    }
}