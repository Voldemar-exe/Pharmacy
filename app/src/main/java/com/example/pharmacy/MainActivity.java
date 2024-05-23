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
    private NavController navController;

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
                navController.navigate(R.id.MedicineListFragment);
                return true;
            }
            if (itemId == R.id.signInFragment) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    navController.navigate(R.id.profileFragment);
                } else {
                    navController.navigate(R.id.signInFragment);
                }
                return true;
            }
            if (itemId == R.id.infoFragment){
                navController.navigate(R.id.infoFragment);
                return true;
            }
            return false;
        });
    }
}