package edu.ranken.prsmith.echodemo;

import android.app.Application;
import android.util.Log;

import androidx.work.Constraints;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

import edu.ranken.prsmith.echodemo.model.EchoDataSource;
import edu.ranken.prsmith.echodemo.worker.EchoAsynchronousWorker;
import edu.ranken.prsmith.echodemo.worker.EchoSynchronousWorker;

public class MyApp extends Application {
    private static final String LOG_TAG = MyApp.class.getSimpleName();

    private EchoDataSource echoDataSource;

    @Override
    public void onCreate() {
        super.onCreate();
        echoDataSource = new EchoDataSource();
        scheduleWorkers();
    }

    public EchoDataSource getEchoDataSource() {
        return echoDataSource;
    }

    private void scheduleWorkers() {
        WorkManager workManager = WorkManager.getInstance(this);

        Constraints workConstraints =
            new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        OneTimeWorkRequest workRequest =
            new OneTimeWorkRequest.Builder(EchoAsynchronousWorker.class)
                .setConstraints(workConstraints)
                .setInitialDelay(1, TimeUnit.SECONDS)
                .build();

        OneTimeWorkRequest workRequest2 =
            new OneTimeWorkRequest.Builder(EchoSynchronousWorker.class)
                .setConstraints(workConstraints)
                .setInitialDelay(1, TimeUnit.SECONDS)
                .build();

        workManager.enqueueUniqueWork("MyWorker", ExistingWorkPolicy.REPLACE, workRequest);
        workManager.enqueueUniqueWork("MyWorker2", ExistingWorkPolicy.REPLACE, workRequest2);

        Log.i(LOG_TAG, "scheduled workers");
    }
}
