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

    @Query("SELECT COUNT(*) FROM medicine")
    LiveData<Integer> getMedicineCount();

    @Query("SELECT * FROM medicine WHERE id = :medicineId")
    LiveData<Medicine> getMedicineById(long medicineId);

    @Query("SELECT * FROM medicine WHERE price BETWEEN :minPrice AND :maxPrice")
    LiveData<List<Medicine>> getMedicinesByPriceRange(double minPrice, double maxPrice);

    @Query("SELECT * FROM medicine WHERE description LIKE '%' || :searchQuery || '%'")
    LiveData<List<Medicine>> searchMedicines(String searchQuery);

    @Insert
    void insertMedicine(Medicine medicine);

    @Update
    void updateMedicine(Medicine medicine);

    @Delete
    void deleteMedicine(Medicine medicine);
}

