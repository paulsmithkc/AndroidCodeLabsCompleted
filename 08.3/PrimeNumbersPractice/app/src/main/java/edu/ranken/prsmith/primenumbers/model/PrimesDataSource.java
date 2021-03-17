package edu.ranken.prsmith.primenumbers.model;

import androidx.annotation.MainThread;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class PrimesDataSource {
    private ArrayList<Long> primes;
    private MutableLiveData<List<Long>> primesLiveData;

    @MainThread
    public PrimesDataSource() {
        primes = new ArrayList<>();
        primesLiveData = new MutableLiveData<>();
        primesLiveData.setValue(primes);
    }

    @MainThread
    public List<Long> getPrimes() {
        return primes;
    }

    @MainThread
    public LiveData<List<Long>> getPrimesLiveData() {
        return primesLiveData;
    }

    @WorkerThread
    public void addPrime(long prime) {
        primes.add(prime);
        primesLiveData.postValue(primes);
    }
}
