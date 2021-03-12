package edu.ranken.prsmith.localbroadcasts;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

public class MyApplication extends Application {
    private static final String LOG_TAG = "LocalBroadcasts";
    public static final String ACTION_TICK = "edu.ranken.prsmith.localbroadcasts.ACTION_TICK";
    public static final String EXTRA_START_TIME = "startTime";
    public static final String EXTRA_CURRENT_TIME = "currentTime";
    public static final String EXTRA_ELAPSED_TIME = "elapsedTime";

    @Override
    public void onCreate() {
        Log.i(LOG_TAG, "MyApplication.onCreate");
        super.onCreate();
        startService(new Intent(this, MyService.class));
    }
}
