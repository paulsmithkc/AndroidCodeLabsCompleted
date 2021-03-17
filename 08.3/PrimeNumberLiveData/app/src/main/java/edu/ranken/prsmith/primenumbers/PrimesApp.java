package edu.ranken.prsmith.primenumbers;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

    private PrimesDataSource primesDataSource;
    private WorkManager workManager;

    @Override
    public void onCreate() {
        super.onCreate();
        primesDataSource = new PrimesDataSource();
        workManager = WorkManager.getInstance(this);
        enqueueFindPrimes(ExistingWorkPolicy.REPLACE);
    }

    public PrimesDataSource getPrimesDataSource() {
        return primesDataSource;
    }

    private void enqueueFindPrimes(ExistingWorkPolicy existingWorkPolicy) {

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
            existingWorkPolicy,
            findPrimesRequest
        );

        Log.i(LOG_TAG, "find primes enqueued");
    }
}
