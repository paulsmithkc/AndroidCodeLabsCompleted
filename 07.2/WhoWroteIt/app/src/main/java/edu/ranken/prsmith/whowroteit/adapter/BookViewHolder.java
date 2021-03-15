package edu.ranken.prsmith.whowroteit.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BookViewHolder extends RecyclerView.ViewHolder {
    public TextView titleView;
    public TextView subtitleView;
    public TextView authorView;

    public BookViewHolder(@NonNull View itemView) {
        super(itemView);
    }
}
