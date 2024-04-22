package com.example.pharmacy.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pharmacy.R;
import com.example.pharmacy.model.MedicineDao;
import com.example.pharmacy.model.MedicineDatabase;
import com.example.pharmacy.utils.MedicineListAdapter;
import com.example.pharmacy.viewmodel.MedicineListViewModel;

public class MedicineListFragment extends Fragment {
    private MedicineListViewModel medicineListViewModel;
    private RecyclerView recyclerView;
    private MedicineListAdapter medicineAdapter;
    private final String tag = "MLfragment";

    public MedicineListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MedicineDao medicineDao = MedicineDatabase.getInstance(requireContext()).medicineDao();
        medicineListViewModel = new MedicineListViewModel(medicineDao);
        View view = inflater.inflate(R.layout.fragment_medicine_list, container, false);
        recyclerView = view.findViewById(R.id.medications_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        medicineAdapter = new MedicineListAdapter(medicineListViewModel);
        recyclerView.setAdapter(medicineAdapter);
        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        medicineListViewModel.getMedicinesLiveData().observe(getViewLifecycleOwner(),
                medicines -> medicineAdapter.notifyDataSetChanged());
    }
}
