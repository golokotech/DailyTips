package com.dennohpeter.dailytips.football;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dennohpeter.dailytips.R;

public class FootballViewHolder extends RecyclerView.ViewHolder{
    public TextView home_team;
    public TextView away_team;
    public TextView start_time;
    public TextView pick;
    public TextView result;
    public TextView odds;
    public ImageView won_or_lost;
    public FootballViewHolder(View itemView) {
        super(itemView);
        home_team = itemView.findViewById(R.id.home_team);
        away_team = itemView.findViewById(R.id.away_team);
        start_time = itemView.findViewById(R.id.start_time);
        pick = itemView.findViewById(R.id.pick);
        result = itemView.findViewById(R.id.result);
        won_or_lost = itemView.findViewById(R.id.match_outcome);
        odds = itemView.findViewById(R.id.odds);
    }
}
