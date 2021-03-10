package edu.ranken.prsmith.primenumbers;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText limitEdit;
    private TextView resultsText;
    private ScrollView resultsScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        limitEdit = findViewById(R.id.limit_edit);
        resultsText = findViewById(R.id.results_text);
        resultsScrollView = findViewById(R.id.results_scroll_view);

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
    }

    public void onClickRun(View view) {
        hideKeyboard(limitEdit);
        resultsText.setText("Running...\n");

        // parse the limit
        int limit;
        try {
            limit = Integer.parseInt(limitEdit.getText().toString());
            if (limit < 1) {
                throw new NumberFormatException("limit must be at least one");
            }
        } catch (NumberFormatException ex) {
            resultsText.setText("Please enter a valid number.");
            return;
        }

        // find the prime numbers
        for (long i = 2; i <= limit; ++i) {
            if (isPrime(i)) {
                resultsText.append(i + "\n");
            }
        }

        resultsScrollView.scrollTo(0, resultsText.getHeight());
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
}
