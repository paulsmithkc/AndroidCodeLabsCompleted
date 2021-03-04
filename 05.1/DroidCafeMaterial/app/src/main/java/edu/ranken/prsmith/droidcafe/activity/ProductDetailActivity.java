package edu.ranken.prsmith.droidcafe.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Locale;

import edu.ranken.prsmith.droidcafe.DroidCafe;
import edu.ranken.prsmith.droidcafe.R;
import edu.ranken.prsmith.droidcafe.model.Product;
import edu.ranken.prsmith.droidcafe.model.ProductDataSource;

public class ProductDetailActivity extends AppCompatActivity {

    // views
    private ImageView imageView;
    private TextView nameView;
    private TextView descriptionView;
    private TextView priceView;

    // data
    private DroidCafe app;
    private String productId;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // find views
        imageView = findViewById(R.id.product_detail_image);
        nameView = findViewById(R.id.product_detail_name);
        descriptionView = findViewById(R.id.product_detail_description);
        priceView = findViewById(R.id.product_detail_price);

        // get data
        Intent intent = getIntent();
        app = (DroidCafe) getApplication();
        productId = intent.getStringExtra("productId");
        product = app.getProducts().getById(productId);

        // show data
        imageView.setImageResource(product.getImageResId());
        nameView.setText(product.getName());
        descriptionView.setText(product.getDescription());

        // format price
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        priceView.setText(currencyFormat.format(product.getPrice()));

        // change window title
        getSupportActionBar().setTitle(product.getName());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void onAddToCart(View view) {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
            this,
            Pair.create(imageView, "image"),
            Pair.create(nameView, "name"),
            Pair.create(descriptionView, "description"),
            Pair.create(priceView, "price")
        );

        Intent intent = new Intent(this, CartActivity.class);
        intent.setAction(CartActivity.ACTION_ADD_TO_CART);
        intent.putExtra("product", product);
        intent.putExtra("quantity", 1);
        startActivity(intent, options.toBundle());
    }
}
