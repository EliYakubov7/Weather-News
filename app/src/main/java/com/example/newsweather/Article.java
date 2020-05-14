package com.example.newsweather;
import java.io.Serializable;

class Article implements Serializable {
    private String sourceName;
    private String title;
    private String author;
    private String description;
    private String url;
    private String imageUrl;
    private String date_time;

    Article(String sourceName, String title, String author, String description, String url,
            String imageUrl, String date_time)
    {
        this.sourceName = sourceName;
        this.title = title;
        this.author = author;
        this.description = description;
        this.url = url;
        this.imageUrl = imageUrl;
        this.date_time = date_time;

    }

    String getSourceName() {
        return sourceName;
    }

    String getTitle() {
        return title;
    }

    String getAuthor() {
        return author;
    }

    String getDescription() {
        return description;
    }

    String getUrl() {
        return url;
    }

    String getImageUrl() {
        return imageUrl;
    }

    String getDateTime() {
        return date_time;
    }
}
