package edu.ranken.prsmith.primenumbers.worker;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.work.Data;
import androidx.work.ForegroundInfo;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import androidx.work.impl.utils.futures.SettableFuture;

import com.google.common.util.concurrent.ListenableFuture;

import edu.ranken.prsmith.primenumbers.PrimesApp;
import edu.ranken.prsmith.primenumbers.R;
import edu.ranken.prsmith.primenumbers.activity.MainActivity;
import edu.ranken.prsmith.primenumbers.model.PrimesDataSource;

public class FindPrimesWorker extends Worker {

    private PrimesApp app;
    private PrimesDataSource dataSource;
    private long cur;
    private long max;

    public FindPrimesWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);

        app = (PrimesApp) context;
        dataSource = app.getPrimesDataSource();
        cur = dataSource.getCurrentLiveData().getValue();
        max = dataSource.getMaxLiveData().getValue();
    }

    @NonNull
    @Override
    public Result doWork() {
        // show notification that worker is running
        // this will also allow the worker to continue for longer than 10 minutes
        showForegroundNotification(getApplicationContext());

        for (; cur <= max && !isStopped(); ++cur) {
            // search for primes
            if (isPrime(cur)) {
                // Log.i(PrimesApp.LOG_TAG, "prime found: " + cur);
                dataSource.addPrime(cur);
            }
        }

        if (cur > max) {
            Log.i(PrimesApp.LOG_TAG, "all primes found up to: " + max);

            // notify observers of progress
            dataSource.setCurrent(max);

            // show finished notification
            showFinishedNotification(getApplicationContext());

            // success, job completed
            return Result.success();
        } else {
            Log.i(PrimesApp.LOG_TAG, "worker was stopped");

            // notify observers of progress
            dataSource.setCurrent(cur);

            // worker was stopped, resume later
            return Result.retry();
        }
    }

    private boolean isPrime(long number) {
        long bound = (long) Math.ceil(Math.sqrt(number));
        for (long j = 2; j <= bound; ++j) {
            if (number % j == 0) {
                return false;
            }
        }
        return true;
    }

    private void showForegroundNotification(Context context) {
        // open MainActivity when the user taps on the notification
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // stop worker when the user taps on the stop action
        PendingIntent stopIntent =
            WorkManager.getInstance(context).createCancelPendingIntent(getId());

        Notification notification =
            new NotificationCompat.Builder(context, PrimesApp.DEFAULT_NOTIFICATION_CHANNEL_ID)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(context.getString(R.string.notification_foreground_text))
                .setSmallIcon(R.drawable.ic_search)
                .setContentIntent(contentIntent)
                .addAction(R.drawable.ic_stop, context.getString(R.string.notification_stop_action), stopIntent)
                .setOngoing(true)
                .build();

        setForegroundAsync(new ForegroundInfo(PrimesApp.FOREGROUND_NOTIFICATION_ID, notification));
    }

    private void showFinishedNotification(Context context) {
        // open MainActivity when the user taps on the notification
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification =
            new NotificationCompat.Builder(context, PrimesApp.DEFAULT_NOTIFICATION_CHANNEL_ID)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(context.getString(R.string.notification_finished_text))
                .setSmallIcon(R.drawable.ic_done)
                .setContentIntent(contentIntent)
                .build();

        NotificationManager mgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mgr.notify(PrimesApp.FINISHED_NOTIFICATION_ID, notification);
    }
}
