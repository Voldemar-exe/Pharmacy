package com.example.pharmacy.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.pharmacy.model.Medicine;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UserDataManager {
    private static final String TAG = "UserDataManager";
    private static final String PREFS_NAME = "user_data";
    private static final String FAVORITES_KEY = "favorites";
    private static final String HISTORY_KEY = "history";
    private static final String DOCUMENT = "med_users";
    private final FirebaseAuth mAuth;
    private final FirebaseFirestore db;
    private final SharedPreferences sharedPreferences;
    private static UserDataManager instance;

    private UserDataManager(Context context) {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized UserDataManager getInstance(Context context) {
        if (instance == null) {
            instance = new UserDataManager(context);
        }
        return instance;
    }

    public void saveFavoritesToSharedPreferences(Set<Medicine> favorites) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(favorites);
        editor.putString(FAVORITES_KEY, json);
        editor.apply();
    }

    public Set<Medicine> getFavoritesFromSharedPreferences() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(FAVORITES_KEY, null);
        Type type = new TypeToken<Set<Medicine>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public void saveHistoryToSharedPreferences(Deque<Medicine> history) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(history);
        editor.putString(HISTORY_KEY, json);
        editor.apply();
    }

    public Deque<Medicine> getHistoryFromSharedPreferences() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(HISTORY_KEY, null);
        Type type = new TypeToken<Deque<Medicine>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public void updateHistoryMedicine(Deque<Medicine> medicines) {
        if (mAuth.getCurrentUser() != null) {
            Map<String, Object> data = new HashMap<>();
            for (Medicine medicine : medicines) {
                data.put(medicine.getName(), medicine);
            }
            db.collection(DOCUMENT).document(mAuth.getCurrentUser().getUid())
                    .collection("data")
                    .document(HISTORY_KEY)
                    .set(data)
                    .addOnSuccessListener(aVoid -> Log.d(TAG,
                            "DocumentSnapshot successfully updated!"))
                    .addOnFailureListener(e -> Log.w(TAG,
                            "Error updating document", e));
        }
    }

    public void updateFavoriteMedicine(Set<Medicine> medicines) {
        if (mAuth.getCurrentUser() != null) {
            Map<String, Object> data = new HashMap<>();
            for (Medicine medicine : medicines) {
                data.put(medicine.getName(), medicine);
            }
            db.collection(DOCUMENT).document(mAuth.getCurrentUser().getUid())
                    .collection("data")
                    .document(FAVORITES_KEY)
                    .set(data)
                    .addOnSuccessListener(aVoid -> Log.d(TAG,
                            "DocumentSnapshot successfully updated!"))
                    .addOnFailureListener(e -> Log.w(TAG,
                            "Error updating document", e));
        }
    }

    public void readHistoryMedicine() {
        if (mAuth.getCurrentUser() != null) {
            db.collection(DOCUMENT).document(mAuth.getCurrentUser().getUid())
                    .collection("data")
                    .document(HISTORY_KEY)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        Map<String, Object> data = documentSnapshot.getData();
                        if (data != null) {
                            Deque<Medicine> medicines = new ArrayDeque<>();
                            for (Map.Entry<String, Object> entry : data.entrySet()) {
                                if (entry.getValue() instanceof Map) {
                                    Map<String, Object> medicineData =
                                            (HashMap<String, Object>) entry.getValue();
                                    Medicine medicine = new Medicine(medicineData);
                                    medicines.add(medicine);
                                }
                            }
                            saveHistoryToSharedPreferences(medicines);
                        }
                    })
                    .addOnFailureListener(e -> Log.w(TAG,
                            "Error reading document", e));
        }
    }
    public void readFavoritesMedicine() {
        if (mAuth.getCurrentUser() != null) {
            db.collection(DOCUMENT).document(mAuth.getCurrentUser().getUid())
                    .collection("data")
                    .document(FAVORITES_KEY)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        Map<String, Object> data = documentSnapshot.getData();
                        if (data != null) {
                            Set<Medicine> medicines = new HashSet<>();
                            for (Map.Entry<String, Object> entry : data.entrySet()) {
                                if (entry.getValue() instanceof Map) {
                                    Map<String, Object> medicineData =
                                            (HashMap<String, Object>) entry.getValue();
                                    Medicine medicine = new Medicine(medicineData);
                                    medicines.add(medicine);
                                }
                            }
                            saveFavoritesToSharedPreferences(medicines);
                        }
                    })
                    .addOnFailureListener(e -> Log.w("Firestore", "Error reading document", e));
        }
    }
}
