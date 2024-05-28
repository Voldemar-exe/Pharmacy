package com.example.pharmacy.utils;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pharmacy.R;
import com.example.pharmacy.databinding.ItemMedicineBinding;
import com.example.pharmacy.model.Medicine;
import com.example.pharmacy.ui.interfaces.OnCheckIsFavorite;
import com.example.pharmacy.ui.interfaces.OnMedicineClickListener;

import java.util.ArrayList;
import java.util.List;

public class MedicineListAdapter extends RecyclerView.Adapter<MedicineListAdapter.MedicineViewHolder> {
    private final List<Medicine> medicineList = new ArrayList<>();

    private static OnMedicineClickListener listenerClick;
    private static OnCheckIsFavorite listenerFavorite;

    public MedicineListAdapter(OnMedicineClickListener listenerClick, OnCheckIsFavorite listenerFavorite) {
        MedicineListAdapter.listenerClick = listenerClick;
        MedicineListAdapter.listenerFavorite = listenerFavorite;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateItems(List<Medicine> newMedicineList) {
        medicineList.clear();
        medicineList.addAll(newMedicineList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMedicineBinding binding =
                ItemMedicineBinding.inflate(
                        LayoutInflater.from(
                                parent.getContext()
                        ),
                        parent,
                        false
                );
        return new MedicineViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
        if (position < medicineList.size()) {
            Medicine medicine = medicineList.get(position);
            holder.bind(medicine);
        }
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }
    public List<Medicine> getMedicineList(){
        return medicineList;
    }

    public static class MedicineViewHolder extends RecyclerView.ViewHolder {
        private final ItemMedicineBinding binding;

        public MedicineViewHolder(ItemMedicineBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Medicine medicine) {
            binding.textViewName.setText(medicine.getName());
            binding.medType.setHint(medicine.getType());
            binding.btnAdd.setOnClickListener(v -> {
                if (listenerClick != null) {
                    listenerClick.onAddToFavoritesClick(medicine);
                }
            });
            binding.getRoot().setOnClickListener(view -> {
                if (listenerClick != null) {
                    listenerClick.onMedicineCardClick(medicine);
                }
            });
            if (listenerFavorite.isFavorite(medicine)){
                binding.btnAdd.setImageResource(R.drawable.ic_favorite);
            } else {
                binding.btnAdd.setImageResource(R.drawable.ic_unfavorite);
            }
        }
    }
}