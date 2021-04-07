package edu.ranken.prsmith.echodemo;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private EditText messageEdit;
    private TextView responseText;
    private RequestQueue requestQueue;
    private Request request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messageEdit = findViewById(R.id.message_edit);
        responseText = findViewById(R.id.response_text);

        requestQueue = Volley.newRequestQueue(this.getApplicationContext());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (request != null) {
            request.cancel();
        }
        if (requestQueue != null) {
            requestQueue.stop();
        }
    }

    public void onSend(View view) {
        if (request != null) {
            request.cancel();
        }

        String message = messageEdit.getText().toString();

        String url =
            Uri.parse("https://awd-example-services.herokuapp.com/api/echo")
               .buildUpon()
               .appendQueryParameter("message", message)
               .toString();

        Log.i(LOG_TAG, url);

        request = new JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            (JSONObject response) -> {
                Log.i(LOG_TAG, "response: " + response);
                String responseMessage = response.optString("message", "");
                String responseTimestamp = response.optString("timestamp", "");
                responseText.setText(String.format("Message: %s\nTimestamp: %s", responseMessage, responseTimestamp));
            },
            (VolleyError error) -> {
                Log.e(LOG_TAG, "error: " + error.getMessage(), error);
                responseText.setText(error.getMessage());
            }
        );

        requestQueue.add(request);
    }
}
