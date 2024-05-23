package com.example.pharmacy.ui.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.pharmacy.R;
import com.example.pharmacy.databinding.FragmentInfoBinding;

public class InfoFragment extends Fragment {
    private FragmentInfoBinding binding;

    public InfoFragment() {}

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
        binding = FragmentInfoBinding.inflate(getLayoutInflater());

        binding.btnAboutAuthor.setOnClickListener(view -> showAboutAuthorDialog());

        return binding.getRoot();
    }

    private void showAboutAuthorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.author_info_dialog, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}