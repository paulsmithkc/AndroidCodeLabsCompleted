package edu.ranken.prsmith.echodemo.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import edu.ranken.prsmith.echodemo.MyApp;
import edu.ranken.prsmith.echodemo.model.EchoDataSource;
import edu.ranken.prsmith.echodemo.network.EchoResponse;
import retrofit2.Call;
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
            // create request
            Call<EchoResponse> call = dataSource.postEcho("MyWorker");

            // make synchronous request
            Response<EchoResponse> response = call.execute();

            // process response...
            EchoResponse responseBody = response.body();
            String output = String.format(
                "response: method=%s, message=%s, timestamp=%s",
                responseBody.method,
                responseBody.message,
                responseBody.timestamp);
            Log.i(LOG_TAG, output);

            // worker succeeded
            return Result.success();
        } catch (Exception ex) {
            // log errors
            Log.e(LOG_TAG, "error: " + ex.getMessage(), ex);

            // worker failed, retry later
            return Result.retry();
        }
    }
}
