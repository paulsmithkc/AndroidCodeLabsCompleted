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

public class FindPrimesWorker extends Worker {

    private long cur;
    private long max;

    private LocalBroadcastManager localBroadcastManager;

    public FindPrimesWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);

        Data inputData = params.getInputData();
        cur = inputData.getLong(PrimesApp.EXTRA_CUR, 2);
        max = inputData.getLong(PrimesApp.EXTRA_MAX, 2);

        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    @NonNull
    @Override
    public Result doWork() {
        {
            // send broadcast that we are starting
            Intent intent = new Intent(PrimesApp.ACTION_PRIME_START);
            intent.putExtra(PrimesApp.EXTRA_PROGRESS, 0L);
            localBroadcastManager.sendBroadcast(intent);
        }

        for (; cur <= max && !isStopped(); ++cur) {
            // send broadcasts as primes are found
            if (isPrime(cur)) {
                // Log.i(PrimesApp.LOG_TAG, "prime found: " + cur);
                Intent intent = new Intent(PrimesApp.ACTION_PRIME_FOUND);
                intent.putExtra(PrimesApp.EXTRA_PROGRESS, 100L * cur / max);
                intent.putExtra(PrimesApp.EXTRA_PRIME, cur);
                localBroadcastManager.sendBroadcast(intent);
            }
        }

        if (cur > max) {
            Log.i(PrimesApp.LOG_TAG, "all primes found up to: " + max);

            // broadcast that we are done
            Intent intent = new Intent(PrimesApp.ACTION_PRIME_STOP);
            intent.putExtra(PrimesApp.EXTRA_PROGRESS, 100L);
            localBroadcastManager.sendBroadcast(intent);

            // success, job completed
            return Result.success();
        } else {
            Log.i(PrimesApp.LOG_TAG, "worker was stopped");

            // broadcast our progress
            Intent intent = new Intent(PrimesApp.ACTION_PRIME_STOP);
            intent.putExtra(PrimesApp.EXTRA_PROGRESS, 100L * cur / max);
            localBroadcastManager.sendBroadcast(intent);

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
