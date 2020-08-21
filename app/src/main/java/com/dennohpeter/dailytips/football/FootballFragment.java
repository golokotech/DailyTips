package com.dennohpeter.dailytips.football;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.dennohpeter.dailytips.Algolia;
import com.dennohpeter.dailytips.DateUtil;
import com.dennohpeter.dailytips.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class FootballFragment extends Fragment implements SearchView.OnQueryTextListener {
    private static final String TAG = "MatchFragment";
    private RecyclerView recyclerView;
    private LinearLayout nothing_to_show;
    private LinearLayout calendarWidget;
    private CalendarView calendarView;
    private FrameLayout datePickerFab;
    private TextView date_text;
    private Button btnToday;
    private FirestoreRecyclerAdapter<FootballModel, FootballViewHolder> recyclerAdapter;
    private String currentDate;
    private SharedPreferences preferences;
    private ProgressBar progressBar;
    private InterstitialAd mInterstitialAd;
    private Index index;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        DateUtil dateUtil = new DateUtil();
        currentDate = dateUtil.getCurrentDate();
        preferences = getContext().getSharedPreferences("OrderBySettings", MODE_PRIVATE);
        loadAd();
        index = new Algolia().getIndex("football");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_football, container, false);

        init(view);
        populateRecyclerViewer(getQuery(currentDate));
        datePickerWidget();
        return view;
    }

    private void init(View root) {
        calendarWidget = root.findViewById(R.id.calendarWidget);
        calendarView = root.findViewById(R.id.calendarView);
        date_text = root.findViewById(R.id.dateText);
        datePickerFab = root.findViewById(R.id.datePickerFab);
        btnToday = root.findViewById(R.id.btn_today);

        recyclerView = root.findViewById(R.id.football_recyclerView);
        recyclerView.setLayoutManager(getLayoutManager());
        recyclerView.setHasFixedSize(true);
        nothing_to_show = root.findViewById(R.id.no_matches);
        progressBar = root.findViewById(R.id.progress_circular);

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

    @Override
    public boolean onQueryTextSubmit(String queryText) {
        searchQuery(queryText.trim());
        return true;
    }

    @Override
    public boolean onQueryTextChange(String queryText) {
        searchQuery(queryText.trim());
        return true;
    }

    private void searchQuery(String queryText) { ;

        CompletionHandler completionHandler = new CompletionHandler() {
            @Override
            public void requestCompleted(JSONObject content, AlgoliaException error) {

                Log.d(TAG, "requestCompleted: " + content.toString());
            }
        };

        // Search for a match
        index.searchAsync(new Query("jimmie"), completionHandler);







//        TODO refactor this method
        Query query;
        if (queryText.isEmpty()) {
            query = getQuery(currentDate);
        } else {
            query = getQuery(currentDate).orderBy("homeTeam").startAt(queryText).endAt(queryText + "\uf8ff");
        }
        changeQuery(query);
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
            case R.id.action_search:
                SearchView searchView = (SearchView) item.getActionView();
                searchView.setQueryHint(this.getString(R.string.search_match_hint));
                searchView.setOnQueryTextListener(this);
                break;
        }
        return super.onOptionsItemSelected(item);
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


    public LinearLayoutManager getLayoutManager() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        String orderBy = preferences.getString("orderBy", getString(R.string.start_time_a_z));
        Log.d(TAG, "getLayoutManager: "+ orderBy);
        if (orderBy.equals(getString(R.string.start_time_a_z))) {
            layoutManager.setReverseLayout(false);
            layoutManager.setStackFromEnd(false);

        } else if (orderBy.equals(getString(R.string.start_time_z_a))) {
            layoutManager.setReverseLayout(true);
            layoutManager.setStackFromEnd(true);
        }
        return layoutManager;
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
    public void onStart() {
        super.onStart();
        recyclerAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        recyclerAdapter.stopListening();

    }

    private void loadAd() {
        MobileAds.initialize(getContext(), initializationStatus -> {
        });
        mInterstitialAd = new InterstitialAd(getContext());
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
}
