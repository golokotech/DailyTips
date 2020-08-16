package com.dennohpeter.dailytips.tennis.live_tab;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dennohpeter.dailytips.R;
import com.dennohpeter.dailytips.football.FootballViewHolder;
import com.dennohpeter.dailytips.tennis.TennisModel;

import java.util.ArrayList;

public class LiveAdapter extends RecyclerView.Adapter<FootballViewHolder> {
    private ArrayList<TennisModel> games = new ArrayList<>();

    void addAll(ArrayList<TennisModel> games) {
        this.games = games;
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
        holder.home_team.setText(games.get(position).getTeams());

    }

    @Override
    public int getItemCount() {
        return games.size();
    }
}
