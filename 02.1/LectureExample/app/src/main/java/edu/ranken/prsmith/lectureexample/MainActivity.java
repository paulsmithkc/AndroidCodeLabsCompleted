package edu.ranken.prsmith.lectureexample;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_NAME = 3;

    private TextView countText;
    private TextView nameText;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countText = findViewById(R.id.count_text);
        nameText = findViewById(R.id.name_text);

        Intent intent = getIntent();
        count = intent.getIntExtra(EnterNameActivity.EXTRA_COUNT, 0);
        countText.setText(Integer.toString(count));

        int colorId = count % 2 == 0 ? R.color.even : R.color.odd;
        getWindow().setBackgroundDrawableResource(colorId);
    }

    public void incrementCount(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(EnterNameActivity.EXTRA_COUNT, count + 1);
        startActivity(intent);
    }

    public void promptName(View view) {
        Intent intent = new Intent(this, EnterNameActivity.class);
        intent.putExtra(EnterNameActivity.EXTRA_COUNT, count);
        startActivityForResult(intent, REQUEST_NAME);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_NAME && resultCode == RESULT_OK) {
            String name = data.getStringExtra(EnterNameActivity.EXTRA_NAME);
            nameText.setText(name);
        }
    }
}
