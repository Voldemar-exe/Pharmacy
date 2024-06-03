package com.example.pharmacy.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.pharmacy.MainActivity;
import com.example.pharmacy.R;
import com.example.pharmacy.databinding.DialogChangeFontBinding;
import com.example.pharmacy.databinding.DialogChangeNameBinding;
import com.example.pharmacy.ui.interfaces.OnNameUpdatedListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class ProfileActionsHandler {
    private final Context context;
    private final FirebaseAuth mAuth;
    private OnNameUpdatedListener onNameUpdatedListener;

    public ProfileActionsHandler(Context context, FirebaseAuth mAuth) {
        this.context = context;
        this.mAuth = mAuth;
    }

    public void setOnNameUpdatedListener(OnNameUpdatedListener listener) {
        this.onNameUpdatedListener = listener;
    }

    public void showChangeNameDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        DialogChangeNameBinding binding =
                DialogChangeNameBinding.inflate(LayoutInflater.from(context));
        dialogBuilder.setView(binding.getRoot());

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        binding.btnSaveName.setOnClickListener(v -> {
            String newName = binding.etNewName.getText().toString();
            if (!newName.isEmpty()) {
                updateUserName(newName.trim());
                alertDialog.dismiss();
            } else {
                showToast("Пожалуйста, введите новое имя");
            }
        });

        binding.btnCancelName.setOnClickListener(v -> alertDialog.dismiss());
    }

    public void showChangeFontSizeDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        DialogChangeFontBinding binding =
                DialogChangeFontBinding.inflate(LayoutInflater.from(context));
        dialogBuilder.setView(binding.getRoot());

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        SharedPreferences sharedPreferences =
                context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        float savedFontScale = sharedPreferences.getFloat("font_scale", 1.0f);
        binding.fontSizeSeekBar.setProgress((int) ((savedFontScale - 0.5f) * 100));
        binding.fontSizeText.setText(String.format("Размер шрифта: %.1f", savedFontScale));

        binding.fontSizeSeekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        float fontScale = (float) progress / 100 + 0.5f;
                        binding.fontSizeText
                                .setText(String.format("Размер шрифта: %.1f", fontScale));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        float fontScale = (float) seekBar.getProgress() / 100 + 0.5f;
                        applyFontScale(fontScale);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putFloat("font_scale", fontScale);
                        editor.apply();
                    }
                });

        binding.btnCancelFont.setOnClickListener(v -> {
            alertDialog.dismiss();
            Snackbar.make(((MainActivity) context).findViewById(R.id.settings_card),
                            "Требуется перезагрузка для обновления",
                            Snackbar.LENGTH_SHORT)
                    .setAction("Перезагрузить", view -> ((MainActivity) context).recreate())
                    .setBackgroundTint(context.getResources().getColor(R.color.white))
                    .setTextColor(context.getResources().getColor(R.color.text_color_description))
                    .setActionTextColor(context.getColor(R.color.black))
                    .show();
        });
    }

    private void applyFontScale(float fontScale) {
        Configuration config = context.getResources().getConfiguration();
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        config.fontScale = fontScale;
        Resources.getSystem().updateConfiguration(config, metrics);
    }

    private void updateUserName(String newName) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            UserProfileChangeRequest profileUpdates =
                    new UserProfileChangeRequest.Builder()
                            .setDisplayName(newName)
                            .build();
            showToast("Обновление имени . . .");
            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(
                            task -> {
                                if (task.isSuccessful()) {
                                    if (onNameUpdatedListener != null) {
                                        onNameUpdatedListener.onNameUpdated(newName);
                                        showToast("Имя пользователя обновлено");
                                    } else {
                                        showToast("Ошибка при обновлении имени пользователя");
                                    }
                                } else {
                                    showToast("Ошибка при обновлении имени пользователя");
                                }
                            });
        }
    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
