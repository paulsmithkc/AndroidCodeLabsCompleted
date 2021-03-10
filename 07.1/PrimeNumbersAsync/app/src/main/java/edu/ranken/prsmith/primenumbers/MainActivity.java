package edu.ranken.prsmith.primenumbers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // views
    private EditText limitEdit;
    private Button runButton;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;

    // data
    private ArrayList<Long> numbers;
    private NumberListAdapter adapter;
    private FindPrimesTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find views
        limitEdit = findViewById(R.id.limit_edit);
        runButton = findViewById(R.id.run_button);
        progressBar = findViewById(R.id.results_progress_bar);
        recyclerView = findViewById(R.id.results_recycler_view);

        // register event handlers
        limitEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    onClickRun(limitEdit);
                    return true;
                }
                return false;
            }
        });

        // setup recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        numbers = new ArrayList<Long>();
        adapter = new NumberListAdapter(this, numbers);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (task != null) {
            task.cancel(true);
        }
    }

    public void onClickRun(View view) {
        hideKeyboard(limitEdit);

        // cancel the previous task
        if (task != null) {
            task.cancel(true);
        }

        // parse the limit
        int limit;
        try {
            limit = Integer.parseInt(limitEdit.getText().toString());
            if (limit < 1) {
                throw new NumberFormatException("limit must be at least one");
            }
        } catch (NumberFormatException ex) {
            Snackbar.make(recyclerView, "Please enter a valid number.", Snackbar.LENGTH_SHORT).show();
            return;
        }

        // start the task
        task = new FindPrimesTask(limit);
        task.execute();
    }

    private boolean isPrime(long number) {
        long bound = (long)Math.ceil(Math.sqrt(number));
        for (long j = 2; j <= bound; ++j) {
            if (number % j == 0) {
                return false;
            }
        }
        return true;
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private class FindPrimesTask extends AsyncTask<Void, Long, Void> {

        private long limit;

        public FindPrimesTask(long limit) {
            this.limit = limit;
        }

        @Override
        protected void onPreExecute() {
            numbers.clear();
            adapter.notifyDataSetChanged();
            progressBar.setMax(100);
            progressBar.setProgress(0);
            runButton.setEnabled(false);
            Snackbar.make(recyclerView, "Running...", Snackbar.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            for (long i = 2; i <= limit && !isCancelled(); ++i) {
                if (isPrime(i)) {
                    numbers.add(i);
                }
                if (i % 1000 == 0) {
                    publishProgress(i);
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Long... progress) {
            adapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(numbers.size() - 1);
            progressBar.setProgress((int)(100 * progress[0] / limit));
        }

        @Override
        protected void onPostExecute(Void result) {
            if (!isCancelled()) {
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(numbers.size() - 1);
                progressBar.setProgress(100);
                runButton.setEnabled(true);
                Snackbar.make(recyclerView, "Done.", Snackbar.LENGTH_SHORT).show();
            }
        }
    }
}
