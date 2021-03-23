package edu.ranken.prsmith.fortunecookie.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import edu.ranken.prsmith.fortunecookie.FortuneCookieApp;
import edu.ranken.prsmith.fortunecookie.R;
import edu.ranken.prsmith.fortunecookie.model.Fortune;
import edu.ranken.prsmith.fortunecookie.model.FortuneDataSource;
import edu.ranken.prsmith.fortunecookie.worker.GetFortuneWorker;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = "FortuneCookie";

    // views
    private TextView fortuneText;
    private TextView fortuneTime;

    // data
    private FortuneCookieApp app;
    private FortuneDataSource dataSource;
    private LiveData<Fortune> fortuneLiveData;

    // handler
    private final Handler handler = new Handler();
    private final Runnable updateTime = new Runnable() {
        @Override
        public void run() {
            showFortune();
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find views
        fortuneText = findViewById(R.id.fortune_text);
        fortuneTime = findViewById(R.id.fortune_time);

        // get data
        app = (FortuneCookieApp) getApplication();
        dataSource = app.getFortuneDataSource();
        fortuneLiveData = dataSource.getFortuneLiveData();

        // init views
        showFortune();

        // observe data
        fortuneLiveData.observe(this, (Fortune value) -> {
            showFortune();
        });
//        fortuneLiveData.observe(this, new Observer<Fortune>() {
//            @Override
//            public void onChanged(Fortune value) {
//                showFortune();
//            }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        handler.postDelayed(updateTime, 1000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(updateTime);
    }

    public void onRefresh(View view) {
        WorkManager workManager = WorkManager.getInstance(this);

        OneTimeWorkRequest workRequest =
            new OneTimeWorkRequest.Builder(GetFortuneWorker.class)
            .build();

        workManager.enqueue(workRequest);
    }

    private void showFortune() {
        Fortune fortune = fortuneLiveData.getValue();
        if (fortune != null) {
            String time = fortune.timestamp;

            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX", Locale.US);
                Date date = format.parse(fortune.timestamp);
                long fortuneMillis = date.getTime();
                long currentMillis = System.currentTimeMillis();
                long elapsedMillis = currentMillis - fortuneMillis;
                if (elapsedMillis >= 60_000) {
                    time = getString(
                        R.string.fortune_time_minutes_and_seconds,
                        elapsedMillis / 1000 / 60,
                        elapsedMillis / 1000 % 60
                    );
                } else {
                    time = getString(
                        R.string.fortune_time_seconds,
                        elapsedMillis / 1000
                    );
                }
            } catch (Exception ex) {
                Log.e(LOG_TAG, "failed to parse/format timestamp", ex);
            }

            fortuneText.setText(fortune.fortune);
            fortuneTime.setText(time);
        }
    }
}
