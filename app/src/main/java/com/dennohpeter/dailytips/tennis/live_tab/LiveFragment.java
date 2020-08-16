package com.dennohpeter.dailytips.tennis.live_tab;

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


public class LiveFragment extends Fragment {
    private ArrayList<TennisModel> liveModel;
    private RecyclerView recyclerView;
    private LiveAdapter liveAdapter;
    private LinearLayout nothing_to_show;

    public LiveFragment(ArrayList<TennisModel> data) {
        this.liveModel = data;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        liveModel = new ArrayList<>();
        liveModel.add(new TennisModel("Thika united Vs Charil Sugar"));
        liveModel.add(new TennisModel("Macias Vs Shaka"));
        liveModel.add(new TennisModel("Chelsea Vs Man U"));
        liveModel.add(new TennisModel("Gor Mafia Vs Leopard fc"));
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
        populateRecyclerView(liveModel);
        return root;

    }
    private void populateRecyclerView(ArrayList<TennisModel> games_arr) {
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

        liveAdapter.addAll(liveModel);
        recyclerView.setAdapter(liveAdapter);

    }

}
