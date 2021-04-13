package edu.ranken.prsmith.echodemo;

import android.app.Application;
import android.util.Log;

import androidx.work.Constraints;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.concurrent.TimeUnit;

public class MyApp extends Application {
    private static final String LOG_TAG = MyApp.class.getSimpleName();

    private RequestQueue requestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        requestQueue = Volley.newRequestQueue(this);
        scheduleWorkers();
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    private void scheduleWorkers() {
        WorkManager workManager = WorkManager.getInstance(this);

        Constraints workConstraints =
            new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        OneTimeWorkRequest workRequest =
            new OneTimeWorkRequest.Builder(MyWorker.class)
                .setConstraints(workConstraints)
                .setInitialDelay(1, TimeUnit.SECONDS)
                .build();

        workManager.enqueueUniqueWork("MyWorker", ExistingWorkPolicy.REPLACE, workRequest);

        Log.i(LOG_TAG, "scheduled workers");
    }
}
