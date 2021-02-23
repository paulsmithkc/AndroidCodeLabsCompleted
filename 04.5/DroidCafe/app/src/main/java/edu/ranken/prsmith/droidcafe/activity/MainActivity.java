package edu.ranken.prsmith.droidcafe.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.ranken.prsmith.droidcafe.DroidCafe;
import edu.ranken.prsmith.droidcafe.adapter.ProductListAdapter;
import edu.ranken.prsmith.droidcafe.R;
import edu.ranken.prsmith.droidcafe.model.Product;
import edu.ranken.prsmith.droidcafe.model.ProductDataSource;

public class MainActivity extends AppCompatActivity {

    private ProductDataSource productDataSource;
    private List<Product> productList;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DroidCafe app = (DroidCafe) getApplication();
        productDataSource = app.getProducts();
        //productDataSource = new ProductDataSource();

        recyclerView = findViewById(R.id.product_list);
        productList = productDataSource.getAll();
        ProductListAdapter adapter = new ProductListAdapter(this, productList);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        int gridColumns = getResources().getInteger(R.integer.grid_columns);
        recyclerView.setLayoutManager(new GridLayoutManager(this, gridColumns));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_order: {
                Intent intent = new Intent(this, CartActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.action_status:
                Toast.makeText(this, "Status", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_favorites:
                Toast.makeText(this, "Favorites", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_contact:
                Toast.makeText(this, "Contact", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    public void onDonut(View view) {
//        Toast.makeText(this, R.string.toast_add_cart_donut, Toast.LENGTH_SHORT).show();
//        orderItems.add(getString(R.string.toast_add_cart_donut));
//    }
//
//    public void onSandwich(View view) {
//        Toast.makeText(this, R.string.toast_add_cart_sandwich, Toast.LENGTH_SHORT).show();
//        orderItems.add(getString(R.string.toast_add_cart_sandwich));
//    }
//
//    public void onFroyo(View view) {
//        Toast.makeText(this, R.string.toast_add_cart_froyo, Toast.LENGTH_SHORT).show();
//        orderItems.add(getString(R.string.toast_add_cart_froyo));
//    }
//
//    public void onOrder(View view) {
//        String[] itemsArray = new String[orderItems.size()];
//        for (int i = 0; i < orderItems.size(); ++i) {
//            itemsArray[i] = orderItems.get(i);
//        }
//
//        Intent intent = new Intent(this, OrderActivity.class);
//        intent.putExtra(OrderActivity.EXTRA_ITEMS, itemsArray);
//        startActivity(intent);
//    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}
