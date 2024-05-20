package com.example.pharmacy;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.pharmacy.databinding.ActivityMainBinding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseAuth mAuth;
    private NavController navController; // Store the NavController

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
        }

        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.MedicineListFragment){
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    navController.navigate(R.id.action_profileFragment_to_MedicineListFragment);
                } else {
                    navController.navigate(R.id.action_signInFragment_to_MedicineListFragment);
                }
                return true;
            }
            if (itemId == R.id.signInFragment) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    navController.navigate(R.id.action_MedicineListFragment_to_profileFragment);
                } else {
                    navController.navigate(R.id.action_MedicineListFragment_to_signInFragment);
                }
                return true;
            }
            return false;
        });
    }
}