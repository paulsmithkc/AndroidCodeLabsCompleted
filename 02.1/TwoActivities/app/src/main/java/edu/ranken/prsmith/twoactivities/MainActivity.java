package edu.ranken.prsmith.twoactivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_REPLY = 1;

    private TextView _replyHeader;
    private TextView _replyText;
    private EditText _editMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _replyHeader = findViewById(R.id.reply_header);
        _replyText = findViewById(R.id.reply_text);
        _editMessage = findViewById(R.id.edit_message);
    }

    public void launchSecondActivity(View view) {
        Log.d(LOG_TAG, "Button clicked!");

        Intent intent = new Intent(this, SecondActivity.class);
        String message = _editMessage.getText().toString();
        intent.putExtra(SecondActivity.EXTRA_MESSAGE, message);
        startActivityForResult(intent, REQUEST_REPLY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_REPLY) {
            String reply =
                resultCode == RESULT_OK ?
                    data.getStringExtra(SecondActivity.EXTRA_REPLY) :
                    null;

            if (reply != null && reply.length() > 0) {
                _replyHeader.setText(R.string.reply_received);
                _replyText.setText(reply);
            } else {
                _replyHeader.setText("");
                _replyText.setText("");
            }
        }
    }
}
