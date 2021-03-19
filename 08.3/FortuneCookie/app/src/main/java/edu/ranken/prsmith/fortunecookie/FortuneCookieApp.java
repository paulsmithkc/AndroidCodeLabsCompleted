package edu.ranken.prsmith.fortunecookie;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

import edu.ranken.prsmith.fortunecookie.model.FortuneDataSource;
import edu.ranken.prsmith.fortunecookie.worker.GetFortuneWorker;

public class FortuneCookieApp extends Application {
    public static final String LOG_TAG = "FortuneCookie";
    public static final String JOB_NAME_WATCH_FORTUNE = "watchFortune";
    public static final String DEFAULT_NOTIFICATION_CHANNEL_ID = "defaultChannel";
    public static final int FORTUNE_NOTIFICATION_ID = 2;

    private FortuneDataSource fortuneDataSource;

    @Override
    public void onCreate() {
        super.onCreate();
        fortuneDataSource = new FortuneDataSource();

        createNotificationChannels();
        enqueueWorkers();
    }

    public FortuneDataSource getFortuneDataSource() {
        return fortuneDataSource;
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationManager mgr = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationChannel channel = new NotificationChannel(
                DEFAULT_NOTIFICATION_CHANNEL_ID,
                getString(R.string.notification_default_channel_name),
                NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription(getString(R.string.notification_default_channel_description));

            mgr.createNotificationChannel(channel);
        }
    }

    private void enqueueWorkers() {

        WorkManager workManager = WorkManager.getInstance(this);

        Constraints watchFortuneConstraints =
            new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        PeriodicWorkRequest watchFortuneRequest =
            new PeriodicWorkRequest.Builder(GetFortuneWorker.class, 15, TimeUnit.MINUTES)
                .setConstraints(watchFortuneConstraints)
                .setInitialDelay(1, TimeUnit.SECONDS)
                .build();

        workManager.enqueueUniquePeriodicWork(
            JOB_NAME_WATCH_FORTUNE,
            ExistingPeriodicWorkPolicy.REPLACE,
            watchFortuneRequest
        );

        Log.i(LOG_TAG, "watch fortune enqueued");
    }
}
