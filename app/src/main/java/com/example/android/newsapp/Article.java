package com.example.android.newsapp;

import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Article implements Comparable<Article>{

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

    private Date fromISO8601UTC(String dateStr) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(tz);

        try {
            return df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getFormatedDate() {
        Date date = fromISO8601UTC(publishedDate);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM yyyy");
        return dateFormat.format(date);
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


    @Override
    public int compareTo(@NonNull Article o) {
        return o.getPublishedDate().compareTo(getPublishedDate());
    }
}
