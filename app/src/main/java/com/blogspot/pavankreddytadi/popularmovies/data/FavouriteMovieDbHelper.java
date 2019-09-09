package com.blogspot.pavankreddytadi.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavouriteMovieDbHelper extends SQLiteOpenHelper
{

    private static final String DATABASE_NAME = "my_favourite_movies.db";
    private static final int DATABASE_VERSION = 1;

    public FavouriteMovieDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        final String CREATE_TABLE = "CREATE TABLE "  + FavouriteMoviesContract.FavouriteMoviesEntry.TABLE_NAME + " (" +
                FavouriteMoviesContract.FavouriteMoviesEntry.COLOUMN_MOVIE_ID + " INTEGER PRIMARY KEY, " +
                FavouriteMoviesContract.FavouriteMoviesEntry.COLOUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                FavouriteMoviesContract.FavouriteMoviesEntry.COLOUMN_POSTER_LINK + " TEXT NOT NULL, " +
                FavouriteMoviesContract.FavouriteMoviesEntry.COLOUMN_BACKDROP_LINK + " TEXT NOT NULL, " +
                FavouriteMoviesContract.FavouriteMoviesEntry.COLOUMN_USER_RATING + " DOUBLE NOT NULL, " +
                FavouriteMoviesContract.FavouriteMoviesEntry.COLOUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                FavouriteMoviesContract.FavouriteMoviesEntry.COLOUMN_PLOT_SYNOPSIS  + " TEXT NOT NULL);";
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
          db.execSQL("DROP TABLE IF EXISTS "+ FavouriteMoviesContract.FavouriteMoviesEntry.TABLE_NAME);
          onCreate(db);
    }
}
