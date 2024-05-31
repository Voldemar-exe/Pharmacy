package com.example.pharmacy.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.pharmacy.R;
import com.example.pharmacy.databinding.FragmentProfileBinding;
import com.example.pharmacy.model.Medicine;
import com.example.pharmacy.ui.interfaces.OnCheckIsFavorite;
import com.example.pharmacy.ui.interfaces.OnMedicineClickListener;
import com.example.pharmacy.utils.MedicineListAdapter;
import com.example.pharmacy.utils.UserDataManager;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ProfileFragment extends Fragment implements OnMedicineClickListener, OnCheckIsFavorite {
    private FirebaseAuth mAuth;
    private Set<Medicine> favorites = new HashSet<>();
    private static final int HISTORY_MAX_SIZE = 50;
    private MedicineListAdapter medicineAdapter;

    public ProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        FragmentProfileBinding binding = FragmentProfileBinding.inflate(inflater, container, false);

        medicineAdapter = new MedicineListAdapter(this, this);

        binding.favoriteList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.favoriteList.setAdapter(medicineAdapter);

        binding.username.setText(Objects.requireNonNull(mAuth.getCurrentUser()).getDisplayName());

        binding.btnExit.setOnClickListener(v -> {
            mAuth.signOut();
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_profileFragment_to_signInFragment);
        });

        binding.btnHistory.setOnClickListener(v -> Navigation.findNavController(v)
                .navigate(R.id.action_profileFragment_to_historyFragment));
        UserDataManager userManager = UserDataManager.getInstance(requireContext());
        userManager.readFavoritesMedicine();
        userManager.readHistoryMedicine();
        favorites = UserDataManager.getInstance(requireContext())
                .getFavoritesFromSharedPreferences();
        if (favorites == null) {
            favorites = new HashSet<>();
        }
        medicineAdapter.updateItems(new ArrayList<>(favorites));
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

        userManager.saveFavoritesToSharedPreferences(favorites);
        userManager.updateFavoriteMedicine(favorites);

        medicineAdapter.updateItems(new ArrayList<>(favorites));
    }

    @Override
    public void onMedicineCardClick(Medicine medicine) {
        UserDataManager userManager = UserDataManager.getInstance(requireContext());
        userManager.readHistoryMedicine();
        Deque<Medicine> history = userManager.getHistoryFromSharedPreferences();
        if (history != null) {
            history.remove(medicine);
        } else {
            history = new ArrayDeque<>();
        }
        history.addFirst(medicine);
        while (history.size() > HISTORY_MAX_SIZE) {
            history.removeLast();
        }
        userManager.saveHistoryToSharedPreferences(history);
        userManager.updateHistoryMedicine(history);

        Bundle bundle = new Bundle();
        bundle.putSerializable("medicine", medicine);
        Navigation.findNavController(requireView())
                .navigate(R.id.action_profileFragment_to_medicineDetailFragment, bundle);
    }

    @Override
    public boolean isFavorite(Medicine medicine) {
        if (favorites != null){
            return favorites.contains(medicine);
        }
        return false;
    }
}
