package com.dennohpeter.dailytips;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.dennohpeter.dailytips.football.FootballFragment;
import com.dennohpeter.dailytips.news.NewsFragment;
import com.dennohpeter.dailytips.tennis.TennisFragment;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private String TAG = "MainActivity";
    private String date;
    private SwitchCompat theme_switch;
    private Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        // show football as the default page on first launch
        Log.d(TAG, "onCreate: " + toolbar.getTitle());
        if (savedInstanceState == null) {
            fragment = new FootballFragment();
            setTitle(R.string.football);

            Log.d(TAG, "first launch: "+ fragment.getArguments());
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            navigationView.setCheckedItem(R.id.nav_football);
        }
        Log.d(TAG, "onCreate: " + toolbar.getTitle());
//        theme_switch = findViewById(R.id.toggle_theme);
        TextView settings = findViewById(R.id.nav_settings);
        settings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });
//        RelativeLayout nav_dark_mode = findViewById(R.id.nav_dark_mode_parent);
//        nav_dark_mode.setOnClickListener(v -> theme_switch.toggle());
//        theme_switch.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            Toast.makeText(MainActivity.this, "" + isChecked, Toast.LENGTH_SHORT).show();
//            changeTheme();
//        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id =  item.getItemId();
        if (id == R.id.action_search){
            return true;
        }
       else if (id == R.id.startTime){
            Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();
        }
        else if (id == R.id.about_to_start){
            Toast.makeText(this, "Coming soon too", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_football:
                fragment = new FootballFragment();
                setTitle(R.string.football);
                break;
            case R.id.nav_tennis:
                fragment = new TennisFragment();
                setTitle(R.string.tennis);
                break;
            case R.id.nav_news:
                fragment = new NewsFragment();
                setTitle(R.string.news);
                break;
        }
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
