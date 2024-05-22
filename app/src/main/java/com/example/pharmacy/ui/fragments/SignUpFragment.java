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
import com.example.pharmacy.databinding.FragmentSignUpBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpFragment extends Fragment {
    private FragmentSignUpBinding binding;
    private FirebaseAuth mAuth;

    public SignUpFragment() {}

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
        binding = FragmentSignUpBinding.inflate(
                getLayoutInflater(),
                container,
                false
        );

        binding.btnBack.setOnClickListener(v -> Navigation
                .findNavController(v)
                .navigate(R.id.action_signUpFragment_to_signInFragment)
        );

        binding.loginButton.setOnClickListener(v -> {
            String username = binding.username.getText().toString().trim();
            String email = binding.email.getText().toString().trim();
            String password = binding.password.getText().toString().trim();
            String confirmPassword = binding.confirmPassword.getText().toString().trim();

            if (!password.equals(confirmPassword)) {
                Toast.makeText(requireContext(),
                        "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }
            if (email.isEmpty() || password.isEmpty() || username.isEmpty()){
                Toast.makeText(requireContext(),
                        "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            String emailRegex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)" +
                    "*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
            Pattern pattern = Pattern.compile(emailRegex);
            Matcher matcher = pattern.matcher(email);
            if (!matcher.matches()) {
                Toast.makeText(requireContext(),
                        "Invalid email format.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.length() < 6){
                Toast.makeText(requireContext(),
                        "Password must be longer than 6", Toast.LENGTH_SHORT).show();
                return;
            }
            registerUser(email, password, username);
        });

        return binding.getRoot();
    }

    private void registerUser(String email, String password, String displayName) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        mAuth.getCurrentUser()
                                .updateProfile(new UserProfileChangeRequest.Builder()
                                        .setDisplayName(displayName)
                                        .build())
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Navigation
                                                .findNavController(requireView())
                                                .navigate(
                                                    R.id.action_signUpFragment_to_profileFragment
                                                );
                                    }
                                });
                    } else {
                        Toast.makeText(
                                requireContext(),
                                task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
