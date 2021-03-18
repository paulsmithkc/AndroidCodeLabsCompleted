package edu.ranken.prsmith.imageupload.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import edu.ranken.prsmith.imageupload.BuildConfig;

/**
 *
 * @see <a href="https://developer.android.com/reference/java/net/HttpURLConnection">HttpURLConnection</a>
 * @see <a href="https://api.imgur.com/endpoints/image/">Imgur API</a>
 * @see <a href="https://api.imgur.com/oauth2/addclient">Imgur: Register and Application</a>
 * @see <a href="https://zh-wang.github.io/blog/2015/01/28/upload-image-with-imgur-api-on-android/">Upload Pics by Imgur Api on Android</a>
 * @see <a href="https://zh-wang.github.io/blog/2015/01/28/upload-image-with-imgur-api-on-android/">Storing Secret Keys in Android</a>
 *
 */
public class UploadWorker extends Worker {
    private static final String LOG_TAG = "ImageUpload";
    public static final String PARAM_SOURCE = "source";
    public static final String PARAM_DESTINATION = "destination";

    private String _source;
    private String _destination;

    public UploadWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);

        Data inputData = params.getInputData();
        _source = inputData.getString(PARAM_SOURCE);
        //destination = inputData.getString(PARAM_DESTINATION);
        _destination = "https://api.imgur.com/3/upload";
    }

    @NonNull
    @Override
    public Result doWork() {
        HttpURLConnection connection = null;
        try {
            // parse paths
            File sourceFile = new File(_source);
            URL destinationURL = new URL(_destination);

            // open connection
            connection = (HttpURLConnection) destinationURL.openConnection();
            connection.setConnectTimeout(60000);
            connection.setReadTimeout(15000);
            connection.setDoOutput(true);
            connection.setChunkedStreamingMode(0);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Client-ID " + BuildConfig.IMGUR_CLIENT_ID);
            //connection.setRequestProperty("Content-Type", "multipart/form-data");
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
                    // flush any straggling bits
                    out.flush();
                }
            }

            Log.i(LOG_TAG, "Image uploaded to " + _destination);

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
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
