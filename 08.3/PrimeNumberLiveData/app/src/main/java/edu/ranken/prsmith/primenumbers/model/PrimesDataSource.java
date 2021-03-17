package edu.ranken.prsmith.primenumbers.model;

import androidx.annotation.MainThread;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class PrimesDataSource {
    private long current;
    private long max;
    private ArrayList<Long> primes;

    private MutableLiveData<Long> currentLiveData;
    private MutableLiveData<Long> maxLiveData;
    private MutableLiveData<List<Long>> primesLiveData;

    @MainThread
    public PrimesDataSource() {
        current = 2;
        max = 10_000_000;
        primes = new ArrayList<>();

        currentLiveData = new MutableLiveData<>(current);
        maxLiveData = new MutableLiveData<>(max);
        primesLiveData = new MutableLiveData<>(primes);
    }

//    public long getCurrent() {
//        return current;
//    }
//
//    public long getMax() {
//        return max;
//    }
//
//    public List<Long> getPrimes() {
//        return primes;
//    }

    public MutableLiveData<Long> getCurrentLiveData() {
        return currentLiveData;
    }

    public MutableLiveData<Long> getMaxLiveData() {
        return maxLiveData;
    }

    public LiveData<List<Long>> getPrimesLiveData() {
        return primesLiveData;
    }

    @WorkerThread
    public void setCurrent(long current) {
        this.current = current;
        currentLiveData.postValue(current);
    }

    @WorkerThread
    public void addPrime(long prime) {
        this.current = prime;
        this.primes.add(prime);
        currentLiveData.postValue(prime);
        primesLiveData.postValue(primes);
    }
}
