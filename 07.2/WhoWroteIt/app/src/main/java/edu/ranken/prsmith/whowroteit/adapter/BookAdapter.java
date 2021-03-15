package edu.ranken.prsmith.whowroteit.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.ranken.prsmith.whowroteit.R;
import edu.ranken.prsmith.whowroteit.model.Book;
import edu.ranken.prsmith.whowroteit.model.BookApiResponse;

public class BookAdapter extends RecyclerView.Adapter<BookViewHolder> {

    private BookApiResponse response;
    private Context context;
    private LayoutInflater layoutInflater;

    public BookAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setResponse(BookApiResponse response) {
        this.response = response;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return response != null ? response.items.size() : 0;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_book, parent, false);

        BookViewHolder vh = new BookViewHolder(itemView);
        vh.titleView = itemView.findViewById(R.id.item_book_title);
        vh.subtitleView = itemView.findViewById(R.id.item_book_subtitle);
        vh.authorView = itemView.findViewById(R.id.item_book_author);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder vh, int position) {
        Book book = response.items.get(position);
        String title = book.volumeInfo.title;
        String subtitle = book.volumeInfo.subtitle;
        String authors = join(book.volumeInfo.authors);

        vh.titleView.setText(title);
        vh.subtitleView.setText(subtitle);
        vh.authorView.setText(authors);

        vh.titleView.setVisibility(title != null && title.length() > 0 ? View.VISIBLE : View.GONE);
        vh.subtitleView.setVisibility(subtitle != null && subtitle.length() > 0 ? View.VISIBLE : View.GONE);
        vh.authorView.setVisibility(authors != null && authors.length() > 0 ? View.VISIBLE : View.GONE);
    }

    private String join(List<String> elements) {
        if (elements == null) {
          return "";
        } else if (Build.VERSION.SDK_INT >= 26) {
            return String.join(", ", elements);
        } else {
            StringBuilder sb = new StringBuilder();
            for (String element : elements) {
                sb.append(element).append(", ");
            }
            if (sb.length() > 0) {
                sb.setLength(sb.length() - 2);
            }
            return sb.toString();
        }
    }
}
