package com.dennohpeter.dailytips.news;

class NewsModel {
    private String news_heading;

    NewsModel(String heading) {
        this.news_heading = heading;
    }

    String getHeading() {
        return news_heading;
    }
}
