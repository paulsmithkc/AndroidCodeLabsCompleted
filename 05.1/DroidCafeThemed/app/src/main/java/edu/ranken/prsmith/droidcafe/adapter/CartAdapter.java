package edu.ranken.prsmith.droidcafe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.Locale;

import edu.ranken.prsmith.droidcafe.R;
import edu.ranken.prsmith.droidcafe.activity.CartActivity;
import edu.ranken.prsmith.droidcafe.model.Cart;
import edu.ranken.prsmith.droidcafe.model.CartItem;

public class CartAdapter extends RecyclerView.Adapter<CartItemViewHolder> {

    private Cart cart;
    private Context context;
    private LayoutInflater inflater;
    private OnQuantityChangeListener onQuantityChange;
    private OnDeleteListener onDelete;

    public CartAdapter(
        Context context,
        Cart cart,
        OnQuantityChangeListener onQuantityChange,
        OnDeleteListener onDelete) {

        this.cart = cart;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.onQuantityChange = onQuantityChange;
        this.onDelete = onDelete;
    }

    @Override
    public int getItemCount() {
        return cart != null ? cart.getItems().size() : 0;
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

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder vh, int position) {
        final CartItem item = cart.getItems().get(position);

        vh.imageView.setImageResource(item.getProductThumbnailResId());
        vh.nameView.setText(item.getProductName());
        vh.quantityView.setSelection(item.getQuantity() - 1, false);

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        float price = item.getProductPrice() * item.getQuantity();
        vh.priceView.setText(currencyFormat.format(price));

        vh.quantityView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 if (onQuantityChange != null) {
                     onQuantityChange.onChange(item, position + 1);
                 }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        vh.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDelete != null) {
                    onDelete.onDelete(item);
                }
            }
        });
    }
}
