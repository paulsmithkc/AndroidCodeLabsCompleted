package edu.ranken.prsmith.droidcafe.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.text.NumberFormat;
import java.util.Locale;

import edu.ranken.prsmith.droidcafe.DroidCafe;
import edu.ranken.prsmith.droidcafe.R;
import edu.ranken.prsmith.droidcafe.adapter.CartAdapter;
import edu.ranken.prsmith.droidcafe.adapter.OnItemDeleteListener;
import edu.ranken.prsmith.droidcafe.adapter.OnQuantityChangeListener;
import edu.ranken.prsmith.droidcafe.model.Cart;
import edu.ranken.prsmith.droidcafe.model.CartItem;
import edu.ranken.prsmith.droidcafe.model.Product;

public class CartActivity extends AppCompatActivity {
    public static final String ACTION_ADD_TO_CART = "edu.ranken.prsmith.droidcafe.ADD_TO_CART";
    public static final int REQUEST_ORDER = 1;

    // views
    private RecyclerView itemsView;
    private TextView subtotalView;
    private TextView taxView;
    private TextView totalView;

    // data
    private DroidCafe app;
    private Cart cart;
    private CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // find views
        itemsView = findViewById(R.id.cart_items);
        subtotalView = findViewById(R.id.cart_subtotal);
        taxView = findViewById(R.id.cart_tax);
        totalView = findViewById(R.id.cart_total);

        // get cart
        app = (DroidCafe) getApplication();
        cart = app.getCart();

        // add item to cart
        Intent intent = getIntent();
        if (ACTION_ADD_TO_CART.equals(intent.getAction())) {
            Product product = (Product) intent.getSerializableExtra("product");
            int quantity = intent.getIntExtra("quantity", 0);
            if (product != null) {
                cart.addItem(new CartItem(product, quantity));
                Snackbar
                    .make(itemsView, R.string.toast_add_to_cart, Snackbar.LENGTH_SHORT)
                    .setAnchorView(R.id.cart_divider)
                    .show();
            }
        }

        // show cart
        adapter = new CartAdapter(
            this, cart,
            new OnQuantityChangeListener<CartItem>() {
                @Override
                public void onChange(CartItem item, int newQuantity) {
                    cart.updateItem(item.getProductId(), newQuantity);
                    adapter.notifyDataSetChanged();
                    updateTotals();
                    Snackbar
                        .make(itemsView, R.string.toast_quantity_updated, Snackbar.LENGTH_SHORT)
                        .setAnchorView(R.id.cart_divider)
                        .show();
                }
            },
            new OnItemDeleteListener<CartItem>() {
                @Override
                public void onDelete(CartItem item) {
                    removeItem(item);
                }
            }
        );
        itemsView.setLayoutManager(new LinearLayoutManager(this));
        itemsView.setAdapter(adapter);

        // show totals
        updateTotals();

        // card gestures
        ItemTouchHelper helper = new ItemTouchHelper(
            // 0000
            // 0001 UP = 1
            // 0010 DOWN = 2
            // 0100 LEFT = 4
            // 1000 RIGHT = 8
            // 0011 UP | DOWN
            // 1111 UP | DOWN | LEFT | RIGHT = 15

            new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                @Override
                public boolean onMove(
                    @NonNull RecyclerView recyclerView,
                    @NonNull RecyclerView.ViewHolder from,
                    @NonNull RecyclerView.ViewHolder to) {

                    int fromPos = from.getAdapterPosition();
                    int toPos = to.getAdapterPosition();
                    cart.swap(fromPos, toPos);
                    adapter.notifyItemMoved(fromPos, toPos);
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder vh, int direction) {
                    CartItem item = cart.getItem(vh.getAdapterPosition());
                    removeItem(item);
                }
            }
        );
        helper.attachToRecyclerView(itemsView);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        // refresh the data
        adapter.notifyDataSetChanged();
        updateTotals();
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

    private void updateTotals() {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);

        String subtotal = currencyFormat.format(cart.getSubtotal());
        String tax = currencyFormat.format(cart.getTaxAmount());
        String total = currencyFormat.format(cart.getTotal());

        subtotalView.setText(getString(R.string.cart_subtotal, subtotal));
        taxView.setText(getString(R.string.cart_tax, tax));
        totalView.setText(getString(R.string.cart_total, total));
    }

    private void removeItem(CartItem item) {
        cart.removeItem(item.getProductId());
        adapter.notifyDataSetChanged();
        updateTotals();
        Snackbar
            .make(itemsView, R.string.toast_removed_from_cart, Snackbar.LENGTH_SHORT)
            .setAnchorView(R.id.cart_divider)
            .show();
    }

    public void onPlaceOrder(View view) {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
        Intent intent = new Intent(this, OrderActivity.class);
        startActivityForResult(intent, REQUEST_ORDER, options.toBundle());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ORDER && resultCode == RESULT_OK) {
            cart.clear();
            adapter.notifyDataSetChanged();
            updateTotals();
            Snackbar
                .make(itemsView, R.string.purchase_toast_purchased, Snackbar.LENGTH_SHORT)
                .setAnchorView(R.id.cart_divider)
                .show();
        }
    }
}
