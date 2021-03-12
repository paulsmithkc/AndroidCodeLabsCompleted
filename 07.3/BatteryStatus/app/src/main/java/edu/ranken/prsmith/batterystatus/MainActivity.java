package edu.ranken.prsmith.batterystatus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    // views
    private ImageView imageView;
    private TextView textView;

    // other
    private BroadcastReceiver receiver;
    private IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find views
        imageView = findViewById(R.id.battery_status_image);
        textView = findViewById(R.id.battery_status_text);

        // create receiver and intent filter
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                onBatteryChanged(intent);
            }
        };
        intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
    }

    private void onBatteryChanged(Intent intent) {
        switch (intent.getAction()) {
            case Intent.ACTION_POWER_CONNECTED:
                Snackbar.make(imageView, "Connected.", Snackbar.LENGTH_SHORT).show();
                break;
            case Intent.ACTION_POWER_DISCONNECTED:
                Snackbar.make(imageView, "Disconnected.", Snackbar.LENGTH_SHORT).show();
                break;
            case Intent.ACTION_BATTERY_CHANGED:
                int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

                boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING;
                imageView.setImageResource(isCharging ? R.drawable.battery_charging : R.drawable.battery);
                imageView.setImageLevel(100 * level / scale);
                textView.setText(String.format(
                    "%2d%% %s",
                    100 * level / scale,
                    isCharging ? "charging" : ""
                ));
                break;
        }
    }
}
