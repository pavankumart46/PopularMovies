package com.blogspot.pavankreddytadi.popularmovies.loaders;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchVideos extends AsyncTaskLoader<String>
{
    private static final String BASE_URL_VIDEOS = "https://api.themoviedb.org/3/movie/";
    private static final String API_KEY = "8889b906e87c45c427250030289f8a2c";
    private long id;

    public FetchVideos(Context context,long id)
    {
        super(context);
        this.id = id;
    }

    @Override
    public String loadInBackground()
    {
        Uri uri = Uri.parse(BASE_URL_VIDEOS+""+id+"/videos?api_key="+API_KEY);
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
        forceLoad();
    }
}
