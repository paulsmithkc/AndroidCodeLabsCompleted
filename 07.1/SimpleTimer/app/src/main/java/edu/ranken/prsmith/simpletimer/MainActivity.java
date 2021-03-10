package edu.ranken.prsmith.simpletimer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.TextView;

import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private TextView timerText;
    private TimerTask timerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timerText = findViewById(R.id.timer_text);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (timerTask != null) {
            timerTask.cancel(true);
        }
        timerTask = new TimerTask();
        timerTask.execute();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (timerTask != null) {
            timerTask.cancel(true);
        }
    }

    private class TimerTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            while (!isCancelled()) {
                try {
                    Thread.sleep(1000L);
                    publishProgress();
                } catch (InterruptedException e) {
                    // thread was interrupted
                    // task may have been cancelled
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... progress) {
            long millisecondsSinceBoot = android.os.SystemClock.elapsedRealtime();
            String timeString = String.format(
                "Time since boot\n%02d:%02d:%02d",
                millisecondsSinceBoot / 1000 / 60 / 60,  // hours
                millisecondsSinceBoot / 1000 / 60 % 60,  // minutes
                millisecondsSinceBoot / 1000 % 60        // seconds
            );
            timerText.setText(timeString);
        }
    }
}
