package com.example.pharmacy.ui;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.pharmacy.R;
import com.example.pharmacy.databinding.FragmentSignInBinding;

public class SignInFragment extends Fragment {
    private FragmentSignInBinding binding;
    public SignInFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentSignInBinding.inflate(getLayoutInflater());

        binding.btnSignUp.setOnClickListener(v -> Navigation
                .findNavController(v)
                .navigate(R.id.action_signInFragment_to_signUpFragment)
        );
        return binding.getRoot();
    }
}