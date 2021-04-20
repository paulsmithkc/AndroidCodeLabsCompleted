package edu.ranken.prsmith.firebasenotepad.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import edu.ranken.prsmith.firebasenotepad.NotepadApp;
import edu.ranken.prsmith.firebasenotepad.R;
import edu.ranken.prsmith.firebasenotepad.model.Note;

public class NoteEditActivity extends AppCompatActivity {
    private static final String LOG_TAG = NoteEditActivity.class.getSimpleName();
    public static final String EXTRA_NOTE_ID = "noteId";

    // views
    private EditText titleView;
    private EditText bodyView;

    // data
    private NotepadApp app;
    private String userId;
    private String noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);

        // find views
        titleView = findViewById(R.id.note_edit_title_input);
        bodyView = findViewById(R.id.note_edit_body_input);

        // get app data
        app = (NotepadApp) getApplication();
        userId = app.getUserId();

        // get intent data
        Intent intent = getIntent();
        noteId = intent.getStringExtra(EXTRA_NOTE_ID);

        // update action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(noteId != null ? R.string.note_edit_activity : R.string.note_add_activity);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // get note from FireStore
        if (noteId != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("notes")
                .document(noteId)
                .get()
                .addOnSuccessListener(task -> {
                    Log.i(LOG_TAG, "Note loaded.");
                    Note note = task.toObject(Note.class);
                    if (note != null) {
                        titleView.setText(note.title);
                        bodyView.setText(note.body);
                    } else {
                        Snackbar.make(titleView, R.string.get_note_error, Snackbar.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(this, this::showError);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            // make the up button work the same as the back button
            onBackPressed();
            return true;
        } else if (itemId == R.id.note_edit_save) {
            onSave();
            return true;
        } else if (itemId == R.id.note_edit_cancel) {
            onCancel();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        // ignore back button
        // to prevent the user from accidentally losing their data
    }

    private void onSave() {
        hideKeyboard(titleView);

        String title = titleView.getText().toString();
        String body = bodyView.getText().toString();

        if (title.length() == 0) {
            Snackbar.make(titleView, R.string.note_edit_title_required, Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (body.length() == 0) {
            Snackbar.make(titleView, R.string.note_edit_body_required, Snackbar.LENGTH_SHORT).show();
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FieldValue timestamp = FieldValue.serverTimestamp();

        Map<String, Object> data = new HashMap<>();
        data.put("title", title);
        data.put("body", body);
        data.put("updatedAt", timestamp);

        if (noteId != null) {
            db.collection("notes")
                .document(noteId)
                .update(data)
                .addOnSuccessListener(this, task -> {
                    // return to previous activity
                    finish();
                }).addOnFailureListener(this, this::showError);
        } else {
            data.put("ownerUid", userId);
            data.put("createAt", timestamp);

            db.collection("notes")
                .add(data)
                .addOnSuccessListener(this, task -> {
                    // return to previous activity
                    finish();
                }).addOnFailureListener(this, this::showError);
        }
    }

    private void onCancel() {
        hideKeyboard(titleView);

        new AlertDialog.Builder(this)
            .setMessage(R.string.note_edit_cancel_message)
            .setPositiveButton(R.string.note_edit_cancel_positive, (dialog, which) -> {
                // return to previous activity
                finish();
            })
            .setNegativeButton(R.string.note_edit_cancel_negative, (dialog, which) -> {
                // do nothing
            })
            .show();
    }

    private void showError(Exception error) {
        String message = getString(R.string.generic_error, error.getMessage());
        Snackbar.make(titleView, message, Snackbar.LENGTH_LONG).show();
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
