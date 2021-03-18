package edu.ranken.prsmith.primenumberscodealong.model;

import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class PrimesDataSource {
    private boolean running;
    private long current;
    private long max;
    private ArrayList<Long> primes;

    private MutableLiveData<Boolean> runningLiveData;
    private MutableLiveData<Long> currentLiveData;
    private MutableLiveData<Long> maxLiveData;
    private MutableLiveData<List<Long>> primesLiveData;

    public PrimesDataSource() {
        running = false;
        current = 2;
        max = 100_000;
        primes = new ArrayList<>();

        runningLiveData = new MutableLiveData<>(running);
        currentLiveData = new MutableLiveData<>(current);
        maxLiveData = new MutableLiveData<>(max);
        primesLiveData = new MutableLiveData<>(primes);
    }

    public LiveData<Boolean> getRunningLiveData() {
        return runningLiveData;
    }

    public LiveData<Long> getCurrentLiveData() {
        return currentLiveData;
    }

    public LiveData<Long> getMaxLiveData() {
        return maxLiveData;
    }

    public LiveData<List<Long>> getPrimesLiveData() {
        return primesLiveData;
    }

    @WorkerThread
    public void setRunning(boolean running) {
        this.running = running;
        this.runningLiveData.postValue(running);
    }

    @WorkerThread
    public void setCurrent(long current) {
        this.current = current;
        this.currentLiveData.postValue(current);
    }

    @WorkerThread
    public void addPrime(long prime) {
        this.current = prime;
        this.primes.add(prime);
        this.currentLiveData.postValue(prime);
        this.primesLiveData.postValue(primes);
    }
}
