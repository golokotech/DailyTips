package com.dennohpeter.dailytips;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private SwitchCompat theme_switch;
    private SharedPreferences preferences;
    private Boolean isNightModeOn;
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_football, R.id.nav_news)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        preferences = getSharedPreferences("AppSettingPrefs", 0);
        isNightModeOn = preferences.getBoolean("NightMode", false);

        if (isNightModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        theme_switch = findViewById(R.id.toggle_theme);
        TextView settings = findViewById(R.id.nav_settings);
        settings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });
        RelativeLayout nav_dark_mode = findViewById(R.id.nav_dark_mode);
        nav_dark_mode.setOnClickListener(v -> theme_switch.toggle());
        theme_switch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Toast.makeText(MainActivity.this, "" + isChecked, Toast.LENGTH_SHORT).show();
            changeTheme();
        });

        loadAd();

    }

    private void loadAd() {
        MobileAds.initialize(this, initializationStatus -> {
        });
        InterstitialAd mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            String TAG = "MainActivity";
            Log.d(TAG, "The interstitial wasn't loaded yet.");
        }
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });

    }

    private void changeTheme() {
        SharedPreferences.Editor editor = preferences.edit();
        if (isNightModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            editor.putBoolean("NightMode", false);

        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            editor.putBoolean("NightMode", true);
        }
        editor.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


}
