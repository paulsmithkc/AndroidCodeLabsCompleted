package edu.ranken.prsmith.simplefetch;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private EditText urlEdit;
    private TextView resultText;
    private ImageButton goButton;

    private FetchTask fetchTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find views
        urlEdit = findViewById(R.id.url_edit);
        resultText = findViewById(R.id.result_text);
        goButton = findViewById(R.id.go_button);

        // register event handlers
        urlEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                    actionId == EditorInfo.IME_ACTION_GO ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    onClickGo(urlEdit);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (fetchTask != null) {
            fetchTask.cancel(true);
        }
    }

    public void onClickGo(View view) {
        hideKeyboard(urlEdit);

        // cancel the previous task
        if (fetchTask != null) {
            fetchTask.cancel(true);
        }

        try {
            // parse the url
            URL url = new URL(urlEdit.getText().toString());
            // start the task
            fetchTask = new FetchTask(url);
            fetchTask.execute();
        } catch (MalformedURLException e) {
            resultText.setText("Please enter a valid URL.");
        }
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private class FetchTask extends AsyncTask<Void, Void, String> {

        private URL url;

        public FetchTask(URL url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            resultText.setText("Downloading...");
            goButton.setEnabled(false);
        }

        @Override
        protected String doInBackground(Void... params) {
            StringBuilder result = new StringBuilder();
            try {
                URLConnection connection = url.openConnection();
                try (InputStream stream = connection.getInputStream()) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            result.append(line).append('\n');
                        }
                    }
                }
            } catch (IOException ex) {
                Log.e(LOG_TAG, "Failed to download URL " + url, ex);
                result
                    .append("\nFailed to download URL ")
                    .append(url)
                    .append("\n\n")
                    .append(ex);
            }
            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            resultText.setText(result);
            goButton.setEnabled(true);
        }
    }
}
