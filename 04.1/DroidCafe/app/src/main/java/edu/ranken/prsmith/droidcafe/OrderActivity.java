package edu.ranken.prsmith.droidcafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class OrderActivity extends AppCompatActivity {
    public static final String EXTRA_ITEMS = "items";
    private TextView orderText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        orderText = findViewById(R.id.order_text);

        Intent intent = getIntent();
        String[] items = intent.getStringArrayExtra(EXTRA_ITEMS);
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < items.length; ++i) {
            result.append(items[i]).append('\n');
        }
        orderText.setText(result);
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
