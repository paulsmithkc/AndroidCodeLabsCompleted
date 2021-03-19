package edu.ranken.prsmith.primenumbers;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import java.util.concurrent.TimeUnit;

import edu.ranken.prsmith.primenumbers.model.PrimesDataSource;
import edu.ranken.prsmith.primenumbers.worker.FindPrimesWorker;

public class PrimesApp extends Application {
    public static final String LOG_TAG = "PrimeNumbers";
    public static final String JOB_NAME_FIND_PRIMES = "findPrimes";
    public static final String DEFAULT_NOTIFICATION_CHANNEL_ID = "defaultChannel";
    public static final int FOREGROUND_NOTIFICATION_ID = 1;

    private PrimesDataSource primesDataSource;
    private WorkManager workManager;

    @Override
    public void onCreate() {
        super.onCreate();
        primesDataSource = new PrimesDataSource();
        workManager = WorkManager.getInstance(this);

        createNotificationChannel();
        enqueueFindPrimes();
    }

    public PrimesDataSource getPrimesDataSource() {
        return primesDataSource;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationManager mgr = getSystemService(NotificationManager.class);

            NotificationChannel channel = new NotificationChannel(
                DEFAULT_NOTIFICATION_CHANNEL_ID,
                getString(R.string.notification_default_channel_name),
                NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription(getString(R.string.notification_default_channel_description));

            mgr.createNotificationChannel(channel);
        }
    }

    private void enqueueFindPrimes() {

        Constraints findPrimesConstraints =
            new Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .build();

        OneTimeWorkRequest findPrimesRequest =
            new OneTimeWorkRequest.Builder(FindPrimesWorker.class)
                .setConstraints(findPrimesConstraints)
                .setInitialDelay(1, TimeUnit.SECONDS)
                .build();

        workManager.enqueueUniqueWork(
            JOB_NAME_FIND_PRIMES,
            ExistingWorkPolicy.REPLACE,
            findPrimesRequest
        );

        Log.i(LOG_TAG, "find primes enqueued");
    }
}
