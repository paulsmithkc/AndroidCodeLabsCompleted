package edu.ranken.prsmith.primenumberscodealong.worker;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import edu.ranken.prsmith.primenumberscodealong.PrimesApp;
import edu.ranken.prsmith.primenumberscodealong.activity.MainActivity;
import edu.ranken.prsmith.primenumberscodealong.model.PrimesDataSource;

public class FindPrimesWorker extends Worker {

    private PrimesApp app;
    private PrimesDataSource dataSource;
    private long current;
    private long max;

    public FindPrimesWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);

        app = (PrimesApp) context;
        dataSource = app.getPrimesDataSource();
        current = dataSource.getCurrentLiveData().getValue();
        max = dataSource.getMaxLiveData().getValue();
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.i(PrimesApp.LOG_TAG, "started finding primes");
        dataSource.setRunning(true);

        for (; current <= max && !isStopped(); ++current) {
            if (isPrime(current)) {
                //Log.i(PrimesApp.LOG_TAG, "found prime = " + current);
                dataSource.addPrime(current);
            }
        }

        // update front-end
        dataSource.setRunning(false);
        dataSource.setCurrent(current);

        if (current > max) {
            Log.i(PrimesApp.LOG_TAG, "found all primes up to " + max);

            // job done
            return Result.success();
        } else {
            Log.i(PrimesApp.LOG_TAG, "worker was canceled at " + current);

            // resume at a later time
            return Result.retry();
        }
    }

    private boolean isPrime(long number) {
        for (long i = 2; i < number; ++i) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }
}
