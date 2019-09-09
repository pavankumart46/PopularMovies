package com.blogspot.pavankreddytadi.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class FavouriteMoviesContentProvider extends ContentProvider
{
    public static final int FAV_MOVIES = 100;
    public static final int FAV_MOVIES_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher()
    {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(FavouriteMoviesContract.AUTHORITY,FavouriteMoviesContract.PATH_FAV_MOVIES,FAV_MOVIES);
        uriMatcher.addURI(FavouriteMoviesContract.AUTHORITY,FavouriteMoviesContract.PATH_FAV_MOVIES+"/#",FAV_MOVIES);
        return uriMatcher;
    }

    FavouriteMovieDbHelper mFavoriteMovieDbHelper;

    public FavouriteMoviesContentProvider()
    {

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        // Implement this to handle requests to delete one or more rows.
        SQLiteDatabase sqLiteDatabase = mFavoriteMovieDbHelper.getWritableDatabase();
        return sqLiteDatabase.delete(FavouriteMoviesContract.FavouriteMoviesEntry.TABLE_NAME,selection,null);
    }

    @Override
    public String getType(Uri uri)
    {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values)
    {
        // TODO: Implement this to handle requests to insert a new row.
        final SQLiteDatabase mSQLiteDatabase = mFavoriteMovieDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match)
        {
            case FAV_MOVIES:
                long id = mSQLiteDatabase.insert(FavouriteMoviesContract.FavouriteMoviesEntry.TABLE_NAME,null,values);
                if(id>0)
                {
                    returnUri = ContentUris.withAppendedId(FavouriteMoviesContract.FavouriteMoviesEntry.CONTENT_URI,id);
                }
                else
                {
                    throw new android.database.SQLException("Failed to Insert into Row "+uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }

        return returnUri;

    }

    @Override
    public boolean onCreate()
    {
        // TODO: Implement this to initialize your content provider on startup.
        Context context = getContext();
        mFavoriteMovieDbHelper = new FavouriteMovieDbHelper(context);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder)
    {
        // TODO: Implement this to handle query requests from clients.
        SQLiteDatabase sqLiteDatabase = mFavoriteMovieDbHelper.getReadableDatabase();
        return sqLiteDatabase.query(FavouriteMoviesContract.FavouriteMoviesEntry.TABLE_NAME,
                        projection,selection,selectionArgs,
                        null,null,sortOrder);

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs)
    {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
