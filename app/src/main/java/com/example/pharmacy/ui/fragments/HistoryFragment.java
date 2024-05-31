package com.example.pharmacy.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.pharmacy.R;
import com.example.pharmacy.databinding.FragmentHistoryBinding;
import com.example.pharmacy.model.Medicine;
import com.example.pharmacy.ui.interfaces.OnCheckIsFavorite;
import com.example.pharmacy.ui.interfaces.OnMedicineClickListener;
import com.example.pharmacy.utils.MedicineListAdapter;
import com.example.pharmacy.utils.UserDataManager;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class HistoryFragment extends Fragment implements OnMedicineClickListener, OnCheckIsFavorite {
    private FragmentHistoryBinding binding;
    private MedicineListAdapter medicineAdapter;
    private Set<Medicine> favorites = new HashSet<>();
    private Deque<Medicine> history = new LinkedList<>();

    public HistoryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
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
        binding = FragmentHistoryBinding.inflate(getLayoutInflater());

        medicineAdapter = new MedicineListAdapter(this, this);

        binding.historyList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.historyList.setAdapter(medicineAdapter);

        history = UserDataManager.getInstance(requireContext())
                .getHistoryFromSharedPreferences();
        if (history == null) {
            history = new ArrayDeque<>();
        }
        medicineAdapter.updateItems(new ArrayList<>(history));
        binding.btnBack.setOnClickListener(v -> Navigation.findNavController(v)
                .popBackStack());

        return binding.getRoot();
    }

    @Override
    public void onAddToFavoritesClick(Medicine medicine) {
        UserDataManager userManager = UserDataManager.getInstance(requireContext());
        userManager.readFavoritesMedicine();
        favorites = userManager.getFavoritesFromSharedPreferences();

        if (favorites == null) {
            favorites = new HashSet<>();
        }

        if (favorites.contains(medicine)) {
            favorites.remove(medicine);
        } else {
            favorites.add(medicine);
        }
        int position = medicineAdapter.getMedicineList().indexOf(medicine);
        if (position != -1) {
            medicineAdapter.notifyItemChanged(position);
        }

        userManager.saveFavoritesToSharedPreferences(favorites);
        userManager.updateFavoriteMedicine(favorites);
    }

    @Override
    public void onMedicineCardClick(Medicine medicine) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("medicine", medicine);
        Navigation.findNavController(requireView())
                .navigate(R.id.action_historyFragment_to_medicineDetailFragment, bundle);
    }

    @Override
    public boolean isFavorite(Medicine medicine) {
        UserDataManager userManager = UserDataManager.getInstance(requireContext());
        userManager.readFavoritesMedicine();
        Set<Medicine> favorites = userManager.getFavoritesFromSharedPreferences();
        if (favorites != null){
            return favorites.contains(medicine);
        }
        return false;
    }
}