package com.example.pharmacy.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pharmacy.model.Medicine;
import com.example.pharmacy.model.MedicineDao;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MedicineListViewModel extends ViewModel {
    private final MedicineDao medicineDao;
    private final MutableLiveData<List<Medicine>> medicinesLiveData;
    private final ExecutorService backgroundExecutor = Executors.newSingleThreadExecutor();
    private final Scheduler backgroundScheduler = Schedulers.from(backgroundExecutor);
    private final Scheduler mainScheduler = AndroidSchedulers.mainThread();

    public MedicineListViewModel(MedicineDao medicineDao) {
        this.medicineDao = medicineDao;
        medicinesLiveData = new MutableLiveData<>();
        loadMedicines();
    }

    private void loadMedicines() {
        medicineDao.getAllMedicines().observeForever(medicinesLiveData::setValue);
    }


    private void insertMedicine(Medicine medicine) {
        Completable insertCompletable = Completable.create(emitter -> {
            medicineDao.insertMedicine(medicine);
            emitter.onComplete();
        });

        insertCompletable
                .subscribeOn(backgroundScheduler)
                .observeOn(mainScheduler)
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        // Optional: Handle subscription for potential cleanup
                    }

                    @Override
                    public void onComplete() {
                        // Optional: Handle successful insertion (e.g., update UI)
                    }

                    @Override
                    public void onError(Throwable e) {
                        // Handle errors during insertion (e.g., show error message)
                    }
                });
    }

    public LiveData<List<Medicine>> getMedicinesLiveData() {
        return medicinesLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        backgroundExecutor.shutdown();
    }
}
