package edu.ranken.prsmith.localbroadcasts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = "LocalBroadcasts";

    private TextView textView;
    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver receiver;
    private IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "MainActivity.onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.time_text);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long startTime = intent.getLongExtra(MyApplication.EXTRA_START_TIME, 0);
                long currentTime = intent.getLongExtra(MyApplication.EXTRA_CURRENT_TIME, 0);
                long elapsedTime = intent.getLongExtra(MyApplication.EXTRA_ELAPSED_TIME, 0);
                textView.setText(String.format(
                    "start %,d\ncurrent %,d\n%,dms elapsed",
                    startTime,
                    currentTime,
                    elapsedTime
                ));
            }
        };
        intentFilter = new IntentFilter();
        intentFilter.addAction(MyApplication.ACTION_TICK);
    }

    @Override
    protected void onStart() {
        Log.i(LOG_TAG, "MainActivity.onStart");
        super.onStart();
        localBroadcastManager.registerReceiver(receiver, intentFilter);

        Intent serviceIntent = new Intent(this, MyService.class);
        if (Build.VERSION.SDK_INT >= 26) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
    }

    @Override
    protected void onStop() {
        Log.i(LOG_TAG, "MainActivity.onStop");
        super.onStop();
        localBroadcastManager.unregisterReceiver(receiver);

        Intent serviceIntent = new Intent(this, MyService.class);
        stopService(serviceIntent);
    }
}
