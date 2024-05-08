package com.example.pharmacy.utils;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pharmacy.R;
import com.example.pharmacy.model.Medicine;

import java.util.ArrayList;
import java.util.List;

public class MedicineListAdapter extends RecyclerView.Adapter<MedicineListAdapter.MedicineViewHolder> {
    private final List<Medicine> medicineList = new ArrayList<>();
    public MedicineListAdapter() {}

    @SuppressLint("NotifyDataSetChanged")
    public void updateItems(List<Medicine> newMedicineList) {
        medicineList.clear();
        medicineList.addAll(newMedicineList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medicine, parent, false);
        return new MedicineViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
        if (position < medicineList.size()) {
            Medicine medicine = medicineList.get(position);
            holder.imageViewMedicine.setImageResource(medicine.getImageResource());
            holder.textViewDescription.setText(medicine.getDescription());
            holder.textViewPrice.setText(String.valueOf(medicine.getPrice()));
            // Set click listeners or other logic for views in the ViewHolder
        }
//        String item = medicineList.get(position).getDescription();
//        holder.bind(item);
    }


    @Override
    public int getItemCount() {
        return medicineList.size();
    }


    public static class MedicineViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageViewMedicine;
        public TextView textViewDescription;
        public TextView textViewPrice;
        public ImageButton btnAdd;

        public MedicineViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewMedicine = itemView.findViewById(R.id.image_view_medicine);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewPrice = itemView.findViewById(R.id.text_view_price);
            btnAdd = itemView.findViewById(R.id.btnAdd);
        }
    }
}
