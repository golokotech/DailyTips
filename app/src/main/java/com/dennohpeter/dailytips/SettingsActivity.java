package com.dennohpeter.dailytips;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class SettingsActivity extends AppCompatActivity {
    SwitchCompat notifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        notifications = findViewById(R.id.notify_switch);

    }

    public void changeTheme(View view) {
        Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();
    }
}