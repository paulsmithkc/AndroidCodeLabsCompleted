package edu.ranken.prsmith.primenumbers.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import edu.ranken.prsmith.primenumbers.PrimesApp;
import edu.ranken.prsmith.primenumbers.R;
import edu.ranken.prsmith.primenumbers.adapter.NumberListAdapter;
import edu.ranken.prsmith.primenumbers.model.PrimesDataSource;

public class MainActivity extends AppCompatActivity {

    // views
    private ProgressBar progressBar;
    private RecyclerView recyclerView;

    // data
    private PrimesApp app;
    private PrimesDataSource dataSource;
    private LiveData<Long> currentLiveData;
    private LiveData<Long> maxLiveData;
    private LiveData<List<Long>> primesLiveData;
    private NumberListAdapter adapter;
    private long current;
    private long max;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find views
        progressBar = findViewById(R.id.primes_progress);
        recyclerView = findViewById(R.id.primes_list);

        // init recylerview
        adapter = new NumberListAdapter(this, null);
        recyclerView.setLayoutManager(new LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            true
        ));
        recyclerView.setAdapter(adapter);

        // get data
        app = (PrimesApp) getApplication();
        dataSource = app.getPrimesDataSource();
        currentLiveData = dataSource.getCurrentLiveData();
        maxLiveData = dataSource.getMaxLiveData();
        primesLiveData = dataSource.getPrimesLiveData();

        // initialize views
        current = currentLiveData.getValue();
        max = maxLiveData.getValue();
        progressBar.setProgress((int)(100 * current / max));
        adapter.setItems(primesLiveData.getValue());

        // register observers
        currentLiveData.observe(this, new Observer<Long>() {
            @Override
            public void onChanged(Long value) {
                // Log.i(PrimesApp.LOG_TAG, "current = " + value);
                current = value;
                progressBar.setProgress((int)(100 * current / max));
            }
        });
        maxLiveData.observe(this, new Observer<Long>() {
            @Override
            public void onChanged(Long value) {
                max = value;
                progressBar.setProgress((int)(100 * current / max));
            }
        });
        primesLiveData.observe(this, new Observer<List<Long>>() {
            @Override
            public void onChanged(List<Long> value) {
                adapter.setItems(value);
                recyclerView.scrollToPosition(value.size() - 1);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
