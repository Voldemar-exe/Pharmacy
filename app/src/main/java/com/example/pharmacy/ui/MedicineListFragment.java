package com.example.pharmacy.ui;

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
import com.example.pharmacy.model.Medicine;
import com.example.pharmacy.model.MedicineDao;
import com.example.pharmacy.model.MedicineDatabase;
import com.example.pharmacy.utils.MedicineListAdapter;
import com.example.pharmacy.viewmodel.MedicineListViewModel;

import java.util.ArrayList;

public class MedicineListFragment extends Fragment {
    private MedicineListViewModel medicineListViewModel;
    private RecyclerView recyclerView;
    private MedicineListAdapter medicineAdapter;
    private MedicineDao medicineDao;
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
        View view = inflater.inflate(R.layout.fragment_medicine_list, container, false);

        medicineDao = MedicineDatabase.getInstance(requireContext()).medicineDao();
        medicineListViewModel = new MedicineListViewModel(medicineDao);

        recyclerView = view.findViewById(R.id.medications_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        medicineAdapter = new MedicineListAdapter();
        recyclerView.setAdapter(medicineAdapter);
        medicineListViewModel.getMedicines().observe(getViewLifecycleOwner(), entities -> {
            if (entities != null) {
                medicineAdapter.updateItems(entities);
            }
        });
        /*ArrayList<Medicine> medicineList = new ArrayList<>();
        String[] medicineArray = getResources().getStringArray(R.array.medicine);
        for (int i = 0; i < medicineArray.length; i++){
            medicineList.add(new Medicine(R.drawable.asperin, medicineArray[i], (double) i / 2));
        }
        new Thread(() -> {
            for (Medicine medicine : medicineList){
                medicineListViewModel.insertMedicine(medicine);
            }
        }).start();*/
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
