package com.example.pharmacy.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.pharmacy.R;
import com.example.pharmacy.databinding.FragmentProfileBinding;
import com.example.pharmacy.utils.UserDataManager;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ProfileFragment extends Fragment {
    private FirebaseAuth mAuth;

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

        binding.username.setText(Objects.requireNonNull(mAuth.getCurrentUser()).getDisplayName());

        binding.btnExit.setOnClickListener(v -> {
            mAuth.signOut();
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_profileFragment_to_signInFragment);
        });
        UserDataManager userManager = UserDataManager.getInstance(requireContext());
        userManager.readFavoritesMedicine();
        userManager.readHistoryMedicine();
        binding.btnHistory.setOnClickListener(v -> Navigation.findNavController(v)
                .navigate(R.id.action_profileFragment_to_historyFragment));
        binding.btnFavorites.setOnClickListener(v -> Navigation.findNavController(v)
                .navigate(R.id.action_profileFragment_to_favoritesFragment));
        return binding.getRoot();
    }
}