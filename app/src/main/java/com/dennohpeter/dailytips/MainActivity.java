package com.dennohpeter.dailytips;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.ObservableSnapshotArray;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private LinearLayout nothing_to_show;
    private LinearLayout calendarWidget;
    private CalendarView calendarView;
    private FrameLayout datePickerFab;
    private TextView date_text;
    private Button btnToday;
    private FirestoreRecyclerAdapter<FootballModel, FootballViewHolder> recyclerAdapter;
    private String currentDate;
    private ProgressBar progressBar;
    private InterstitialAd mInterstitialAd;
    private SharedPreferences preferences;
    private TextView wonMatches, lostMatches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        wonMatches = findViewById(R.id.wonMatches);
        lostMatches = findViewById(R.id.lostMatches);
        preferences = getSharedPreferences("Preferences", MODE_PRIVATE);

        applyTheme();

        DateUtil dateUtil = new DateUtil();
        currentDate = dateUtil.getCurrentDate();
        init();
        populateRecyclerViewer(getQuery(currentDate));
        datePickerWidget();
        loadAd();

    }

    private void applyTheme() {
        boolean isNightModeOn = preferences.getBoolean("isNightModeOn", false);
        if (isNightModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void init() {
        calendarWidget = findViewById(R.id.calendarWidget);
        calendarView = findViewById(R.id.calendarView);
        date_text = findViewById(R.id.dateText);
        datePickerFab = findViewById(R.id.datePickerFab);
        btnToday = findViewById(R.id.btn_today);

        recyclerView = findViewById(R.id.football_recyclerView);
        recyclerView.setLayoutManager(getLayoutManager());
        recyclerView.setHasFixedSize(true);
        nothing_to_show = findViewById(R.id.nothing_to_show);
        progressBar = findViewById(R.id.progress_circular);

    }

    private void loadAd() {
        MobileAds.initialize(this, initializationStatus -> {
        });
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });

    }

    private void datePickerWidget() {

        Calendar calendar = Calendar.getInstance();
        String dayOfMonth = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        date_text.setText(dayOfMonth);

        BottomSheetBehavior<LinearLayout> bottomSheetBehavior = BottomSheetBehavior.from(calendarWidget);

        datePickerFab.setOnClickListener(view -> bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED));

        calendar.add(Calendar.DATE, -14);
        calendarView.setMinDate(calendar.getTimeInMillis());

        calendar.add(Calendar.DATE, 15);
        calendarView.setMaxDate(calendar.getTimeInMillis());

        calendarView.setOnDateChangeListener((view, year, month, day) -> {
            Log.d("calendarView", "onSelectedDayChange: " + day + "/" + month + "/" + year);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            date_text.setText(String.valueOf(day));
            datePickerFab.setVisibility(View.VISIBLE);
            String date = String.format(Locale.getDefault(), "%d-%02d-%02d", year, month + 1, day);
            Query query = getQuery(date);
            changeQuery(query);

        });
        btnToday.setOnClickListener(v -> {
            calendarView.setDate(calendar.getTimeInMillis());
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            date_text.setText(dayOfMonth);
            datePickerFab.setVisibility(View.VISIBLE);
            changeQuery(getQuery(currentDate));

        });

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                    case BottomSheetBehavior.STATE_DRAGGING:
                    case BottomSheetBehavior.STATE_SETTLING:
                    case BottomSheetBehavior.STATE_HALF_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        datePickerFab.setVisibility(View.GONE);
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        datePickerFab.setVisibility(View.VISIBLE);
                    }
                    break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    private void showNothingToShowWidget() {
        nothing_to_show.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    private void hideNothingToShowWidget() {
        nothing_to_show.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    public LinearLayoutManager getLayoutManager() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        String orderBy = preferences.getString("orderBy", getString(R.string.start_time_a_z));
        Log.d(TAG, "getLayoutManager: " + orderBy);
        if (orderBy != null) {
            if (orderBy.equals(getString(R.string.start_time_a_z))) {
                layoutManager.setReverseLayout(false);
                layoutManager.setStackFromEnd(false);

            } else if (orderBy.equals(getString(R.string.start_time_z_a))) {
                layoutManager.setReverseLayout(true);
                layoutManager.setStackFromEnd(true);
            }
        }
        return layoutManager;
    }

    public void populateRecyclerViewer(Query query) {
        progressBar.setVisibility(View.VISIBLE);
        FirestoreRecyclerOptions<FootballModel> recyclerOptions = new FirestoreRecyclerOptions
                .Builder<FootballModel>()
                .setQuery(query, FootballModel.class)
                .build();

        recyclerAdapter = new FirestoreRecyclerAdapter<FootballModel, FootballViewHolder>(
                recyclerOptions
        ) {
            @Override
            protected void onBindViewHolder(@NonNull FootballViewHolder holder, int position, @NonNull FootballModel model) {
                holder.setHomeTeam(model.getHomeTeam());
                holder.setAwayTeam(model.getAwayTeam());
                holder.setOdds(model.getOdds());
                holder.setStartTime(model.getStartTime());
                holder.setResult(model.getResult());
                holder.setPickField(model.getPick());
                holder.setMatchStatus(model.getStatus());
            }

            @NonNull
            @Override
            public FootballViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.football_card, parent, false);
                return new FootballViewHolder(view);
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                if (getItemCount() == 0) {
                    showNothingToShowWidget();

                } else {
                    hideNothingToShowWidget();

                }
                updateStats(getSnapshots());
                progressBar.setVisibility(View.GONE);
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d(TAG, "The interstitial wasn't loaded yet.");
                }
            }
        };
        recyclerView.setAdapter(recyclerAdapter);


    }

    private void updateStats(ObservableSnapshotArray<FootballModel> matches) {
        int won = 0;
        int lost = 0;

        for (FootballModel model : matches) {
            if (model.getStatus().equals("won")) {
                won += 1;
            } else if (model.getStatus().equals("lost")) {
                lost += 1;
            }
        }
        wonMatches.setText(String.format(Locale.getDefault(), "W: %d", won));
        lostMatches.setText(String.format(Locale.getDefault(), "L: %d", lost));
    }

    private Query getQuery(String date) {
        return FirebaseFirestore.getInstance().collection("football").document(date).collection(date).orderBy("startTime");
    }

    private void changeQuery(Query query) {
        progressBar.setVisibility(View.VISIBLE);
        FirestoreRecyclerOptions<FootballModel> recyclerOptions = new FirestoreRecyclerOptions
                .Builder<FootballModel>()
                .setQuery(query, FootballModel.class)
                .build();
        recyclerAdapter.updateOptions(recyclerOptions);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        SharedPreferences.Editor editor = preferences.edit();
        Log.d(TAG, "onOptionsItemSelected: " + item.getItemId());
        switch (item.getItemId()) {
            case R.id.ascending:
                editor.putString("orderBy", getString(R.string.start_time_a_z));
                editor.apply();
                Log.d(TAG, "onOptionsItemSelected: ");
                recyclerView.setLayoutManager(getLayoutManager());
                break;
            case R.id.descending:
                editor.putString("orderBy", getString(R.string.start_time_z_a));
                editor.apply();
                recyclerView.setLayoutManager(getLayoutManager());
                break;
            case R.id.action_settings:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        recyclerAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        recyclerAdapter.stopListening();

    }

}
