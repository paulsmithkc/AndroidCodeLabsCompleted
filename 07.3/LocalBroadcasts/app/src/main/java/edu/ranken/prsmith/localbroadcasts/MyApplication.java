package edu.ranken.prsmith.localbroadcasts;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class MyApplication extends Application {
    private static final String LOG_TAG = "LocalBroadcasts";
    public static final String ACTION_TICK = "edu.ranken.prsmith.localbroadcasts.ACTION_TICK";
    public static final String EXTRA_START_TIME = "startTime";
    public static final String EXTRA_CURRENT_TIME = "currentTime";
    public static final String EXTRA_ELAPSED_TIME = "elapsedTime";
    public static final String DEFAULT_NOTIFICATION_CHANNEL_ID = "defaultChannel";
    public static final int FOREGROUND_NOTIFICATION_ID = 1;

    @Override
    public void onCreate() {
        Log.i(LOG_TAG, "MyApplication.onCreate");
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationManager mgr = getSystemService(NotificationManager.class);

            NotificationChannel channel = new NotificationChannel(
                DEFAULT_NOTIFICATION_CHANNEL_ID,
                "Default Channel",
                NotificationManager.IMPORTANCE_MIN
            );
            channel.setDescription("This is the default notification channel");
            mgr.createNotificationChannel(channel);
        }
    }
}
