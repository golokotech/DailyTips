package com.dennohpeter.dailytips.football.live_tab;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dennohpeter.dailytips.R;
import com.dennohpeter.dailytips.football.FootballModel;
import com.dennohpeter.dailytips.football.FootballViewHolder;

import java.util.ArrayList;

public class LiveAdapter extends RecyclerView.Adapter<FootballViewHolder> {
    private ArrayList<FootballModel> live_matches = new ArrayList<>();

    void addAll(ArrayList<FootballModel> games) {
        this.live_matches = games;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FootballViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.football_card, parent,false);
        return new FootballViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FootballViewHolder holder, int position) {
        holder.home_team.setText(live_matches.get(position).getTeams());

    }

    @Override
    public int getItemCount() {
        return live_matches.size();
    }
}
