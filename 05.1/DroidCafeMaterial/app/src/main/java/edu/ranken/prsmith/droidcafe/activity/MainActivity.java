package edu.ranken.prsmith.droidcafe.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import edu.ranken.prsmith.droidcafe.DroidCafe;
import edu.ranken.prsmith.droidcafe.adapter.ProductListAdapter;
import edu.ranken.prsmith.droidcafe.R;
import edu.ranken.prsmith.droidcafe.model.Product;
import edu.ranken.prsmith.droidcafe.model.ProductDataSource;

public class MainActivity extends AppCompatActivity {

    // views
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;

    // data
    private DroidCafe app;
    private ProductDataSource productDataSource;
    private List<Product> productList;
    private ProductListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find views
        refreshLayout = findViewById(R.id.product_list_refresh);
        recyclerView = findViewById(R.id.product_list);

        // set layout manager
        int gridColumns = getResources().getInteger(R.integer.grid_columns);
        recyclerView.setLayoutManager(new GridLayoutManager(this, gridColumns));

        // get data and connect adapter
        app = (DroidCafe) getApplication();
        productDataSource = app.getProducts();
        productList = productDataSource.getAll();
        adapter = new ProductListAdapter(this, productList);
        recyclerView.setAdapter(adapter);

        // refresh layout
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh(refreshLayout);
            }
        });
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
            case R.id.action_cart: {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
                Intent intent = new Intent(this, CartActivity.class);
                startActivity(intent, options.toBundle());
                return true;
            }
            case R.id.action_refresh:
                refresh(refreshLayout);
                return true;
            case R.id.action_settings:
                Snackbar.make(recyclerView, R.string.action_settings, Snackbar.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void refresh(View view) {
        refreshLayout.setRefreshing(true);

        productList = productDataSource.getAll();
        adapter.setItems(productList);

        refreshLayout.setRefreshing(false);
        Snackbar.make(recyclerView, R.string.toast_refreshed, Snackbar.LENGTH_SHORT).show();
    }
}
