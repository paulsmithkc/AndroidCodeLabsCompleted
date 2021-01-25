package edu.ranken.prsmith.lectureexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class EnterNameActivity extends AppCompatActivity {
    public static final String EXTRA_COUNT = "count2";
    public static final String EXTRA_NAME = "name2";

    private TextView countText;
    private EditText nameEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_name);

        countText = findViewById(R.id.enter_name_count_text);
        nameEdit = findViewById(R.id.enter_name_edit);

        Intent intent = getIntent();
        int count = intent.getIntExtra(EXTRA_COUNT, 0);
        countText.setText(Integer.toString(count));
    }

    public void saveName(View view) {
        String name = nameEdit.getText().toString();

        Intent replyIntent = new Intent();
        replyIntent.putExtra(EXTRA_NAME, name);
        setResult(RESULT_OK, replyIntent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
