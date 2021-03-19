package edu.ranken.prsmith.fortunecookie.model;

import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class FortuneDataSource {
    private MutableLiveData<Fortune> fortuneLiveData;

    public FortuneDataSource() {
        fortuneLiveData = new MutableLiveData<>();
    }

    public LiveData<Fortune> getFortuneLiveData() {
        return fortuneLiveData;
    }

    public Fortune getFortune() {
        return fortuneLiveData.getValue();
    }

    @WorkerThread
    public void setFortune(Fortune fortune) {
        this.fortuneLiveData.postValue(fortune);
    }
}
