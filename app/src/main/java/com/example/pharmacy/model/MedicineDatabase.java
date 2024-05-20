package com.example.pharmacy.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Medicine.class}, version = 1)
public abstract class MedicineDatabase extends RoomDatabase {
    public abstract MedicineDao medicineDao();

    private static volatile MedicineDatabase INSTANCE;

    public static MedicineDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (MedicineDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context,
                            MedicineDatabase.class,
                            "medicine_database"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}
