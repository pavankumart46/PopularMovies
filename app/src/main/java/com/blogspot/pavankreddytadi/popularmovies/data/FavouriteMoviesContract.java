package com.blogspot.pavankreddytadi.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class FavouriteMoviesContract
{
    //This content Provider Must be Accessed.
    public static final String AUTHORITY = "com.blogspot.pavankreddytadi.popularmovies.data.CONTENT_PROVIDER";
    //Every Content Provider Must start with prefix content://
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+AUTHORITY);
    //table to which I need to navigate
    public static final String PATH_FAV_MOVIES = "favourite_movies";


    public static class FavouriteMoviesEntry implements BaseColumns
    {
        //build the content Uri using which the db can be accessed
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAV_MOVIES).build();
        //Table Name
        public static final String TABLE_NAME = "favourite_movies";

        //Coloumns of the table
        public static final String COLOUMN_MOVIE_ID = "movie_id";
        public static final String COLOUMN_MOVIE_TITLE = "movie_title";
        public static final String COLOUMN_POSTER_LINK = "poster_link";
        public static final String COLOUMN_BACKDROP_LINK = "backdrop_link";
        public static final String COLOUMN_USER_RATING = "user_rating";
        public static final String COLOUMN_RELEASE_DATE = "release_date";
        public static final String COLOUMN_PLOT_SYNOPSIS = "plot_synopsis";
    }
}
