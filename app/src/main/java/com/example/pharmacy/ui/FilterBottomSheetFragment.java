package com.example.pharmacy.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.example.pharmacy.R;
import com.example.pharmacy.databinding.FilterBottomSheetBinding;
import com.example.pharmacy.model.MedicineFiltration;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FilterBottomSheetFragment extends BottomSheetDialogFragment {
    private FilterBottomSheetBinding binding;
    private MedicineFiltration savedFiltration;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedFiltration = new MedicineFiltration();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FilterBottomSheetBinding.inflate(getLayoutInflater());
        String[] medicineArray = getResources().getStringArray(R.array.medicine_types);

        Set<String> uniqueMedicinesSet = new HashSet<>(Arrays.asList(medicineArray));

        for (String medicine : uniqueMedicinesSet) {
            Chip chip = new Chip(requireContext());
            chip.setText(medicine);
            chip.setCheckable(true);
            binding.typeChips.addView(chip);
        }
        binding.button.setOnClickListener(v -> applyFiltersAndNavigate());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            MedicineFiltration receivedFiltration =
                    (MedicineFiltration) getArguments().getSerializable("filter");
            if (receivedFiltration != null) {
                savedFiltration = receivedFiltration;
                restoreFilters(savedFiltration);
            }
        }
    }

    private void applyFiltersAndNavigate() {
        ArrayList<String> types = new ArrayList<>();
        for (int chipId : binding.typeChips.getCheckedChipIds()) {
            types.add(((Chip) binding.typeChips.findViewById(chipId)).getText().toString());
        }

        savedFiltration = new MedicineFiltration(
                null,
                types.toArray(new String[0]),
                binding.inputDosage.getText().toString(),
                binding.inputSideEffects.getText().toString(),
                binding.inputInteraction.getText().toString()
        );

        Bundle bundle = new Bundle();
        bundle.putSerializable("filter", savedFiltration);
        Navigation.findNavController(requireParentFragment().requireView())
                .navigate(
                        R.id.action_filterBottomSheetFragment_to_MedicineListFragment,
                        bundle
                );
    }

    private void restoreFilters(MedicineFiltration filter) {
        if (filter == null) return;

        if (filter.getTypes() != null) {
            List<String> selectedTypes = Arrays.asList(filter.getTypes());
            for (int i = 0; i < binding.typeChips.getChildCount(); i++) {
                Chip chip = (Chip) binding.typeChips.getChildAt(i);
                if (selectedTypes.contains(chip.getText().toString())) {
                    chip.setChecked(true);
                }
            }
        }

        binding.inputDosage
                .setText(filter.getDosage() != null ? filter.getDosage() : "");
        binding.inputSideEffects
                .setText(filter.getSideEffects() != null ? filter.getSideEffects() : "");
        binding.inputInteraction
                .setText(filter.getInteraction() != null ? filter.getInteraction() : "");
    }
}