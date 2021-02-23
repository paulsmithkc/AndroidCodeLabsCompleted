package edu.ranken.prsmith.droidcafe.adapter;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartItemViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageView;
    public Spinner quantityView;
    public TextView nameView;
    public TextView priceView;
    public ImageButton deleteButton;

    public CartItemViewHolder(@NonNull View itemView) {
        super(itemView);
    }
}
