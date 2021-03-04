package edu.ranken.prsmith.droidcafe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import edu.ranken.prsmith.droidcafe.R;
import edu.ranken.prsmith.droidcafe.model.Cart;
import edu.ranken.prsmith.droidcafe.model.CartItem;
import edu.ranken.prsmith.droidcafe.model.Product;

public class CartAdapter extends RecyclerView.Adapter<CartItemViewHolder> {

    private Cart cart;
    private Context context;
    private LayoutInflater inflater;
    private OnQuantityChangeListener<CartItem> onQuantityChange;
    private OnItemDeleteListener<CartItem> onDelete;

    public CartAdapter(
        Context context,
        Cart cart,
        OnQuantityChangeListener<CartItem> onQuantityChange,
        OnItemDeleteListener<CartItem> onDelete) {

        this.cart = cart;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.onQuantityChange = onQuantityChange;
        this.onDelete = onDelete;
    }

    public void setItems(Cart cart) {
        this.cart = cart;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return cart != null ? cart.getItemCount() : 0;
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_cart, parent, false);
        CartItemViewHolder vh = new CartItemViewHolder(itemView);

        vh.imageView = itemView.findViewById(R.id.item_cart_image);
        vh.quantityView = itemView.findViewById(R.id.item_cart_quantity);
        vh.nameView = itemView.findViewById(R.id.item_cart_name);
        vh.priceView = itemView.findViewById(R.id.item_cart_price);
        vh.deleteButton = itemView.findViewById(R.id.item_cart_delete);

        vh.quantityView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CartItem item = cart.getItem(vh.getAdapterPosition());
                int newQuantity = position + 1;
                if (onQuantityChange != null && item.getQuantity() != newQuantity) {
                    onQuantityChange.onChange(item, newQuantity);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        vh.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartItem item = cart.getItem(vh.getAdapterPosition());
                if (onDelete != null) {
                    onDelete.onDelete(item);
                }
            }
        });

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder vh, int position) {
        final CartItem item = cart.getItem(position);

        vh.imageView.setImageResource(item.getProductThumbnailResId());
        vh.nameView.setText(item.getProductName());
        vh.quantityView.setSelection(item.getQuantity() - 1, false);

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        float price = item.getProductPrice() * item.getQuantity();
        vh.priceView.setText(currencyFormat.format(price));
    }
}
