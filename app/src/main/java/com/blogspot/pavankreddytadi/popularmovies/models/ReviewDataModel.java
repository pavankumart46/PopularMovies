package com.blogspot.pavankreddytadi.popularmovies.models;

public class ReviewDataModel
{
    private String author;
    private String content;

    public ReviewDataModel(String author, String content) {
        this.author = author;
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
