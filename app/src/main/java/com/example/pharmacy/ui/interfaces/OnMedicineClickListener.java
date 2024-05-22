package com.example.pharmacy.ui.interfaces;

import com.example.pharmacy.model.Medicine;

public interface OnMedicineClickListener {
    void onAddToFavoritesClick(Medicine medicine);
    void onMedicineCardClick(Medicine medicine);
}
