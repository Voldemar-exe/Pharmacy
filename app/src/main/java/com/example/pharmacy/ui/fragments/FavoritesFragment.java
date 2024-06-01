package com.example.pharmacy.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pharmacy.R;
import com.example.pharmacy.databinding.FragmentFavoritesBinding;
import com.example.pharmacy.model.Medicine;
import com.example.pharmacy.ui.interfaces.OnCheckIsFavorite;
import com.example.pharmacy.ui.interfaces.OnMedicineClickListener;
import com.example.pharmacy.utils.MedicineListAdapter;
import com.example.pharmacy.utils.UserDataManager;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

public class FavoritesFragment extends Fragment implements OnMedicineClickListener, OnCheckIsFavorite {
    public FavoritesFragment() {
    }

    private Set<Medicine> favorites = new HashSet<>();
    private static final int HISTORY_MAX_SIZE = 50;
    private MedicineListAdapter medicineAdapter;
    private FragmentFavoritesBinding binding;

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
        binding = FragmentFavoritesBinding.inflate(getLayoutInflater());
        medicineAdapter = new MedicineListAdapter(this, this);

        binding.favoriteList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.favoriteList.setAdapter(medicineAdapter);
        binding.favoriteList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private final Handler handler = new Handler(Looper.getMainLooper());
            private final Runnable hideButtonRunnable =
                    () -> binding.btnUp.setVisibility(View.INVISIBLE);

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    handler.postDelayed(hideButtonRunnable, 500);
                } else {
                    binding.btnUp.setVisibility(View.VISIBLE);
                    handler.removeCallbacks(hideButtonRunnable);
                }
            }
        });
        binding.btnUp.setVisibility(View.INVISIBLE);
        binding.btnUp.setOnClickListener(view -> binding.favoriteList.smoothScrollToPosition(0));
        binding.btnBack.setOnClickListener(v -> Navigation.findNavController(v)
                .popBackStack());

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
                .navigate(R.id.action_favoritesFragment_to_medicineDetailFragment, bundle);
    }

    @Override
    public boolean isFavorite(Medicine medicine) {
        if (favorites != null) {
            return favorites.contains(medicine);
        }
        return false;
    }
}