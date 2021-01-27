package edu.ranken.prsmith.activitylifecycle;

import android.app.Application;
import android.util.Log;

public class MyApplication extends Application {
    private static final String LOG_TAG = "ActivityLifecycle";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "Application.onCreate()");
    }
}
