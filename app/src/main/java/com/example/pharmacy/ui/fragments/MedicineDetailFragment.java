package com.example.pharmacy.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.pharmacy.databinding.FragmentMedicineDetailBinding;
import com.example.pharmacy.model.Medicine;

public class MedicineDetailFragment extends Fragment {

    private FragmentMedicineDetailBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(requireView()).popBackStack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentMedicineDetailBinding.inflate(inflater, container, false);
        binding.btnBack.setOnClickListener(view ->
                Navigation
                .findNavController(view).popBackStack()
        );
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            Medicine medicine = (Medicine) getArguments().getSerializable("medicine");

            if (medicine != null) {
                binding.medicineName.setText(medicine.getName());
                binding.medicineDescription.setText(medicine.getDescription());
                binding.medicineDosage.setText(medicine.getDosage());
                binding.medicineSideEffects.setText(medicine.getSideEffects());
                binding.medicineInteractions.setText(medicine.getInteractions());
            }
        }
    }
}