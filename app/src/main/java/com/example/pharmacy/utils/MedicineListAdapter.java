package com.example.pharmacy.utils;
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

import java.util.List;

public class MedicineListAdapter extends RecyclerView.Adapter<MedicineListAdapter.MedicineViewHolder> {

    private final List<Medicine> MedicineList;

    public MedicineListAdapter(List<Medicine> MedicineList) {
        this.MedicineList = MedicineList;
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_medicine, parent, false);
        return new MedicineViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
        Medicine Medicine = MedicineList.get(position);
        holder.imageViewMedicine.setImageResource(Medicine.getImageResource());
        holder.textViewDescription.setText(Medicine.getDescription());
        holder.textViewPrice.setText("Цена: " + Medicine.getPrice() + "₽");
    }

    @Override
    public int getItemCount() {
        return MedicineList.size();
    }

    public class MedicineViewHolder extends RecyclerView.ViewHolder {

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
            // Добавь обработчик для кнопки или других View, если необходимо
        }
    }
}
