package edu.ranken.prsmith.primenumbers.worker;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import edu.ranken.prsmith.primenumbers.PrimesApp;
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
}
