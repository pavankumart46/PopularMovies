package com.blogspot.pavankreddytadi.popularmovies.models;

public class DataModelClass
{
    private String movieTitle;
    private String imageLink;
    private String plotSynopsis;
    private double vote_average;
    private String releaseDate;
    private long id;
    private String backdrop;

    public DataModelClass(String movieTitle, String imageLink, String plotSynopsis, double vote_average, String releaseDate, long id,String backdrop)
    {
        this.movieTitle = movieTitle;
        this.imageLink = imageLink;
        this.plotSynopsis = plotSynopsis;
        this.vote_average = vote_average;
        this.releaseDate = releaseDate;
        this.id = id;
        this.backdrop = backdrop;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public long getId()
    {
        return id;
    }

    public String getMovieTitle()
    {
        return movieTitle;
    }

    public String getImageLink()
    {
        return imageLink;
    }

    public String getPlotSynopsis()
    {
        return plotSynopsis;
    }

    public double getVote_average()
    {
        return vote_average;
    }

    public String getReleaseDate()
    {
        return releaseDate;
    }
}
