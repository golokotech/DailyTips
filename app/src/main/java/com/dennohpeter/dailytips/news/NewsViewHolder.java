package com.dennohpeter.dailytips.news;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dennohpeter.dailytips.R;

class NewsViewHolder extends RecyclerView.ViewHolder {
    TextView news_heading;

    NewsViewHolder(View itemView) {
        super(itemView);
        news_heading = itemView.findViewById(R.id.news_heading);

    }
}
