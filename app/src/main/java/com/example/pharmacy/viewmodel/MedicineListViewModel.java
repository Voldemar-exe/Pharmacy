package com.example.pharmacy.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pharmacy.model.Medicine;
import com.example.pharmacy.model.MedicineDao;
import com.example.pharmacy.model.MedicineFiltration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MedicineListViewModel extends ViewModel {
    private final MedicineDao medicineDao;
    private final MutableLiveData<List<Medicine>> medicinesLiveData = new MutableLiveData<>();
    private final ExecutorService backgroundExecutor = Executors.newSingleThreadExecutor();
    private final Scheduler backgroundScheduler = Schedulers.from(backgroundExecutor);
    private final Scheduler mainScheduler = AndroidSchedulers.mainThread();


    public MedicineListViewModel(MedicineDao medicineDao) {
        this.medicineDao = medicineDao;
        medicineDao.getAllMedicines().observeForever(medicinesLiveData::setValue);
    }
    public List<Medicine> getFilteredMedicines(MedicineFiltration medicineFiltration){
        return filterMedicines(
                medicineFiltration.getSearchText(),
                medicineFiltration.getTypes(),
                medicineFiltration.getDosage(),
                medicineFiltration.getSideEffects(),
                medicineFiltration.getInteraction()
        );
    }

    private List<Medicine> filterMedicines(
            String searchText,
            String[] types,
            String dosage,
            String sideEffects,
            String interaction
    ) {
        List<Medicine> allMedicines = getMedicines().getValue();
        List<Medicine> filteredMedicines = new ArrayList<>(allMedicines);
        if (searchText != null && !searchText.isEmpty()) {
            filteredMedicines = filteredMedicines.stream()
                    .filter(m -> m.getName().toLowerCase().contains(searchText.toLowerCase()) ||
                            m.getDescription().toLowerCase().contains(searchText.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (types != null && types.length > 0) {
            filteredMedicines = filteredMedicines.stream()
                    .filter(m -> Arrays.stream(types)
                            .anyMatch(type -> m
                                    .getType()
                                    .toLowerCase()
                                    .contains(type.toLowerCase())))
                    .collect(Collectors.toList());
        }

        if (dosage != null && !dosage.isEmpty()) {
            filteredMedicines = filteredMedicines.stream()
                    .filter(m -> m
                            .getDosage()
                            .toLowerCase()
                            .contains(dosage.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (sideEffects != null && !sideEffects.isEmpty()) {
            filteredMedicines = filteredMedicines.stream()
                    .filter(m -> m
                            .getSideEffects()
                            .toLowerCase()
                            .contains(sideEffects.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (interaction != null && !interaction.isEmpty()) {
            filteredMedicines = filteredMedicines.stream()
                    .filter(m -> m
                            .getInteractions()
                            .toLowerCase()
                            .contains(interaction.toLowerCase()))
                    .collect(Collectors.toList());
        }

        return filteredMedicines;
    }

    public void insertMedicine(Medicine medicine) {
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
                        // Handle subscription for potential cleanup
                    }

                    @Override
                    public void onComplete() {
                        // Handle successful insertion
                    }

                    @Override
                    public void onError(Throwable e) {
                        // Handle errors during insertion
                    }
                });
    }

    public LiveData<List<Medicine>> getMedicines() {
        return medicinesLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        backgroundExecutor.shutdown();
    }
}
