package edu.ranken.prsmith.fortunecookie.worker;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

import edu.ranken.prsmith.fortunecookie.FortuneCookieApp;
import edu.ranken.prsmith.fortunecookie.R;
import edu.ranken.prsmith.fortunecookie.activity.MainActivity;
import edu.ranken.prsmith.fortunecookie.model.Fortune;
import edu.ranken.prsmith.fortunecookie.model.FortuneDataSource;

public class GetFortuneWorker extends Worker {
    private static final String LOG_TAG = "FortuneCookie";
    private static final String BASE_URL = "https://awd-example-services.herokuapp.com/api/fortuneCookie";

    private FortuneCookieApp app;
    private FortuneDataSource dataSource;

    public GetFortuneWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
        app = (FortuneCookieApp) context;
        dataSource = app.getFortuneDataSource();
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.i(LOG_TAG, "checking for new fortune");
        HttpURLConnection connection = null;
        try {
            URL url = new URL(BASE_URL);

            // connect
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(60_000);
            connection.setReadTimeout(15_000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();

            // read and parse response
            try (InputStream stream = connection.getInputStream()) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
                    Gson gson = new Gson();
                    Fortune newFortune = gson.fromJson(reader, Fortune.class);

                    // is this a new fortune
                    Fortune oldFortune = dataSource.getFortune();
                    if (oldFortune == null) {
                        // first time reading the fortune
                        Log.i(LOG_TAG, "fortune initialized");
                        dataSource.setFortune(newFortune);
                    } else if (Objects.equals(newFortune.fortune, oldFortune.fortune)
                               && Objects.equals(newFortune.timestamp, oldFortune.timestamp)) {
                        // nothing changed
                        Log.i(LOG_TAG, "nothing new");
                        dataSource.setFortune(newFortune);
                    } else {
                        // new fortune, update and send notification
                        Log.i(LOG_TAG, "new fortune!");
                        dataSource.setFortune(newFortune);
                        showFortuneNotification(getApplicationContext(), newFortune);
                    }
                }
            }

            // success
            return Result.success();

        } catch (MalformedURLException ex) {
            Log.i(LOG_TAG, "error getting fortune, failed due to bad url", ex);
            return Result.failure();
        } catch (IOException ex) {
            Log.i(LOG_TAG, "error getting fortune, retrying due to IO error", ex);
            return Result.retry();
        } catch (Exception ex) {
            Log.i(LOG_TAG, "error getting fortune, failed for other reason", ex);
            return Result.failure();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private void showFortuneNotification(Context context, Fortune fortune) {
        // open MainActivity when the user taps on the notification
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // build notification
        Notification notification =
            new NotificationCompat.Builder(context, FortuneCookieApp.DEFAULT_NOTIFICATION_CHANNEL_ID)
                .setContentTitle(context.getString(R.string.notification_new_fortune))
                .setContentText(fortune.fortune)
                .setSmallIcon(R.drawable.fortune_cookie)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .build();

        // show notification now
        NotificationManager mgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mgr.notify(FortuneCookieApp.FORTUNE_NOTIFICATION_ID, notification);
    }
}
