package edu.ranken.prsmith.imageupload.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UploadWorker extends Worker {
    private static final String LOG_TAG = "ImageUpload";
    public static final String PARAM_SOURCE = "source";
    public static final String PARAM_DESTINATION = "destination";

    private String source;
    private String destination;

    public UploadWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);

        Data inputData = params.getInputData();
        source = inputData.getString(PARAM_SOURCE);
        destination = inputData.getString(PARAM_DESTINATION);
    }

    @NonNull
    @Override
    public Result doWork() {
        HttpURLConnection connection = null;
        try {
            // parse paths
            File sourceFile = new File(source);
            URL destinationURL = new URL(destination);

            // open connection
            connection = (HttpURLConnection) destinationURL.openConnection();
            connection.setConnectTimeout(60000);
            connection.setReadTimeout(15000);
            connection.setDoOutput(true);
            connection.connect();

            // send data
            try (BufferedOutputStream out = new BufferedOutputStream(connection.getOutputStream())) {
                try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(sourceFile))) {
                    byte[] buffer = new byte[1024];
                    int chunk;
                    // read chunks from the input stream
                    // -1 signals that file has been read completely
                    while ((chunk = in.read(buffer)) >= 0) {
                        // write chunks to the output stream
                        out.write(buffer, 0, chunk);
                    }
                }
            }

            // upload is complete
            return Result.success();
        } catch (MalformedURLException ex) {
            Log.e(LOG_TAG, "Failed to upload file, failing", ex);
            return Result.failure();
        } catch (IOException ex) {
            Log.e(LOG_TAG, "Failed to upload file, retrying", ex);
            return Result.retry();
        } catch (Exception ex) {
            Log.e(LOG_TAG, "Failed to upload file, failing", ex);
            return Result.failure();
        } finally {
            connection.disconnect();
        }
    }
}
