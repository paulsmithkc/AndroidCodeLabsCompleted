package edu.ranken.prsmith.threebuttons;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {
    private static final String LOG_TAG = SecondActivity.class.getSimpleName();
    public static final String EXTRA_PASSAGE = "passage";

    private TextView _passageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        _passageText = findViewById(R.id.passage_text);

        Intent intent = getIntent();
        String passage = intent.getStringExtra(EXTRA_PASSAGE);
        _passageText.setText(passage);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // Make the Home/Up button behave the same as the back button
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
