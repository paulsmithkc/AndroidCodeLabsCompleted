package edu.ranken.prsmith.droidcafe.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.NumberFormat;
import java.util.Locale;

import edu.ranken.prsmith.droidcafe.DroidCafe;
import edu.ranken.prsmith.droidcafe.R;
import edu.ranken.prsmith.droidcafe.model.Cart;
import edu.ranken.prsmith.droidcafe.model.Product;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView nameView;
    private TextView descriptionView;
    private TextView priceView;
    private Button addtoCartButton;

    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        imageView = findViewById(R.id.product_detail_image);
        nameView = findViewById(R.id.product_detail_name);
        descriptionView = findViewById(R.id.product_detail_description);
        priceView = findViewById(R.id.product_detail_price);
        addtoCartButton = findViewById(R.id.product_detail_add_to_cart);

        DroidCafe app = (DroidCafe) getApplication();
        Intent intent = getIntent();
        String productId = intent.getStringExtra("productId");
        product = app.getProducts().getById(productId);

        imageView.setImageResource(product.getImageResId());
        nameView.setText(product.getName());
        descriptionView.setText(product.getDescription());

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        priceView.setText(currencyFormat.format(product.getPrice()));

        getSupportActionBar().setTitle(product.getName());
    }

    public void onAddToCart(View view) {
        Intent intent = new Intent(this, CartActivity.class);
        intent.putExtra("product", product);
        intent.putExtra("quantity", 1);
        startActivity(intent);
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
}
