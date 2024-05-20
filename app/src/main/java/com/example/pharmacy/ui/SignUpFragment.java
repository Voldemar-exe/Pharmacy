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
import com.example.pharmacy.databinding.FragmentSignUpBinding;

public class SignUpFragment extends Fragment {
    private FragmentSignUpBinding binding;
    public SignUpFragment() {
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
        binding = FragmentSignUpBinding.inflate(getLayoutInflater());

        binding.btnBack.setOnClickListener(v -> Navigation
                .findNavController(v)
                .navigate(R.id.action_signUpFragment_to_signInFragment)
        );

        return binding.getRoot();
    }
}