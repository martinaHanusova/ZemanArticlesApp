package com.example.android.newsapp;

public class Article {

    private String title;
    private String description;
    private String url;
    private String imageUrl;
    private String publishedDate;

    public Article(String title, String description, String url, String imageUrl, String publishedDate) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.imageUrl = imageUrl;
        this.publishedDate = publishedDate;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    @Override
    public String toString() {
        return "Article{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", publishedDate='" + publishedDate + '\'' +
                '}';
    }
}
