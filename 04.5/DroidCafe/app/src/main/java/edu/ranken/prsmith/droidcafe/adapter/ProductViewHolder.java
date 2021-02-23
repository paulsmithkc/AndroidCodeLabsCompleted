package edu.ranken.prsmith.droidcafe.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductViewHolder extends RecyclerView.ViewHolder {
    public ImageView image;
    public TextView name;
    public TextView description;
    public TextView price;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
    }
}
