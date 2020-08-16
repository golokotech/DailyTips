package com.dennohpeter.dailytips.tennis.match_tab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dennohpeter.dailytips.R;
import com.dennohpeter.dailytips.tennis.TennisModel;
import com.dennohpeter.dailytips.tennis.TennisViewHolder;

import java.util.ArrayList;

public class MatchAdapter extends RecyclerView.Adapter<TennisViewHolder> {
    private Context context;
    private ArrayList<TennisModel> games;



    MatchAdapter(Context context) {
        this.context = context;
    }

    void set(ArrayList<TennisModel> games) {
        this.games = games;
        notifyDataSetChanged();
    }
    int size(){
        return games.size();
    }

    @NonNull
    @Override
    public TennisViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tennis_card, parent, false);
        return new TennisViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TennisViewHolder holder, int position) {
        String[] teams = games.get(position).getTeams().split("-");
        holder.home_team.setText(teams[0].trim());

    }

    @Override
    public int getItemCount() {
        return games.size();
    }
}
