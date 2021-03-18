package edu.ranken.prsmith.primenumberscodealong;

import android.app.Application;
import android.util.Log;

import androidx.work.Constraints;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

import edu.ranken.prsmith.primenumberscodealong.model.PrimesDataSource;
import edu.ranken.prsmith.primenumberscodealong.worker.FindPrimesWorker;

public class PrimesApp extends Application {
    public static final String LOG_TAG = "PrimeNumbers";
    public static final String JOB_NAME_FIND_PRIMES = "findPrimes";
    public static final String ACTION_FIND_PRIMES_START = "findPrimesStart";
    public static final String ACTION_FIND_PRIMES_STOP = "findPrimesStop";
    public static final String ACTION_PRIME_FOUND = "primeFound";
    public static final String EXTRA_CURRENT = "current";
    public static final String EXTRA_MAX = "max";

    private PrimesDataSource primesDataSource;

    @Override
    public void onCreate() {
        super.onCreate();

        // construct data source
        primesDataSource = new PrimesDataSource();

        // get work manager
        WorkManager workManager = WorkManager.getInstance(this);

        // enqueue worker

        Constraints findPrimesConstraints =
            new Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build();

        OneTimeWorkRequest findPrimesRequest =
            new OneTimeWorkRequest.Builder(FindPrimesWorker.class)
            .setConstraints(findPrimesConstraints)
            .setInitialDelay(1, TimeUnit.SECONDS)
            .build();

        workManager.enqueueUniqueWork(JOB_NAME_FIND_PRIMES, ExistingWorkPolicy.REPLACE, findPrimesRequest);

        Log.i(LOG_TAG, "find primes enqueued");
    }

    public PrimesDataSource getPrimesDataSource() {
        return primesDataSource;
    }
}
