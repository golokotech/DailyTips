package com.dennohpeter.dailytips.football.match_tab;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.dennohpeter.dailytips.R;
import com.dennohpeter.dailytips.football.FootballModel;
import com.dennohpeter.dailytips.football.FootballViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MatchFragment extends Fragment implements SearchView.OnQueryTextListener {
    DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private LinearLayout nothing_to_show;
    private SwipeRefreshLayout pullToRefresh;
    private String date;
    private CardView bottom_sheet;
    private CalendarView calendarView;
    private FloatingActionButton fab;
    private TextView date_text;
    private String day;
    private Button today_btn;
    private BottomSheetBehavior sheetBehavior;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Football");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.football_container, container, false);
        pullToRefresh = root.findViewById(R.id.football_container);
        bottom_sheet = root.findViewById(R.id.calendarWidget);
        calendarView = root.findViewById(R.id.calendarView);
        date_text = root.findViewById(R.id.date_text);
        fab = root.findViewById(R.id.fab);
        today_btn = root.findViewById(R.id.btn_today);

        recyclerView = root.findViewById(R.id.football_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        nothing_to_show = root.findViewById(R.id.no_matches);

//        datePickerWidget();

        setSwipeRefreshView();

        return root;
    }

    private void datePickerWidget() {

        long current_date = calendarView.getDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(current_date);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        date = format.format(calendar.getTime());

        day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        date_text.setText(day);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        sheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int state) {
                // dragging events
                if (state == BottomSheetBehavior.STATE_EXPANDED) {
                    fab.hide();
                } else {
                    fab.show();
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        fab.setOnClickListener(view -> sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED));
        calendarView.setOnDateChangeListener((view, year1, month1, day) -> {
            Log.d("calendarView", "onSelectedDayChange: " + day + "/" + month1 + "/" + year1);
            sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            date_text.setText(String.valueOf(day));
            fab.show();

        });
        today_btn.setOnClickListener(v -> {
            calendarView.setDate(current_date);
            date_text.setText(day);
            sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            fab.show();
        });
    }

    private void showNothingToShowWidget() {
        TextView no_events_description = nothing_to_show.findViewById(R.id.no_events_description);
        String description = this.getString(R.string.no_events_caption, "football");
        no_events_description.setText(description);
        nothing_to_show.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    private void hideNothingToShowWidget() {
        nothing_to_show.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void setSwipeRefreshView() {
        // the refreshing colors
        pullToRefresh.setColorSchemeColors(getResources().
                        getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light)
                , getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light));
        pullToRefresh.setSize(SwipeRefreshLayout.LARGE);

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Toast.makeText(getContext(), "Query Inserted", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String queryText) {
//        footballAdapter.getFilter().filter(queryText.trim());
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<FootballModel> recyclerOptions = new FirebaseRecyclerOptions.Builder<FootballModel>()
                .setQuery(databaseReference, FootballModel.class)
                .build();

        FirebaseRecyclerAdapter<FootballModel, FootballViewHolder> recyclerAdapter = new FirebaseRecyclerAdapter<FootballModel, FootballViewHolder>(
                recyclerOptions
        ) {
            @Override
            protected void onBindViewHolder(FootballViewHolder holder, int position, FootballModel model) {
                holder.setHomeTeam(model.getHomeTeam());
                holder.setAwayTeam(model.getAwayTeam());
                holder.setOdds(model.getOdds());
                holder.setStartTime(model.getStartTime());
                holder.setResult(model.getResult());
                holder.setWonOrLost(model.getOutcome());
            }

            @NonNull
            @Override
            public FootballViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.football_card, parent, false);
                return new FootballViewHolder(view);
            }
        };
        recyclerAdapter.startListening();
        recyclerView.setAdapter(recyclerAdapter);
    }
}