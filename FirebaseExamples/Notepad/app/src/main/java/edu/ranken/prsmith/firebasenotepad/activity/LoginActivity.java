package edu.ranken.prsmith.firebasenotepad.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import edu.ranken.prsmith.firebasenotepad.NotepadApp;
import edu.ranken.prsmith.firebasenotepad.R;

public class LoginActivity extends AppCompatActivity {
    private static final String LOG_TAG = LoginActivity.class.getSimpleName();
    private static final int REQUEST_AUTH = 1;

    // views
    private Button loginButton;

    // data
    private NotepadApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // find views
        loginButton = findViewById(R.id.login_button);

        // get app
        app = (NotepadApp) getApplication();

        // auto-login
        loginButton.performClick();
    }

    public void onLogin(View view) {
        Log.i(LOG_TAG, "Starting sign-in process");

        // check if user is already logged in
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            app.setUserId(user.getUid());
            gotoNoteList();
            return;
        } else {
            app.setUserId(null);
        }

        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build(),
            new AuthUI.IdpConfig.GitHubBuilder().build());

        // Create and launch sign-in intent
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.drawable.ic_launcher_foreground)  // use application logo
                .setTheme(R.style.Theme_FirebaseNotepad)  // use application theme
                .build(),
            REQUEST_AUTH);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_AUTH) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                // Sign in success
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                // Show a toast, for debugging
                String message = String.format("%s:%s:%s", user.getUid(), user.getEmail(), user.getDisplayName());
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();

                // save login info SharedPreferences
                app.setUserId(user.getUid());

                // navigate to note list
                gotoNoteList();

            } else {
                // Sign in failed
                Exception error = response.getError();
                String message = getString(R.string.generic_error, error.getMessage());
                Snackbar.make(loginButton, message, Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private void gotoNoteList() {
        Intent intent = new Intent(this, NoteListActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            // make the up button work the same as the back button
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
