package edu.ranken.prsmith.twoactivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_REPLY = "reply";

    private TextView _messageHeader;
    private TextView _messageText;
    private EditText _editReply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        _messageHeader = findViewById(R.id.mesage_header);
        _messageText = findViewById(R.id.message_text);
        _editReply = findViewById(R.id.edit_reply);

        Intent intent = getIntent();
        String message = intent.getStringExtra(EXTRA_MESSAGE);
        if (message != null && message.length() > 0) {
            _messageHeader.setText(R.string.message_received);
            _messageText.setText(message);
        } else {
            _messageHeader.setText("");
            _messageText.setText("");
        }
    }

    public void returnReply(View view) {
        Log.d(LOG_TAG, "Button clicked!");

        Intent replyIntent = new Intent();
        String reply = _editReply.getText().toString();
        replyIntent.putExtra(EXTRA_REPLY, reply);
        setResult(RESULT_OK, replyIntent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // Make the Home/Up button behave the same as the back button
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
