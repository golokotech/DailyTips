package com.dennohpeter.dailytips.tennis.match_tab;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dennohpeter.dailytips.R;
import com.dennohpeter.dailytips.tennis.TennisModel;

import java.util.ArrayList;

public class MatchFragment extends Fragment {
    private MatchAdapter matchAdapter;
    private RecyclerView recyclerView;
    private LinearLayout nothing_to_show;
    private ArrayList<TennisModel> games;

    public MatchFragment(ArrayList<TennisModel> games) {
        this.games = games;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_match, container, false);
        recyclerView = root.findViewById(R.id.match_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        nothing_to_show = root.findViewById(R.id.no_matches);
        matchAdapter = new MatchAdapter(getContext());

//        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
//        fab.setOnClickListener(v -> {
//            calendarView.setOnDateChangeListener((view, year1, month1, dayOfMonth) -> date = year1 + "-" + month1 + "-" + day);
//        });
        populateRecyclerView(games);
        return root;
    }

    private void populateRecyclerView(ArrayList<TennisModel> games_arr) {
        Log.d("populate", "populateRecyclerView: " + games_arr.size());

        if (games_arr.size() == 0) {
            recyclerView.setVisibility(View.GONE);

            TextView no_events_description = nothing_to_show.findViewById(R.id.no_events_description);
            String description = this.getString(R.string.no_events_caption, "tennis");
            no_events_description.setText(description);

            nothing_to_show.setVisibility(View.VISIBLE);
        } else {
            nothing_to_show.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        matchAdapter.set(games_arr);
        recyclerView.setAdapter(matchAdapter);

    }

}