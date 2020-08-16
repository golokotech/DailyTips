package com.dennohpeter.dailytips.tennis;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dennohpeter.dailytips.R;

public class TennisViewHolder extends RecyclerView.ViewHolder {
    public TextView home_team;

    public TennisViewHolder(View itemView) {
        super(itemView);
        home_team = itemView.findViewById(R.id.home_team);
    }

}
