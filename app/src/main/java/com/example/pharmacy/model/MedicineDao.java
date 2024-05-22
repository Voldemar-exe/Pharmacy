package com.example.pharmacy.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MedicineDao {

    @Query("SELECT * FROM medicine")
    LiveData<List<Medicine>> getAllMedicines();
    @Query("SELECT * FROM medicine WHERE name IN (:names)")
    List<Medicine> getMedicinesByNames(List<String> names);

    @Insert
    void insertMedicine(Medicine medicine);

    @Update
    void updateMedicine(Medicine medicine);

    @Delete
    void deleteMedicine(Medicine medicine);
}

