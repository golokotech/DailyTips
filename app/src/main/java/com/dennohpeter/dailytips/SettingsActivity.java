package com.dennohpeter.dailytips;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

public class SettingsActivity extends AppCompatActivity {

    private SwitchCompat themeSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences preferences = getSharedPreferences("Preferences", MODE_PRIVATE);

        boolean isNightModeOn = preferences.getBoolean("isNightModeOn", false);
        themeSwitch = findViewById(R.id.changeTheme);
        TextView dark_mode_text = findViewById(R.id.dark_mode_text);

        if (isNightModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            themeSwitch.setChecked(true);
            dark_mode_text.setText(getString(R.string.disable_dark_mode));

        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            themeSwitch.setChecked(false);
            dark_mode_text.setText(getString(R.string.enable_dark_mode));

        }

        themeSwitch.setOnClickListener(v -> {
            SharedPreferences.Editor editor = preferences.edit();
            if (themeSwitch.isChecked()) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                editor.putBoolean("isNightModeOn", true);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                editor.putBoolean("isNightModeOn", false);
            }
            editor.apply();
            recreate();
        });

    }
}