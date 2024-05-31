package com.example.pharmacy.ui.dialogs;

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
import java.util.Set;

public class FilterBottomSheetFragment extends BottomSheetDialogFragment {
    private FilterBottomSheetBinding binding;
    private MedicineFiltration savedFiltration;
    private Chip[] medicineChips;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        savedFiltration = new MedicineFiltration();

    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        binding = FilterBottomSheetBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (medicineChips == null) {
            Set<String> uniqueMedicinesSet = new HashSet<>(
                    Arrays.asList(getResources().getStringArray(R.array.medicine_types))
            );
            medicineChips = new Chip[uniqueMedicinesSet.size()];
            for (String medicine : uniqueMedicinesSet) {
                Chip chip = new Chip(requireContext());
                chip.setText(medicine);
                chip.setCheckable(true);
                binding.typeChips.addView(chip);
            }
        } else {
            for (Chip medicineChip : medicineChips) {
                binding.typeChips.addView(medicineChip);
            }
        }

        binding.button.setOnClickListener(v -> applyFiltersAndNavigate());
        binding.clear.setOnClickListener(v -> {
            clearFilters();
            savedFiltration = new MedicineFiltration(
                    null,
                    null,
                    null,
                    null,
                    null
            );
        });
        if (getArguments() != null && getArguments().containsKey("filter")) {
            savedFiltration = (MedicineFiltration) getArguments().getSerializable("filter");
            restoreFilters(savedFiltration);
        }
    }

    private void applyFiltersAndNavigate() {
        ArrayList<String> types = new ArrayList<>();
        for (int i = 0; i < binding.typeChips.getChildCount(); i++) {
            Chip chip = (Chip) binding.typeChips.getChildAt(i);
            if (chip.isChecked()) {
                types.add(chip.getText().toString());
            }
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

    private void clearFilters() {
        binding.typeChips.clearCheck();
        binding.inputDosage.setText("");
        binding.inputSideEffects.setText("");
        binding.inputInteraction.setText("");
    }

    private void restoreFilters(MedicineFiltration filter) {
        if (filter == null) return;

        if (filter.getTypes() != null) {
            Set<String> selectedTypes = new HashSet<>(Arrays.asList(filter.getTypes()));
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