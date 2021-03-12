package edu.ranken.prsmith.localbroadcasts;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class MyService extends Service {
    private static final String LOG_TAG = "LocalBroadcasts";

    private LocalBroadcastManager localBroadcastManager;
    private WorkerThread worker;

    @Override
    public void onCreate() {
        Log.i(LOG_TAG, "MyService.onCreate");
        super.onCreate();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(LOG_TAG, "MyService.onStartCommand");
        if (worker != null) {
            worker.cancelled = true;
            worker.interrupt();
        }
        worker = new WorkerThread();
        worker.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i(LOG_TAG, "MyService.onDestroy");
        super.onDestroy();
        if (worker != null) {
            worker.cancelled = true;
            worker.interrupt();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // intentionally empty, not used
        return null;
    }

    private class WorkerThread extends Thread {
        private boolean cancelled = false;

        public WorkerThread() {}

        @Override
        public void run() {
            long startTime = SystemClock.uptimeMillis();
            while (!cancelled) {
                try {
                    Thread.sleep(1000);
                    long currentTime = SystemClock.uptimeMillis();

                    Intent intent = new Intent(MyApplication.ACTION_TICK);
                    intent.putExtra(MyApplication.EXTRA_START_TIME, startTime);
                    intent.putExtra(MyApplication.EXTRA_CURRENT_TIME, currentTime);
                    intent.putExtra(MyApplication.EXTRA_ELAPSED_TIME, currentTime - startTime);
                    localBroadcastManager.sendBroadcast(intent);

                } catch (InterruptedException e) {
                    // worker may have been stopped
                }
            }
        }
    };
}
