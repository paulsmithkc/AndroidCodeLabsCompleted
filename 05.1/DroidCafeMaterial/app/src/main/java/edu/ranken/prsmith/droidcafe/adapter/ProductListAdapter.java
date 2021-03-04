package edu.ranken.prsmith.droidcafe.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import edu.ranken.prsmith.droidcafe.R;
import edu.ranken.prsmith.droidcafe.activity.ProductDetailActivity;
import edu.ranken.prsmith.droidcafe.model.Product;

public class ProductListAdapter extends RecyclerView.Adapter<ProductViewHolder> {

    private List<Product> items;
    private Activity context;
    private LayoutInflater inflater;

    public ProductListAdapter(Activity context, List<Product> items) {
        this.items = items;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setItems(List<Product> items) {
        this.items = items;
        this.notifyDataSetChanged();
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

        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product item = items.get(vh.getAdapterPosition());

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                    context,
                    Pair.create(vh.image, "image"),
                    Pair.create(vh.name, "name"),
                    Pair.create(vh.description, "description"),
                    Pair.create(vh.price, "price")
                );

                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("productId", item.getId());
                context.startActivity(intent, options.toBundle());
            }
        });

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder vh, int position) {
        final Product item = items.get(position);

        vh.image.setImageResource(item.getImageResId());
        vh.name.setText(item.getName());
        vh.description.setText(item.getDescription());

        NumberFormat priceFormat = NumberFormat.getCurrencyInstance(Locale.US);
        vh.price.setText(priceFormat.format(item.getPrice()));
    }
}
