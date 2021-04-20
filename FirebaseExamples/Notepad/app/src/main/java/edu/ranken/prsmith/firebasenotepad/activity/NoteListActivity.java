package edu.ranken.prsmith.firebasenotepad.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import edu.ranken.prsmith.firebasenotepad.NotepadApp;
import edu.ranken.prsmith.firebasenotepad.R;
import edu.ranken.prsmith.firebasenotepad.adapter.NoteListAdapter;
import edu.ranken.prsmith.firebasenotepad.model.Note;

public class NoteListActivity extends AppCompatActivity {
    private static final String LOG_TAG = NoteListActivity.class.getSimpleName();

    // views
    private RecyclerView recyclerView;

    // data
    private NotepadApp app;
    private String userId;
    private NoteListAdapter adapter;
    private List<Note> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        recyclerView = findViewById(R.id.note_list_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new NoteListAdapter(this);
        recyclerView.setAdapter(adapter);

        // get app data
        app = (NotepadApp) getApplication();
        userId = app.getUserId();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // get my notes from FireStore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("notes")
            .whereEqualTo("ownerUid", userId)
            .orderBy("title")
            .addSnapshotListener(this, (value, error) -> {
                if (error != null) {
                    String message = getString(R.string.generic_error, error.getMessage());
                    Snackbar.make(recyclerView, message, Snackbar.LENGTH_LONG).show();
                } else {
                    Log.i(LOG_TAG, "Notes Updated.");

                    items = value.toObjects(Note.class);
                    adapter.setItems(items);
                }
            });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            // make the up button work the same as the back button
            onBackPressed();
            return true;
        } else if (itemId == R.id.note_list_logout) {
            onLogout();
            return true;
        } else if (itemId == R.id.note_list_add) {
            onAdd();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        // ignore back button
        // to prevent the user from accidentally logging out
    }

    private void onLogout() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnSuccessListener(this, task -> {
                app.setUserId(null);
                finish();
            })
            .addOnFailureListener(this, error -> {
                String message = getString(R.string.generic_error, error.getMessage());
                Snackbar.make(recyclerView, message, Snackbar.LENGTH_LONG).show();
            });
    }

    private void onAdd() {
        Intent intent = new Intent(this, NoteEditActivity.class);
        startActivity(intent);
    }
}
