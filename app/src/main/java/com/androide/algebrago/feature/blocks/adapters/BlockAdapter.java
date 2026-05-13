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
 * PATTERN UI: Carousel / Horizontal Scrollable List (Tidwell).
 * Each block is a mini-screen card in horizontal RecyclerView.
 */
public class BlockAdapter extends RecyclerView.Adapter<BlockAdapter.BlockVH> {

    public interface OnBlockClick {
        void onClick(Block block);
    }

    private final List<Block> blocks;
    private final OnBlockClick listener;

    public BlockAdapter(List<Block> blocks, OnBlockClick listener) {
        this.blocks = blocks;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BlockVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_block_card, parent, false);
        return new BlockVH(v);
    }

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