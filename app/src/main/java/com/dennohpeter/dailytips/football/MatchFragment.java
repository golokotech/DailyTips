package com.dennohpeter.dailytips.football;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.dennohpeter.dailytips.DateUtil;
import com.dennohpeter.dailytips.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Calendar;

public class MatchFragment extends Fragment implements SearchView.OnQueryTextListener {
    private static final String TAG = "MatchFragment";
    FirebaseFirestore db;
    private RecyclerView recyclerView;
    private LinearLayout nothing_to_show;
    private SwipeRefreshLayout pullToRefresh;
    private LinearLayout calendarWidget;
    private CalendarView calendarView;
    private FrameLayout datePickerFab;
    private TextView date_text;
    private Button btnToday;
    private Query query;
    private FirestoreRecyclerAdapter<FootballModel, FootballViewHolder> recyclerAdapter;
    private boolean isLive = false;
    private String currentDate;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isLive = getArguments().getBoolean("isLive");
        }
        DateUtil dateUtil = new DateUtil();
        currentDate = dateUtil.getCurrentDate();
        Log.d(TAG, "onCreate: " + currentDate);
        query = FirebaseFirestore.getInstance().collection("football").document(currentDate).collection(currentDate);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.football_container, container, false);

        init(root);
        Log.d(TAG, "onCreate: " + isLive);
        populateRecyclerViewer(currentDate);
        setSwipeRefreshView();
        datePickerWidget();
        return root;
    }

    private void init(View root) {
        pullToRefresh = root.findViewById(R.id.football_container);
        calendarWidget = root.findViewById(R.id.calendarWidget);
        calendarView = root.findViewById(R.id.calendarView);
        date_text = root.findViewById(R.id.dateText);
        datePickerFab = root.findViewById(R.id.datePickerFab);
        btnToday = root.findViewById(R.id.btn_today);

        recyclerView = root.findViewById(R.id.football_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        nothing_to_show = root.findViewById(R.id.no_matches);

        if (isLive) {
            datePickerFab.setVisibility(View.GONE);
        } else {
            datePickerFab.setVisibility(View.VISIBLE);
        }

    }

    private void datePickerWidget() {

        Calendar calendar = Calendar.getInstance();
        String dayOfMonth = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        date_text.setText(dayOfMonth);

        BottomSheetBehavior<LinearLayout> bottomSheetBehavior = BottomSheetBehavior.from(calendarWidget);

        datePickerFab.setOnClickListener(view -> bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED));

        calendarView.setOnDateChangeListener((view, year, month, day) -> {
            Log.d("calendarView", "onSelectedDayChange: " + day + "/" + month + "/" + year);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            date_text.setText(String.valueOf(day));
            datePickerFab.setVisibility(View.VISIBLE);

        });
        btnToday.setOnClickListener(v -> {
            calendarView.setDate(calendar.getTimeInMillis());
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            date_text.setText(dayOfMonth);
            datePickerFab.setVisibility(View.VISIBLE);


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
        TextView no_events_description = nothing_to_show.findViewById(R.id.no_events_description);
        String description = this.getString(R.string.no_events_caption, "football");
        no_events_description.setText(description);
        Log.d(TAG, "showNothingToShowWidget: " + description);
        nothing_to_show.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    private void hideNothingToShowWidget() {
        Log.d(TAG, "hideNothingToShowWidget: " + isLive);
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

    public void populateRecyclerViewer(String matchDate) {
        query = query.whereEqualTo("date", matchDate).whereEqualTo("isLive", isLive).orderBy("startTime");
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
                Log.d(TAG, "populateRecyclerViewer:gd" + getItemCount() + isLive);
                if (getItemCount() == 0) {
                    showNothingToShowWidget();
                } else {
                    hideNothingToShowWidget();
                }
            }
        };
        recyclerView.setAdapter(recyclerAdapter);


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