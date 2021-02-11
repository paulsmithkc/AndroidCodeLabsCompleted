package edu.ranken.prsmith.droidcafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        items = new ArrayList<String>();
    }

    public void onDonut(View view) {
        Toast.makeText(this, R.string.toast_add_cart_donut, Toast.LENGTH_SHORT).show();
        items.add(getString(R.string.toast_add_cart_donut));
    }

    public void onSandwich(View view) {
        Toast.makeText(this, R.string.toast_add_cart_sandwich, Toast.LENGTH_SHORT).show();
        items.add(getString(R.string.toast_add_cart_sandwich));
    }

    public void onFroyo(View view) {
        Toast.makeText(this, R.string.toast_add_cart_froyo, Toast.LENGTH_SHORT).show();
        items.add(getString(R.string.toast_add_cart_froyo));
    }

    public void onOrder(View view) {
        String[] itemsArray = new String[items.size()];
        for (int i = 0; i < items.size(); ++i) {
            itemsArray[i] = items.get(i);
        }

        Intent intent = new Intent(this, OrderActivity.class);
        intent.putExtra(OrderActivity.EXTRA_ITEMS, itemsArray);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        String[] itemsArray = new String[items.size()];
        for (int i = 0; i < items.size(); ++i) {
            itemsArray[i] = items.get(i);
        }
        outState.putStringArray("items", itemsArray);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        String[] itemsArray = savedInstanceState.getStringArray("items");
        for (int i = 0; i < itemsArray.length; ++i) {
            items.add(itemsArray[i]);
        }
    }
}
