package com.example.pharmacy.utils;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.pharmacy.R;

public class NavigationHandler {
    private final NavController navController;

    public NavigationHandler(View view) {
        this.navController = Navigation.findNavController(view);
    }

    public void navigateToSignIn() {
        navController.navigate(R.id.action_profileFragment_to_signInFragment);
    }

    public void navigateToHistory() {
        navController.navigate(R.id.action_profileFragment_to_historyFragment);
    }

    public void navigateToFavorites() {
        navController.navigate(R.id.action_profileFragment_to_favoritesFragment);
    }
}
