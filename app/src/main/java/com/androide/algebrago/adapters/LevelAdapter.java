package com.androide.algebrago.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androide.algebrago.R;
import com.androide.algebrago.models.Level;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class LevelAdapter extends RecyclerView.Adapter<LevelAdapter.LevelVH> {

    public interface OnLevelClick {
        void onClick(Level level);
    }

    private final List<Level> levels;
    private final OnLevelClick listener;

    public LevelAdapter(List<Level> levels, OnLevelClick listener) {
        this.levels = levels;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LevelVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_level_card, parent, false);
        return new LevelVH(v);
    }

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