package edu.ranken.prsmith.primenumbers.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import edu.ranken.prsmith.primenumbers.PrimesApp;
import edu.ranken.prsmith.primenumbers.R;
import edu.ranken.prsmith.primenumbers.adapter.NumberListAdapter;

public class MainActivity extends AppCompatActivity {

    // views
    private ProgressBar progressBar;
    private RecyclerView recyclerView;

    // broadcasts
    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver receiver;
    private IntentFilter intentFilter;

    // data
    private List<Long> primes;
    private NumberListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find views
        progressBar = findViewById(R.id.primes_progress);
        recyclerView = findViewById(R.id.primes_list);

        // get broadcast manager
        localBroadcastManager = LocalBroadcastManager.getInstance(this);

        // register receiver
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case PrimesApp.ACTION_PRIME_FOUND:
                        onPrimeFound(intent.getLongExtra(PrimesApp.EXTRA_PRIME, 0));
                        onPrimeProgress(intent.getLongExtra(PrimesApp.EXTRA_PROGRESS, 0));
                        break;
                    case PrimesApp.ACTION_PRIME_START:
                        onPrimeStart(intent.getLongExtra(PrimesApp.EXTRA_PROGRESS, 0));
                        break;
                    case PrimesApp.ACTION_PRIME_PROGRESS:
                        onPrimeProgress(intent.getLongExtra(PrimesApp.EXTRA_PROGRESS, 0));
                        break;
                    case PrimesApp.ACTION_PRIME_STOP:
                        onPrimeStop(intent.getLongExtra(PrimesApp.EXTRA_PROGRESS, 0));
                        break;
                }
            }
        };
        intentFilter = new IntentFilter();
        intentFilter.addAction(PrimesApp.ACTION_PRIME_FOUND);
        intentFilter.addAction(PrimesApp.ACTION_PRIME_START);
        intentFilter.addAction(PrimesApp.ACTION_PRIME_PROGRESS);
        intentFilter.addAction(PrimesApp.ACTION_PRIME_STOP);
        localBroadcastManager.registerReceiver(receiver, intentFilter);

        // init recylerview
        primes = new ArrayList<>();
        adapter = new NumberListAdapter(this, primes);
        recyclerView.setLayoutManager(new LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            true
        ));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(receiver);
    }

    private void onPrimeFound(long prime) {
        primes.add(prime);
        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(primes.size() - 1);
    }

    private void onPrimeStart(long progress) {
        primes.clear();
        adapter.notifyDataSetChanged();
        progressBar.setProgress((int)progress);
    }

    private void onPrimeProgress(long progress) {
        progressBar.setProgress((int)progress);
    }

    private void onPrimeStop(long progress) {
        progressBar.setProgress((int)progress);
    }
}
