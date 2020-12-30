package edu.ranken.prsmith.hellotoastchallenge;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private int _count;
    private TextView _countText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _countText = findViewById(R.id.text_count);
        _countText.setText(String.valueOf(_count));
    }

    public void showToast(View view) {
        Toast.makeText(this, R.string.toast_message, Toast.LENGTH_LONG).show();
    }

    public void countUp(View view) {
        ++_count;
        _countText.setText(String.valueOf(_count));
    }
}
