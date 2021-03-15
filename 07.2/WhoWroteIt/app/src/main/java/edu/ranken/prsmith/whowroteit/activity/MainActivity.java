package edu.ranken.prsmith.whowroteit.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import edu.ranken.prsmith.whowroteit.R;
import edu.ranken.prsmith.whowroteit.WhoWroteIt;
import edu.ranken.prsmith.whowroteit.adapter.BookAdapter;
import edu.ranken.prsmith.whowroteit.model.AsyncTaskResult;
import edu.ranken.prsmith.whowroteit.model.Book;
import edu.ranken.prsmith.whowroteit.model.BookApiResponse;
import edu.ranken.prsmith.whowroteit.model.BookDataSource;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = "WhoWroteIt";

    // views
    private EditText queryEdit;
    private ImageButton searchButton;
    private RecyclerView recyclerView;

    // data
    private WhoWroteIt app;
    private BookDataSource bookDataSource;
    private SearchBooksTask task;
    private BookAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find views
        queryEdit = findViewById(R.id.query_edit);
        searchButton = findViewById(R.id.search_button);
        recyclerView = findViewById(R.id.search_results);

        // get data source
        app = (WhoWroteIt) getApplication();
        bookDataSource = app.getBookDataSource();
        adapter = new BookAdapter(this);

        // initialize recyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // register event handlers
        queryEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                    actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    onClickSearch(searchButton);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (task != null){
            task.cancel(true);
        }
    }

    public void onClickSearch(View view) {
        hideKeyboard(queryEdit);

        // cancel task
        if (task != null){
            task.cancel(true);
        }

        // start task
        String query = queryEdit.getText().toString();
        if (query.length() <= 0) {
            Snackbar.make(recyclerView, "Please enter a book title.", Snackbar.LENGTH_SHORT).show();
        } else if (!isConnected()) {
            Snackbar.make(recyclerView, "No network connection available.", Snackbar.LENGTH_SHORT).show();
        } else {
            task = new SearchBooksTask(query, 10);
            task.execute();
        }
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private boolean isConnected() {
        ConnectivityManager mgr = (ConnectivityManager) this.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = mgr.getActiveNetworkInfo();
        boolean isConnected = networkInfo != null && networkInfo.isConnected();
        return isConnected;
    }

    private class SearchBooksTask extends AsyncTask<Void, Void, AsyncTaskResult<BookApiResponse>> {

        private String query;
        private int maxResults;

        public SearchBooksTask(String query, int maxResults) {
            this.query = query;
            this.maxResults = maxResults;
        }

        @Override
        protected AsyncTaskResult<BookApiResponse> doInBackground(Void... params) {
            try {
                BookApiResponse result = bookDataSource.searchBooks(query, maxResults);
                return new AsyncTaskResult<>(result);
            } catch (Exception ex) {
                return new AsyncTaskResult<>(ex);
            }
        }

        @Override
        protected void onPostExecute(AsyncTaskResult<BookApiResponse> result) {
            if (result.error != null) {
                Log.e(LOG_TAG, "Failed to get books", result.error);
                Snackbar.make(recyclerView, "Failed to get books.", Snackbar.LENGTH_SHORT).show();
            } else {
                adapter.setResponse(result.result);
                Snackbar.make(recyclerView, "Done.", Snackbar.LENGTH_SHORT).show();
            }
        }
    }
}
