package edu.ranken.prsmith.primenumbers.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NumberViewHolder extends RecyclerView.ViewHolder {
    public TextView text;

    public NumberViewHolder(@NonNull View itemView) {
        super(itemView);
    }
}
