package com.example.pharmacy.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.pharmacy.R;
import com.example.pharmacy.databinding.FragmentSignInBinding;
import com.google.firebase.auth.FirebaseAuth;

public class SignInFragment extends Fragment {
    private FragmentSignInBinding binding;
    private FirebaseAuth mAuth;

    public SignInFragment() {
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
        binding = FragmentSignInBinding.inflate(getLayoutInflater(), container, false);

        binding.btnSignUp.setOnClickListener(v -> Navigation
                .findNavController(v)
                .navigate(R.id.action_signInFragment_to_signUpFragment)
        );

        binding.loginButton.setOnClickListener(v -> {
            String email = binding.username.getText().toString().trim();
            String password = binding.password.getText().toString().trim();

            if (!email.isEmpty() && !password.isEmpty()) {
                signInUser(email, password);
            } else {
                Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });

        return binding.getRoot();
    }

    private void signInUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        Navigation.findNavController(requireView())
                                .navigate(R.id.action_signInFragment_to_profileFragment);
                    }else {
                        Toast.makeText(
                                requireContext(),
                                task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
