package edu.ranken.prsmith.threebuttons;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void showPassage(String passage) {
        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtra(SecondActivity.EXTRA_PASSAGE, passage);
        startActivity(intent);
    }

    public void showPassage1(View view) {
        showPassage(getString(R.string.passage1));
    }

    public void showPassage2(View view) {
        showPassage(getString(R.string.passage2));
    }

    public void showPassage3(View view) {
        showPassage(getString(R.string.passage3));
    }
}
