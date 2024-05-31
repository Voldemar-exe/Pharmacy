package com.example.pharmacy.ui.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.pharmacy.R;
import com.example.pharmacy.databinding.FragmentMainMenuBinding;
import com.google.firebase.auth.FirebaseAuth;

public class MainMenuFragment extends Fragment {
    private FragmentMainMenuBinding binding;
    private final FirebaseAuth mAuth;

    public MainMenuFragment() {
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentMainMenuBinding.inflate(getLayoutInflater());
        binding.cardFavorites.setOnClickListener(v -> {
            if (mAuth.getCurrentUser() != null) {
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_mainMenuFragment_to_favoritesFragment);
            } else {
                Toast.makeText(requireContext(),
                        "Зарегистрируйтесь",
                        Toast.LENGTH_SHORT).show();
            }
        });

        binding.cardHistory.setOnClickListener(v -> {
            if (mAuth.getCurrentUser() != null) {
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_mainMenuFragment_to_historyFragment);
            } else {
                Toast.makeText(requireContext(),
                        "Зарегистрируйтесь",
                        Toast.LENGTH_SHORT).show();
            }
        });
        binding.cardMap.setOnClickListener(v ->
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_mainMenuFragment_to_mapFragment)
        );
        binding.cardMedicines.setOnClickListener(v ->
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_mainMenuFragment_to_MedicineListFragment)
        );
        binding.btnInfoMedicines.setOnClickListener(v ->
                showInfoFragment("fragment_info_medicine"));
        binding.btnInfoMap.setOnClickListener(v ->
                showInfoFragment("fragment_info_map"));
        binding.btnInfoHistory.setOnClickListener(v ->
                showInfoFragment("fragment_info_history"));
        binding.btnInfoFavorites.setOnClickListener(v ->
                showInfoFragment("fragment_info_favorites"));

        return binding.getRoot();
    }

    private void showInfoFragment(String layoutName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        int layoutResource;
        switch (layoutName) {
            case "fragment_info_favorites":
                layoutResource = R.layout.fragment_info_favorites;
                break;
            case "fragment_info_history":
                layoutResource = R.layout.fragment_info_history;
                break;
            case "fragment_info_medicine":
                layoutResource = R.layout.fragment_info_medicine;
                break;
            default:
                layoutResource = R.layout.fragment_info_map;
        }
        View dialogView = inflater.inflate(layoutResource, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}