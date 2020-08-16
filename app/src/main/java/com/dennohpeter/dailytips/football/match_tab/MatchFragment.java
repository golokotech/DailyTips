package com.dennohpeter.dailytips.football.match_tab;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.dennohpeter.dailytips.FetchGamesFromDB;
import com.dennohpeter.dailytips.R;
import com.dennohpeter.dailytips.football.FootballModel;
import com.dennohpeter.dailytips.DataBaseHelper;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MatchFragment extends Fragment implements SearchView.OnQueryTextListener {
    private MatchAdapter footballAdapter;
    private RecyclerView recyclerView;
    private LinearLayout nothing_to_show;
    private SwipeRefreshLayout pullToRefresh;
    private String date;

    private CardView bottom_sheet;
    private CalendarView calendarView;
    private FloatingActionButton fab;
    private TextView date_text;
    private String day;
    private  Button today_btn;
    private BottomSheetBehavior sheetBehavior;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_match, container, false);
        pullToRefresh = root.findViewById(R.id.football_container);
        bottom_sheet = root.findViewById(R.id.bottom_sheet);
        calendarView = root.findViewById(R.id.calendarView);
        date_text = root.findViewById(R.id.date_text);
        fab = root.findViewById(R.id.fab);
        today_btn = root.findViewById(R.id.today);

        recyclerView = root.findViewById(R.id.match_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        setHasOptionsMenu(true);
        nothing_to_show = root.findViewById(R.id.no_matches);

        datePickerWidget();
        FetchGamesFromDB fetchGamesFromDB = new FetchGamesFromDB(getContext(), "football", date, "false");
        ArrayList<FootballModel> games = fetchGamesFromDB.get_timeline_games();

        populateRecyclerView(games);
        fetch_games();
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.dashboard, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint(this.getString(R.string.search_match_hint));
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void populateRecyclerView(ArrayList<FootballModel> games_arr) {
        if (games_arr.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            TextView no_events_description = nothing_to_show.findViewById(R.id.no_events_description);
            String description = this.getString(R.string.no_events_caption, "football");
            no_events_description.setText(description);

            nothing_to_show.setVisibility(View.VISIBLE);
        } else {
            nothing_to_show.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        footballAdapter = new MatchAdapter(getContext(), games_arr);
        recyclerView.setAdapter(footballAdapter);

    }
    private void setSwipeRefreshView() {
        // the refreshing colors
        pullToRefresh.setColorSchemeColors(getResources().
                        getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light)
                , getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light));
        pullToRefresh.setSize(SwipeRefreshLayout.LARGE);

        pullToRefresh.setOnRefreshListener(this::fetch_games);
    }
    private void fetch_games() {
        String url = this.getString(R.string.source_url);
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    // updating game in db
                    new MatchFragment.BackgroundTask().execute("create_or_update_matches", response.toString());
                }, Throwable::printStackTrace);
        request.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) {

            }
        });
        requestQueue.add(request);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Toast.makeText(getContext(), "Query Inserted", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String queryText) {
        footballAdapter.getFilter().filter(queryText.trim());
        return true;
    }

    class  BackgroundTask extends AsyncTask<String, FootballModel, String> {

        @Override
        protected String doInBackground(String... params) {
            String method = params[0];
            DataBaseHelper DataBaseHelper = new DataBaseHelper(getContext());
            String teams, start_time, date, pick, result, won_or_lost, odds;

            if (method.equals("create_or_update_matches")) {
                JSONArray games = null;
                try {
                    games = new JSONArray(params[1]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (games != null) {
                    for (int day = 0; day < games.length(); day++) {
                        try {
                            JSONArray game = games.getJSONArray(day);
                            for (int i = 0; i < game.length(); i++) {
                                try {
                                    JSONObject gameObject = game.getJSONObject(i);
                                    teams = gameObject.getString("teams");
                                    start_time = gameObject.getString("start_time");
                                    date = gameObject.getString("date");
                                    pick = gameObject.getString("pick");
                                    result = gameObject.getString("score");
                                    won_or_lost = gameObject.getString("won_or_lost");
                                    odds = gameObject.getString("odds");
                                    String is_live =  "false";

                                    SQLiteDatabase db = DataBaseHelper.getWritableDatabase();
                                    DataBaseHelper.create_or_update_matches(db, "football", teams, start_time, date, pick, result, won_or_lost, odds, is_live);
                                    db.close();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    return "done updating db";
                } else {
                    return "nothing to refreshGames";
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("done updating db")) {
                Log.d("size", "onPostExecute: "+footballAdapter.size());
//                populateRecyclerView(games);
                pullToRefresh.setRefreshing(false);

            }
        }

    }

}