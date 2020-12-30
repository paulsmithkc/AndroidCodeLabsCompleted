package edu.ranken.prsmith.hellotoastchallenge2;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private int _count;
    private TextView _countText;
    private Button _zeroButton;
    private Button _countButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _countText = findViewById(R.id.text_count);
        _countText.setText(String.valueOf(_count));
        _zeroButton = findViewById(R.id.button_zero);
        _countButton = findViewById(R.id.button_count);
    }

    public void showToast(View view) {
        Toast.makeText(this, R.string.toast_message, Toast.LENGTH_LONG).show();
    }

    public void setZero(View view) {
        _count = 0;
        _countText.setText(String.valueOf(_count));
        _zeroButton.setBackgroundColor(Color.GRAY);
        _countButton.setBackgroundColor(Color.BLUE);
    }

    public void countUp(View view) {
        ++_count;
        _countText.setText(String.valueOf(_count));
        _zeroButton.setBackgroundColor(Color.BLUE);
        _countButton.setBackgroundColor(_count % 2 == 0 ? Color.BLUE : Color.RED);
    }
}
