package edu.ranken.prsmith.primenumberscodealong.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.ranken.prsmith.primenumberscodealong.PrimesApp;
import edu.ranken.prsmith.primenumberscodealong.R;
import edu.ranken.prsmith.primenumberscodealong.adapter.NumberListAdapter;

public class MainActivity extends AppCompatActivity {

    // views
    private TextView highestText;
    private ProgressBar horizontalProgress;
    private ProgressBar circleProgress;
    private RecyclerView recyclerView;

    // data
    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver receiver;
    private IntentFilter intentFilter;
    private List<Long> primes;
    private NumberListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find views
        highestText = findViewById(R.id.prime_highest);
        horizontalProgress = findViewById(R.id.prime_progress_horizontal);
        circleProgress = findViewById(R.id.prime_progress_circle);
        recyclerView = findViewById(R.id.prime_list);

        // get broadcast manager
        localBroadcastManager = LocalBroadcastManager.getInstance(this);

        // create receiver
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case PrimesApp.ACTION_FIND_PRIMES_START: {
                        long current = intent.getLongExtra(PrimesApp.EXTRA_CURRENT, 0);
                        long max = intent.getLongExtra(PrimesApp.EXTRA_MAX, 0);
                        circleProgress.setVisibility(View.VISIBLE);
                        horizontalProgress.setProgress((int) (100 * current / max));
                        break;
                    }
                    case PrimesApp.ACTION_FIND_PRIMES_STOP: {
                        long current = intent.getLongExtra(PrimesApp.EXTRA_CURRENT, 0);
                        long max = intent.getLongExtra(PrimesApp.EXTRA_MAX, 0);
                        circleProgress.setVisibility(View.GONE);
                        horizontalProgress.setProgress((int) (100 * current / max));
                        break;
                    }
                    case PrimesApp.ACTION_PRIME_FOUND: {
                        long current = intent.getLongExtra(PrimesApp.EXTRA_CURRENT, 0);
                        long max = intent.getLongExtra(PrimesApp.EXTRA_MAX, 0);
                        circleProgress.setVisibility(View.VISIBLE);
                        horizontalProgress.setProgress((int) (100 * current / max));
                        highestText.setText(String.valueOf(current));

                        primes.add(current);
                        adapter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(primes.size() - 1);
                        break;
                    }
                }
            }
        };

        // create intent filter
        intentFilter = new IntentFilter();
        intentFilter.addAction(PrimesApp.ACTION_FIND_PRIMES_START);
        intentFilter.addAction(PrimesApp.ACTION_FIND_PRIMES_STOP);
        intentFilter.addAction(PrimesApp.ACTION_PRIME_FOUND);

        // register the receiver
        localBroadcastManager.registerReceiver(receiver, intentFilter);

        // init recyclerview
        primes = new ArrayList<>();
        adapter = new NumberListAdapter(this, primes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(receiver);
    }
}
