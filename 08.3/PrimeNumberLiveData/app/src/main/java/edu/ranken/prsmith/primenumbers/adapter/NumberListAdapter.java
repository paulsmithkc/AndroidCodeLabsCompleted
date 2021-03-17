package edu.ranken.prsmith.primenumbers.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.ranken.prsmith.primenumbers.R;

public class NumberListAdapter extends RecyclerView.Adapter<NumberViewHolder> {
    private List<Long> items;
    private Context context;
    private LayoutInflater layoutInflater;

    public NumberListAdapter(Context context, List<Long> items) {
        this.items = items;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setItems(List<Long> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    @NonNull
    @Override
    public NumberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_number, parent, false);

        NumberViewHolder vh = new NumberViewHolder(itemView);
        vh.text = (TextView)itemView;

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull NumberViewHolder vh, int position) {
        vh.text.setText(items.get(position).toString());
    }
}
