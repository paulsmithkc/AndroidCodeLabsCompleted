package edu.ranken.prsmith.droidcafe.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Locale;

import edu.ranken.prsmith.droidcafe.DroidCafe;
import edu.ranken.prsmith.droidcafe.R;
import edu.ranken.prsmith.droidcafe.adapter.CartAdapter;
import edu.ranken.prsmith.droidcafe.adapter.OnDeleteListener;
import edu.ranken.prsmith.droidcafe.adapter.OnQuantityChangeListener;
import edu.ranken.prsmith.droidcafe.model.Cart;
import edu.ranken.prsmith.droidcafe.model.CartItem;
import edu.ranken.prsmith.droidcafe.model.Product;

public class CartActivity extends AppCompatActivity {

    private RecyclerView itemsView;
    private TextView subtotalView;
    private TextView taxView;
    private TextView totalView;
    private Button placeOrderButton;

    private Cart cart;
    private CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        itemsView = findViewById(R.id.cart_items);
        subtotalView = findViewById(R.id.cart_subtotal);
        taxView = findViewById(R.id.cart_tax);
        totalView = findViewById(R.id.cart_total);
        placeOrderButton = findViewById(R.id.cart_place_order);

        DroidCafe app = (DroidCafe) getApplication();
        cart = app.getCart();

        Intent intent = getIntent();
        Product product = (Product)intent.getSerializableExtra("product");
        int quantity = intent.getIntExtra("quantity", 0);
        if (product != null) {
            cart.addItem(new CartItem(product, quantity));
        }

        adapter = new CartAdapter(
            this, cart,
            new OnQuantityChangeListener() {
                @Override
                public void onChange(CartItem item, int newQuantity) {
                    cart.updateItem(item.getProductId(), newQuantity);
                    adapter.notifyDataSetChanged();
                    updateTotals();
                }
            },
            new OnDeleteListener() {
                @Override
                public void onDelete(CartItem item) {
                    cart.removeItem(item.getProductId());
                    adapter.notifyDataSetChanged();
                    updateTotals();
                }
            }
        );
        itemsView.setLayoutManager(new LinearLayoutManager(this));
        itemsView.setAdapter(adapter);

        updateTotals();
    }

    private void updateTotals() {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);

        String subtotal = currencyFormat.format(cart.getSubtotal());
        String tax = currencyFormat.format(cart.getTaxAmount());
        String total = currencyFormat.format(cart.getTotal());

        subtotalView.setText(getString(R.string.cart_subtotal, subtotal));
        taxView.setText(getString(R.string.cart_tax, tax));
        totalView.setText(getString(R.string.cart_total, total));
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

    public void onPlaceOrder(View view) {
        startActivity(new Intent(this, OrderActivity.class));
    }
}
