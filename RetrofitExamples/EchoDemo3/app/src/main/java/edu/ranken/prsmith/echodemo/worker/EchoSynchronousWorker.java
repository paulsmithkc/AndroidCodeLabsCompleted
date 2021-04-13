package edu.ranken.prsmith.echodemo.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.common.util.concurrent.ListenableFuture;

import edu.ranken.prsmith.echodemo.MyApp;
import edu.ranken.prsmith.echodemo.model.EchoDataSource;
import edu.ranken.prsmith.echodemo.network.EchoResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EchoSynchronousWorker extends Worker {
    private static final String LOG_TAG = EchoSynchronousWorker.class.getSimpleName();

    private MyApp app;
    private EchoDataSource dataSource;

    public EchoSynchronousWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
        app = (MyApp) context;
        dataSource = app.getEchoDataSource();
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            Call<EchoResponse> call = dataSource.postEcho("MyWorker");

            Response<EchoResponse> response = call.execute();

            EchoResponse responseBody = response.body();
            String output = String.format(
                "response: method=%s, message=%s, timestamp=%s",
                responseBody.method,
                responseBody.message,
                responseBody.timestamp);
            Log.i(LOG_TAG, output);

            return Result.success();
        } catch (Exception ex) {
            Log.e(LOG_TAG, "error: " + ex.getMessage(), ex);
            return Result.retry();
        }
    }
}
