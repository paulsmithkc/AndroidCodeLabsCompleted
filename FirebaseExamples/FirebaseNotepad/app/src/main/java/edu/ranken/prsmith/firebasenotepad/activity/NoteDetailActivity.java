package edu.ranken.prsmith.firebasenotepad.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;

import edu.ranken.prsmith.firebasenotepad.NotepadApp;
import edu.ranken.prsmith.firebasenotepad.R;
import edu.ranken.prsmith.firebasenotepad.model.Note;

public class NoteDetailActivity extends AppCompatActivity {
    private static final String LOG_TAG = NoteDetailActivity.class.getSimpleName();
    public static final String EXTRA_NOTE_ID = "noteId";

    // views
    private TextView titleView;
    private TextView bodyView;

    // data
    private NotepadApp app;
    private String userId;
    private String noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        // find views
        titleView = findViewById(R.id.note_detail_title);
        bodyView = findViewById(R.id.note_detail_body);

        // get app data
        app = (NotepadApp) getApplication();
        userId = app.getUserId();

        // get intent data
        Intent intent = getIntent();
        noteId = intent.getStringExtra(EXTRA_NOTE_ID);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // get note from FireStore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("notes")
            .document(noteId)
            .addSnapshotListener(this, (value, error) -> {
                if (error != null) {
                    showError(error);
                } else {
                    Log.i(LOG_TAG, "Note Updated.");

                    Note note = value.toObject(Note.class);
                    if (note != null) {
                        titleView.setText(note.title);
                        bodyView.setText(note.body);
                    } else {
                        Snackbar.make(titleView, R.string.get_note_error, Snackbar.LENGTH_LONG).show();
                    }
                }
            });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            // make the up button work the same as the back button
            onBackPressed();
            return true;
        } else if (itemId == R.id.note_detail_edit) {
            onEdit();
            return true;
        } else if (itemId == R.id.note_detail_delete) {
            onDelete();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void onEdit() {
        Intent intent = new Intent(this, NoteEditActivity.class);
        intent.putExtra(NoteEditActivity.EXTRA_NOTE_ID, noteId);
        startActivity(intent);
    }

    private void onDelete() {
        new AlertDialog.Builder(this)
            .setMessage(R.string.note_detail_delete_message)
            .setPositiveButton(R.string.note_detail_delete_positive, (dialog, which) -> {
                performDelete();
            })
            .setNegativeButton(R.string.note_detail_delete_negative, (dialog, which) -> {
                // do nothing
            })
            .show();
    }

    private void performDelete() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("notes")
            .document(noteId)
            .delete()
            .addOnSuccessListener(this, task -> {
                // return to previous activity
                finish();
            })
            .addOnFailureListener(this, this::showError);
    }

    private void showError(Exception error) {
        String message = getString(R.string.generic_error, error.getMessage());
        Snackbar.make(titleView, message, Snackbar.LENGTH_LONG).show();
    }
}
