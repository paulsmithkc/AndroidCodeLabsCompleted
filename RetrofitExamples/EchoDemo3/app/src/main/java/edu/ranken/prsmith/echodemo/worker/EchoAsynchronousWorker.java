package edu.ranken.prsmith.echodemo.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;

import com.google.common.util.concurrent.ListenableFuture;

import edu.ranken.prsmith.echodemo.MyApp;
import edu.ranken.prsmith.echodemo.model.EchoDataSource;
import edu.ranken.prsmith.echodemo.network.EchoResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EchoAsynchronousWorker extends ListenableWorker {
    private static final String LOG_TAG = EchoAsynchronousWorker.class.getSimpleName();

    private MyApp app;
    private EchoDataSource dataSource;

    public EchoAsynchronousWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
        app = (MyApp) context;
        dataSource = app.getEchoDataSource();
    }

    @NonNull
    @Override
    public ListenableFuture<Result> startWork() {
        return CallbackToFutureAdapter.getFuture(resolver -> {

            // create request
            Call<EchoResponse> call = dataSource.postEcho("MyWorker");

            // make asynchronous request
            call.enqueue(new Callback<EchoResponse>() {

                @Override
                public void onResponse(Call<EchoResponse> call, Response<EchoResponse> response) {

                    // process response...
                    EchoResponse responseBody = response.body();
                    String output = String.format(
                        "response: method=%s, message=%s, timestamp=%s",
                        responseBody.method,
                        responseBody.message,
                        responseBody.timestamp);
                    Log.i(LOG_TAG, output);

                    // worker succeeded
                    resolver.set(Result.success());
                }

                @Override
                public void onFailure(Call<EchoResponse> call, Throwable error) {
                    // log errors
                    Log.e(LOG_TAG, "error: " + error.getMessage(), error);

                    // worker failed, retry later
                    resolver.set(Result.retry());
                }
            });

            // return request
            return call;
        });
    }
}
