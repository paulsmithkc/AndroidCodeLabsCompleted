package edu.ranken.prsmith.globalreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class GlobalReceiver extends BroadcastReceiver {
    private static final String TAG = GlobalReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, intent.getAction());
        // your code here ...
        try {
            Toast.makeText(context, intent.getAction(), Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            // ignore exception
        }
    }
}
