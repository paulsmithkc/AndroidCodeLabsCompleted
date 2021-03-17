package edu.ranken.prsmith.primenumberscodealong.worker;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import edu.ranken.prsmith.primenumberscodealong.PrimesApp;

public class FindPrimesWorker extends Worker {

    private long current;
    private long max;
    private LocalBroadcastManager localBroadcastManager;

    public FindPrimesWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
        current = 2;
        max = 1_000_000;
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.i(PrimesApp.LOG_TAG, "started finding primes");
        {
            Intent intent = new Intent(PrimesApp.ACTION_FIND_PRIMES_START);
            intent.putExtra(PrimesApp.EXTRA_CURRENT, current);
            intent.putExtra(PrimesApp.EXTRA_MAX, max);
            localBroadcastManager.sendBroadcast(intent);
        }

        for (; current <= max && !isStopped(); ++current) {
            if (isPrime(current)) {
                //Log.i(PrimesApp.LOG_TAG, "found prime = " + current);

                Intent intent = new Intent(PrimesApp.ACTION_PRIME_FOUND);
                intent.putExtra(PrimesApp.EXTRA_CURRENT, current);
                intent.putExtra(PrimesApp.EXTRA_MAX, max);
                localBroadcastManager.sendBroadcast(intent);
            }
        }

        if (current > max) {
            Log.i(PrimesApp.LOG_TAG, "found all primes up to " + max);

            Intent intent = new Intent(PrimesApp.ACTION_FIND_PRIMES_STOP);
            intent.putExtra(PrimesApp.EXTRA_CURRENT, current);
            intent.putExtra(PrimesApp.EXTRA_MAX, max);
            localBroadcastManager.sendBroadcast(intent);

            // job done
            return Result.success();
        } else {
            Log.i(PrimesApp.LOG_TAG, "worker was canceled at " + current);

            Intent intent = new Intent(PrimesApp.ACTION_FIND_PRIMES_STOP);
            intent.putExtra(PrimesApp.EXTRA_CURRENT, current);
            intent.putExtra(PrimesApp.EXTRA_MAX, max);
            localBroadcastManager.sendBroadcast(intent);

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
