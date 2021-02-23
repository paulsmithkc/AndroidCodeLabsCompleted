package edu.ranken.prsmith.droidcafe.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import edu.ranken.prsmith.droidcafe.R;
import edu.ranken.prsmith.droidcafe.activity.OrderActivity;
import edu.ranken.prsmith.droidcafe.activity.ProductDetailActivity;
import edu.ranken.prsmith.droidcafe.model.Product;

public class ProductListAdapter extends RecyclerView.Adapter<ProductViewHolder> {

    private List<Product> items;
    private Context context;
    private LayoutInflater inflater;

    public ProductListAdapter(Context context, List<Product> items) {
        this.items = items;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_product, parent, false);
        ProductViewHolder vh = new ProductViewHolder(itemView);

        vh.image = itemView.findViewById(R.id.item_product_image);
        vh.name = itemView.findViewById(R.id.item_product_name);
        vh.description = itemView.findViewById(R.id.item_product_description);
        vh.price = itemView.findViewById(R.id.item_product_price);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        final Product item = items.get(position);

        holder.image.setImageResource(item.getImageResId());
        holder.name.setText(item.getName());
        holder.description.setText(item.getDescription());

        NumberFormat priceFormat = NumberFormat.getCurrencyInstance(Locale.US);
        holder.price.setText(priceFormat.format(item.getPrice()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(context, item.getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("productId", item.getId());
                context.startActivity(intent);
            }
        });
    }
}
