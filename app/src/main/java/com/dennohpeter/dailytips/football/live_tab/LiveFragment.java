package com.dennohpeter.dailytips.football.live_tab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dennohpeter.dailytips.FetchGamesFromDB;
import com.dennohpeter.dailytips.R;
import com.dennohpeter.dailytips.football.FootballModel;

import java.util.ArrayList;


public class LiveFragment extends Fragment {
    private RecyclerView recyclerView;
    private LiveAdapter liveAdapter;
    private LinearLayout nothing_to_show;
    private String date;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root  = inflater.inflate(R.layout.fragment_live, container, false);
        recyclerView = root.findViewById(R.id.live_container);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        nothing_to_show = root.findViewById(R.id.no_matches);
        liveAdapter = new LiveAdapter();

        FetchGamesFromDB fetchGamesFromDB = new FetchGamesFromDB(getContext(), "football", date, "true");
        ArrayList<FootballModel> games = fetchGamesFromDB.get_timeline_games();
        populateRecyclerView(games);
        return root;

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

        liveAdapter.addAll(games_arr);
        recyclerView.setAdapter(liveAdapter);

    }

}
