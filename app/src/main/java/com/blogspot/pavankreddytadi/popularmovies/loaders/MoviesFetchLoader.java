package com.blogspot.pavankreddytadi.popularmovies.loaders;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.blogspot.pavankreddytadi.popularmovies.MainActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MoviesFetchLoader extends AsyncTaskLoader<String>
{
    private String sort_order;
    private static final String BASE_URL_POPULAR_MOVIES = "http://api.themoviedb.org/3/movie/popular";
    private static final String BASE_URL_TOP_RATED = "http://api.themoviedb.org/3/movie/top_rated";
    private static final String QUERY_PARAMETER = "api_key";
    private static final String API_KEY = "8889b906e87c45c427250030289f8a2c";

    public MoviesFetchLoader(@NonNull Context context, String sort_order)
    {
        super(context);
        this.sort_order = sort_order;
    }

    @Nullable
    @Override
    public String loadInBackground()
    {
        Uri uri;
        if(sort_order.equals(MainActivity.HIGHEST_RATED))
        {
            uri = Uri.parse(BASE_URL_TOP_RATED).buildUpon()
                    .appendQueryParameter(QUERY_PARAMETER,API_KEY)
                    .build();
        }
        else
        {
            uri = Uri.parse(BASE_URL_POPULAR_MOVIES).buildUpon()
                    .appendQueryParameter(QUERY_PARAMETER,API_KEY)
                    .build();
        }
        try {
            URL url = new URL(uri.toString());
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuffer = new StringBuilder();
            String line;
            while((line = bufferedReader.readLine())!=null)
            {
                stringBuffer.append(line);
            }
            return stringBuffer.toString();


        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad(); //to invoke loadInBackground() method
    }
}
