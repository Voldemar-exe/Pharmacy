package com.example.pharmacy.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.pharmacy.databinding.FragmentProfileBinding;
import com.example.pharmacy.utils.NavigationHandler;
import com.example.pharmacy.utils.ProfileActionsHandler;
import com.example.pharmacy.utils.UserDataManager;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private FirebaseAuth mAuth;
    private ProfileActionsHandler profileActionsHandler;
    private NavigationHandler navigationHandler;

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
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        profileActionsHandler = new ProfileActionsHandler(requireContext(), mAuth);
        navigationHandler = new NavigationHandler(requireParentFragment().requireView());
        binding.username.setText(Objects.requireNonNull(mAuth.getCurrentUser()).getDisplayName());

        binding.btnExit.setOnClickListener(v -> {
            mAuth.signOut();
            navigationHandler.navigateToSignIn();
        });

        UserDataManager userManager = UserDataManager.getInstance(requireContext());
        userManager.readFavoritesMedicine();
        userManager.readHistoryMedicine();

        binding.btnHistory.setOnClickListener(v ->
                navigationHandler.navigateToHistory());
        binding.btnFavorites.setOnClickListener(v ->
                navigationHandler.navigateToFavorites());
        binding.btnChangeName.setOnClickListener(v ->
                profileActionsHandler.showChangeNameDialog());
        binding.btnChangeSize.setOnClickListener(v ->
                profileActionsHandler.showChangeFontSizeDialog());

        return binding.getRoot();
    }
}

