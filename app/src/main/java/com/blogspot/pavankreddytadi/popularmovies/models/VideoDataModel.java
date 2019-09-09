package com.blogspot.pavankreddytadi.popularmovies.models;

public class VideoDataModel
{
    private String key,name;

    public VideoDataModel(String key, String name)
    {
        this.key = key;
        this.name = name;
    }

    public String getKey()
    {
        return key;
    }

    public String getName()
    {
        return name;
    }
}
