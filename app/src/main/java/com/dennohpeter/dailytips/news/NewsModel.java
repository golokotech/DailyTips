package com.dennohpeter.dailytips.news;

class NewsModel {
    private String heading;
    private String publishedDate;
    private String coverImage;
    private String content;

    public NewsModel() {
    }

    public NewsModel(String heading, String publishedDate, String coverImage, String content) {
        this.heading = heading;
        this.publishedDate = publishedDate;
        this.coverImage = coverImage;
        this.content = content;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
