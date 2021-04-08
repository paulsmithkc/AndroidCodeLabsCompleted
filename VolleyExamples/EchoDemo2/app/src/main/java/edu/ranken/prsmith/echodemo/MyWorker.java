package edu.ranken.prsmith.echodemo;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.google.common.util.concurrent.ListenableFuture;

public class MyWorker extends ListenableWorker {
    private static final String LOG_TAG = MyWorker.class.getSimpleName();

    private MyApp app;
    private RequestQueue requestQueue;

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
        app = (MyApp) context;
        requestQueue = app.getRequestQueue();
    }

    @NonNull
    @Override
    public ListenableFuture<Result> startWork() {
        return CallbackToFutureAdapter.getFuture(resolver -> {

            Request request = new PostEchoRequest(
                "MyWorker",
                (EchoApiResponse response) -> {

                    String output = String.format(
                        "response: method=%s, message=%s, timestamp=%s",
                        response.method,
                        response.message,
                        response.timestamp);
                    Log.i(LOG_TAG, output);

                    resolver.set(Result.success());
                },
                (VolleyError error) -> {
                    Log.e(LOG_TAG, "error: " + error.getMessage(), error);
                    resolver.set(Result.retry());
                }
            );

            requestQueue.add(request);
            return request;
        });
    }
}
