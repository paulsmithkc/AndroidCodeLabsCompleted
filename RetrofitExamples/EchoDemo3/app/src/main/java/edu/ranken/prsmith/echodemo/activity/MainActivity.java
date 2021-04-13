package edu.ranken.prsmith.echodemo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import edu.ranken.prsmith.echodemo.model.EchoDataSource;
import edu.ranken.prsmith.echodemo.network.EchoResponse;
import edu.ranken.prsmith.echodemo.MyApp;
import edu.ranken.prsmith.echodemo.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private EditText messageEdit;
    private TextView responseText;

    private MyApp app;
    private EchoDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messageEdit = findViewById(R.id.message_edit);
        responseText = findViewById(R.id.response_text);

        app = (MyApp) getApplication();
        dataSource = app.getEchoDataSource();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onSend(View view) {
        hideKeyboard(this, messageEdit);

        String message = messageEdit.getText().toString();

        try {
            Call<EchoResponse> call = dataSource.postEcho(message);
            call.enqueue(new Callback<EchoResponse>() {

                @Override
                public void onResponse(Call<EchoResponse> call, Response<EchoResponse> response) {
                    EchoResponse responseBody = response.body();
                    responseText.setText(
                        String.format(
                            "Method: %s\nMessage: %s\nTimestamp: %s",
                            responseBody.method,
                            responseBody.message,
                            responseBody.timestamp
                        )
                    );
                }

                @Override
                public void onFailure(Call<EchoResponse> call, Throwable error) {
                    Log.e(LOG_TAG, "error: " + error.getMessage(), error);
                    responseText.setText(error.getMessage());
                }
            });
        } catch (Exception ex) {
            Log.e(LOG_TAG, "error: " + ex.getMessage(), ex);
            responseText.setText(ex.getMessage());
        }
    }

    public void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
