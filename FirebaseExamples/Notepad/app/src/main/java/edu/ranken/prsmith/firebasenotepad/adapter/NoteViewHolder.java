package edu.ranken.prsmith.firebasenotepad.adapter;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NoteViewHolder extends RecyclerView.ViewHolder {
    public TextView titleView;
    public TextView bodyView;
    public Button editButton;
    public Button deleteButton;

    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);
    }
}
