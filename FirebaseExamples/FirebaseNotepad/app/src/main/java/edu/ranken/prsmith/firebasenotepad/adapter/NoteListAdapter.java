package edu.ranken.prsmith.firebasenotepad.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import edu.ranken.prsmith.firebasenotepad.R;
import edu.ranken.prsmith.firebasenotepad.activity.NoteDetailActivity;
import edu.ranken.prsmith.firebasenotepad.activity.NoteEditActivity;
import edu.ranken.prsmith.firebasenotepad.model.Note;

public class NoteListAdapter extends RecyclerView.Adapter<NoteViewHolder> {
    private static final String LOG_TAG = NoteListAdapter.class.getSimpleName();
    private static final int SNIPPET_LENGTH = 40;

    private Activity activity;
    private LayoutInflater layoutInflater;
    private List<Note> items;

    public NoteListAdapter(Activity activity) {
        this.activity = activity;
        this.layoutInflater = LayoutInflater.from(activity);
        this.items = null;
    }

    public void setItems(List<Note> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_note, parent, false);
        NoteViewHolder holder = new NoteViewHolder(itemView);

        holder.titleView = itemView.findViewById(R.id.item_note_title);
        holder.bodyView = itemView.findViewById(R.id.item_note_body);
        holder.editButton = itemView.findViewById(R.id.item_note_edit);
        holder.deleteButton = itemView.findViewById(R.id.item_note_delete);

        itemView.setOnClickListener((View v) -> {
            int position = holder.getAdapterPosition();
            onView(holder, items.get(position));
        });
        holder.editButton.setOnClickListener((View v) -> {
            int position = holder.getAdapterPosition();
            onEdit(holder, items.get(position));
        });
        holder.deleteButton.setOnClickListener((View v) -> {
            int position = holder.getAdapterPosition();
            onDelete(holder, items.get(position));
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note item = items.get(position);

        // only show the first part of the note's body
        String snippet = item.body;
        if (snippet.length() > SNIPPET_LENGTH) {
            snippet = snippet.substring(0, SNIPPET_LENGTH) + "...";
        }

        holder.titleView.setText(item.title);
        holder.bodyView.setText(snippet);
    }

    private void onView(@NonNull NoteViewHolder holder, @NonNull Note item) {
        Intent intent = new Intent(activity, NoteDetailActivity.class);
        intent.putExtra(NoteDetailActivity.EXTRA_NOTE_ID, item.id);
        activity.startActivity(intent);
    }

    private void onEdit(@NonNull NoteViewHolder holder, @NonNull Note item) {
        Intent intent = new Intent(activity, NoteEditActivity.class);
        intent.putExtra(NoteEditActivity.EXTRA_NOTE_ID, item.id);
        activity.startActivity(intent);
    }

    private void onDelete(@NonNull NoteViewHolder holder, @NonNull Note item) {
        new AlertDialog.Builder(activity)
            .setMessage(R.string.note_detail_delete_message)
            .setPositiveButton(R.string.note_detail_delete_positive, (dialog, which) -> {
                performDelete(item);
            })
            .setNegativeButton(R.string.note_detail_delete_negative, (dialog, which) -> {
                // do nothing
            })
            .show();
    }

    private void performDelete(Note item) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("notes")
            .document(item.id)
            .delete()
            .addOnSuccessListener(activity, task -> {
                items.remove(item);
            })
            .addOnFailureListener(activity, error -> {
                Log.e(LOG_TAG, "error: " + error.getMessage(), error);
            });
    }
}
