package edu.ranken.prsmith.networkstatus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    // views
    private ImageView networkImage;
    private ImageView wifiImage;
    private TextView statusText;

    // other
    private BroadcastReceiver receiver;
    private IntentFilter intentFilter;
    private ConnectivityManager connectivityManager;
    private TelephonyManager telephonyManager;
    private WifiManager wifiManager;
    private PhoneStateListener phoneStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find views
        networkImage = findViewById(R.id.network_image);
        wifiImage = findViewById(R.id.wifi_image);
        statusText = findViewById(R.id.status_text);

        // create receiver and intent filter
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                onNetworkChanged(intent);
            }
        };
        intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);

        // create listeners/callbacks
        phoneStateListener = new PhoneStateListener() {
            @Override
            public void onSignalStrengthsChanged(SignalStrength signalStrength) {
                int level;
                if (Build.VERSION.SDK_INT >= 23) {
                    level = signalStrength.getLevel();
                } else {
                    level = 4;
                }
                networkImage.setImageLevel(level);
                Log.i(LOG_TAG, "Cell Signal = " + level);
            }
            @Override
            public void onDataConnectionStateChanged(int state) {
                if (state == TelephonyManager.DATA_CONNECTED) {
                    networkImage.setImageResource(R.drawable.network_strength);
                    Log.i(LOG_TAG, "Cell Connected");
                } else {
                    networkImage.setImageResource(R.drawable.network_strength_off_outline);
                    Log.i(LOG_TAG, "Cell State = " + state);
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();

        // get system services
        Context context = getApplicationContext();
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        // register
        registerReceiver(receiver, intentFilter);
        telephonyManager.listen(
            phoneStateListener,
            PhoneStateListener.LISTEN_SIGNAL_STRENGTHS | PhoneStateListener.LISTEN_DATA_CONNECTION_STATE
        );
    }

    @Override
    protected void onStop() {
        super.onStop();

        // unregister
        unregisterReceiver(receiver);
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
    }

    private void onNetworkChanged(Intent intent) {
        Context context = getApplicationContext();
        switch (intent.getAction()) {
            case ConnectivityManager.CONNECTIVITY_ACTION: {
                NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null && activeNetwork.isConnected();
                if (isConnected) {
                    statusText.setText("Network Connected.");
                } else {
                    statusText.setText("Network Disconnected.");
                }
                break;
            }
            case WifiManager.WIFI_STATE_CHANGED_ACTION: {
                int wifiState = wifiManager.getWifiState();
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                if (wifiState != WifiManager.WIFI_STATE_ENABLED) {
                    wifiImage.setImageResource(R.drawable.wifi_strength_off_outline);
                    wifiImage.setImageLevel(0);
                    Log.i(LOG_TAG, "Wifi State = " + wifiState);
                } else if (wifiInfo != null) {
                    int level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), 5);
                    wifiImage.setImageResource(R.drawable.wifi_strength);
                    wifiImage.setImageLevel(level);
                    Log.i(LOG_TAG, "Wifi Signal = " + level);
                } else {
                    wifiImage.setImageResource(R.drawable.wifi_strength);
                    wifiImage.setImageLevel(0);
                    Log.i(LOG_TAG, "Wifi Disconnected");
                }
                break;
            }
        }
    }
}
