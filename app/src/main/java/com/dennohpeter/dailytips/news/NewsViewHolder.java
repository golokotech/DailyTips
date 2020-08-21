package com.dennohpeter.dailytips.news;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dennohpeter.dailytips.R;

class NewsViewHolder extends RecyclerView.ViewHolder {
    private TextView news_heading, news_content;
    private ImageView news_img;
    private TextView first_seen;

    NewsViewHolder(View itemView) {
        super(itemView);
        news_heading = itemView.findViewById(R.id.news_heading);
        news_img = itemView.findViewById(R.id.news_img);
        first_seen = itemView.findViewById(R.id.first_seen);
        news_content = itemView.findViewById(R.id.news_content);

    }

    public void setHeading(String heading) {
        news_heading.setText(heading);
    }

    public void setCoverImage(String heading) {
        news_img.setImageResource(R.drawable.ic_football);
    }

    public void setPublishedDate(String publishedDate) {
        first_seen.setText(publishedDate);
    }

    public void setContent(String content) {
        news_content.setText(content);
    }

}
