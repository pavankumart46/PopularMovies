package com.blogspot.pavankreddytadi.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.pavankreddytadi.popularmovies.adapters.VideoDataAdapter;
import com.blogspot.pavankreddytadi.popularmovies.data.FavouriteMoviesContract;
import com.blogspot.pavankreddytadi.popularmovies.loaders.FetchVideos;
import com.blogspot.pavankreddytadi.popularmovies.models.VideoDataModel;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>, View.OnClickListener
{
    
    @BindView(R.id.textView_video)
    TextView textView_video;
    @BindView(R.id.pb_video)
    ProgressBar pb_video;
    @BindView(R.id.fav_tab)
    FloatingActionButton fab;
    @BindView(R.id.video_here_recyclerview)
    RecyclerView video_recyclerview;
    @BindView(R.id.thumbnail)
    ImageView thumbnail;
    @BindView(R.id.detailCoverImage)
    ImageView detail_cover_poster;
    @BindView(R.id.detail_original_title)
    TextView detail_movie_title;
    @BindView(R.id.detail_user_rating)
    TextView detail_user_rating;
    @BindView(R.id.detail_plot_synopsis)
    TextView detail_plot_synopsis;
    @BindView(R.id.detail_release_date)
    TextView detail_release_date;

    //Variables to store values
    private long movie_id;
    private String movie_title;
    private double user_rating;
    private String desc;
    private String imagelink;
    private String release_date;
    private String thumbnail_link;

    //Constant Keys for Details
    public static final String TITLE_KEY ="TITLE";
    public static final String RATING_KEY ="RATING";
    public static final String DESCRIPTION_KEY ="DESC";
    public static final String IMAGE_LINK_KEY ="IMG";
    public static final String RELEASE_DATE_KEY = "RLS_DATE";
    public static final String MOVIE_ID = "MOVIE_ID";
    public static final String BACKDROP_PATH = "backdrop_path";
    //Loader Id
    private static final int LOADER_ID_VIDEOS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*initializeViews();*/
        ButterKnife.bind(this);

        textView_video.setVisibility(View.GONE);
        pb_video.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        movie_title = intent.getStringExtra(TITLE_KEY);
        user_rating = intent.getDoubleExtra(RATING_KEY,0.0);
        desc = intent.getStringExtra(DESCRIPTION_KEY);
        imagelink= intent.getStringExtra(IMAGE_LINK_KEY);
        release_date = intent.getStringExtra(RELEASE_DATE_KEY);
        thumbnail_link =intent.getStringExtra(BACKDROP_PATH);
        Glide.with(this).load("https://image.tmdb.org/t/p/w500"+thumbnail_link).into(thumbnail);
        Glide.with(this).load("http://image.tmdb.org/t/p/w185"+imagelink).into(detail_cover_poster);
        detail_movie_title.append(movie_title);
        detail_user_rating.append(String.valueOf(user_rating));
        detail_plot_synopsis.append(desc);
        detail_release_date.append(release_date);
        setTitle(movie_title);
        movie_id = getIntent().getLongExtra(MOVIE_ID,0);

        boolean status = checkIfMovieIsInDatabase(movie_id);
        if(status)
        {
            fab.setImageResource(R.drawable.ic_star_black_24dp);
        }
        else
        {
            fab.setImageResource(R.drawable.ic_star_border_black_24dp);
        }
        fab.setOnClickListener(this);

        if(getSupportLoaderManager().getLoader(1)!=null)
        {
            getSupportLoaderManager().initLoader(LOADER_ID_VIDEOS,null,this);
        }
        else if(isInternetAvailable())
        {
            Bundle args = new Bundle();
            args.putLong("BUNDLE",movie_id);
            getSupportLoaderManager().initLoader(LOADER_ID_VIDEOS,args,this);
        }
        else
        {
            pb_video.setVisibility(View.GONE);
        }
    }

    private boolean checkIfMovieIsInDatabase(long movie_id)
    {
        Cursor cursor = getContentResolver().query(FavouriteMoviesContract.FavouriteMoviesEntry.CONTENT_URI,null,null,null,null);
        if(cursor.moveToFirst() && cursor!=null)
        {
            do{
                if(movie_id == cursor.getLong(cursor.getColumnIndex(FavouriteMoviesContract.FavouriteMoviesEntry.COLOUMN_MOVIE_ID)))
                {
                    return true;
                }
            }while(cursor.moveToNext());
        }
        cursor.close();
        return false;
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        if(id!=0)
        {
            return new FetchVideos(this,movie_id);
        }
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data)
    {
        textView_video.setVisibility(View.VISIBLE);
        pb_video.setVisibility(View.GONE);

        try {
            JSONObject root = new JSONObject(data);
            JSONArray results = root.getJSONArray("results");
            List<VideoDataModel> list = new ArrayList<>();
            for(int i=0;i<results.length();i++)
            {
                JSONObject result_item = results.getJSONObject(i);
                String key = result_item.getString("key");
                String title = result_item.getString("name");
                VideoDataModel vdm = new VideoDataModel(key,title);
                list.add(vdm);
            }
            VideoDataAdapter vda = new VideoDataAdapter(this,list);
            int orientation = this.getResources().getConfiguration().orientation;
            if(orientation == Configuration.ORIENTATION_LANDSCAPE)
            {
                video_recyclerview.setLayoutManager(new GridLayoutManager(this,3));
            }
            else
            {
                video_recyclerview.setLayoutManager(new GridLayoutManager(this,2));
            }
            video_recyclerview.setAdapter(vda);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean isInternetAvailable()
    {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        assert manager != null;
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return networkInfo!=null && networkInfo.isConnected();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

    public void readReviews(View view)
    {
        if(isInternetAvailable())
        {
            Intent intent = new Intent(this,ReviewsActivity.class);
            intent.putExtra(MOVIE_ID,movie_id);
            intent.putExtra(TITLE_KEY,movie_title);
            startActivity(intent);
        }
        else
        {
            Snackbar.make(view,"YOU CANNOT PROCEED! NO INTERNET!",Snackbar.LENGTH_LONG).show();
        }

    }

    @Override
    public void onClick(View v)
    {
        if(checkIfMovieIsInDatabase(movie_id))
        {
            int results = getContentResolver().delete(FavouriteMoviesContract.FavouriteMoviesEntry.CONTENT_URI,
                    FavouriteMoviesContract.FavouriteMoviesEntry.COLOUMN_MOVIE_ID+"="+movie_id,null);
            if(results>0)
            {
                fab.setImageResource(R.drawable.ic_star_border_black_24dp);
                Snackbar.make(v,"Movie Removed From Favorite Movies",Snackbar.LENGTH_LONG).show();
            }
        }
        else
        {
            ContentValues values = new ContentValues();
            values.put(FavouriteMoviesContract.FavouriteMoviesEntry.COLOUMN_MOVIE_ID,movie_id);
            values.put(FavouriteMoviesContract.FavouriteMoviesEntry.COLOUMN_MOVIE_TITLE,movie_title);
            values.put(FavouriteMoviesContract.FavouriteMoviesEntry.COLOUMN_POSTER_LINK,imagelink);
            values.put(FavouriteMoviesContract.FavouriteMoviesEntry.COLOUMN_BACKDROP_LINK,thumbnail_link);
            values.put(FavouriteMoviesContract.FavouriteMoviesEntry.COLOUMN_USER_RATING,user_rating);
            values.put(FavouriteMoviesContract.FavouriteMoviesEntry.COLOUMN_RELEASE_DATE,release_date);
            values.put(FavouriteMoviesContract.FavouriteMoviesEntry.COLOUMN_PLOT_SYNOPSIS,desc);

            Uri result = getContentResolver().insert(FavouriteMoviesContract.FavouriteMoviesEntry.CONTENT_URI,values);
            if(result!=null)
            {
                fab.setImageResource(R.drawable.ic_star_black_24dp);
                Snackbar.make(v,"Movie Added to Favorites",Snackbar.LENGTH_LONG).show();
            }
        }
    }

}
