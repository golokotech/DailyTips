package com.dennohpeter.dailytips.football;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dennohpeter.dailytips.R;

public class FootballViewHolder extends RecyclerView.ViewHolder {
    private TextView home_team;
    private TextView away_team;
    private TextView start_time;
    private TextView pickField;
    private TextView resultField;
    private TextView oddsField;
    private ImageView matchOutcome;

    public FootballViewHolder(View itemView) {
        super(itemView);
        home_team = itemView.findViewById(R.id.home_team);
        away_team = itemView.findViewById(R.id.away_team);
        start_time = itemView.findViewById(R.id.start_time);
        pickField = itemView.findViewById(R.id.pick);
        resultField = itemView.findViewById(R.id.result);
        matchOutcome = itemView.findViewById(R.id.match_outcome);
        oddsField = itemView.findViewById(R.id.odds);
    }

    public void setHomeTeam(String homeTeam) {
        home_team.setText(homeTeam);
    }

    public void setAwayTeam(String awayTeam) {
        away_team.setText(awayTeam);
    }

    public void setStartTime(String startTime) {
        start_time.setText(startTime);
    }

    public void setPickField(String pick) {
        pickField.setText(pick);
    }

    public void setResult(String result) {
        resultField.setText(result);
    }

    public void setOdds(Float odds) {
        oddsField.setText(String.valueOf(odds));
    }

    public void setMatchStatus(String status) {
        if (status.equals("won")) {
            matchOutcome.setImageResource(R.drawable.won);
        } else if (status.equals("lost")) {
            matchOutcome.setImageResource(R.drawable.lost);
        } else {
            matchOutcome.setImageResource(R.drawable.waiting);
        }
    }
}
