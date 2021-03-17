package edu.ranken.prsmith.primenumbers;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

import edu.ranken.prsmith.primenumbers.worker.FindPrimesWorker;

public class PrimesApp extends Application {
    public static final String LOG_TAG = "PrimeNumbers";
    public static final String JOB_NAME_FIND_PRIMES = "findPrimes";
    public static final String ACTION_PRIME_START = "primeStart";
    public static final String ACTION_PRIME_FOUND = "primeFound";
    public static final String ACTION_PRIME_PROGRESS = "primeProgress";
    public static final String ACTION_PRIME_STOP = "primeStop";
    public static final String EXTRA_CUR = "cur";
    public static final String EXTRA_MAX = "max";
    public static final String EXTRA_PRIME = "prime";
    public static final String EXTRA_PROGRESS = "progress";

    private WorkManager workManager;

    @Override
    public void onCreate() {
        super.onCreate();
        workManager = WorkManager.getInstance(this);
        enqueueFindPrimes(2, 10_000_000, ExistingWorkPolicy.REPLACE);
    }

    private void enqueueFindPrimes(long cur, long max, ExistingWorkPolicy existingWorkPolicy) {
        Data findPrimesInput =
            new Data.Builder()
                .putLong(EXTRA_CUR, cur)
                .putLong(EXTRA_MAX, max)
                .build();

        Constraints findPrimesConstraints =
            new Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .build();

        OneTimeWorkRequest findPrimesRequest =
            new OneTimeWorkRequest.Builder(FindPrimesWorker.class)
                .setInputData(findPrimesInput)
                .setConstraints(findPrimesConstraints)
                .setInitialDelay(1, TimeUnit.SECONDS)
                .build();

        workManager.enqueueUniqueWork(
            JOB_NAME_FIND_PRIMES,
            existingWorkPolicy,
            findPrimesRequest
        );

        Log.i(LOG_TAG, "find primes enqueued " + cur + " to " + max);
    }
}
