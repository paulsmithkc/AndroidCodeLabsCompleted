package edu.ranken.prsmith.activitylifecycle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = "ActivityLifecycle";

    private void showToast(String message) {
        message = "MainActivity." + message;
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        Log.d(LOG_TAG, message);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showToast(savedInstanceState != null ? "onCreate(bundle)" : "onCreate(null)");
    }

    @Override
    protected void onStart() {
        super.onStart();
        showToast("onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        showToast("onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        showToast("onPause(), is finishing: " + isFinishing());
    }

    @Override
    protected void onStop() {
        super.onStop();
        showToast("onStop(), is finishing: " + isFinishing());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        showToast("onDestroy(), is finishing: " + isFinishing());
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        showToast("onSaveInstanceState()");
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        showToast("onRestoreInstanceState()");
    }

    // button click handler
    public void goNext(View view) {
        startActivity(new Intent(this, SecondActivity.class));
    }
}
