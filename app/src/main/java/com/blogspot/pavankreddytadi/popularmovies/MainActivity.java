package com.blogspot.pavankreddytadi.popularmovies;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.blogspot.pavankreddytadi.popularmovies.adapters.RecyclerDataAdapter;
import com.blogspot.pavankreddytadi.popularmovies.data.FavouriteMoviesContract;
import com.blogspot.pavankreddytadi.popularmovies.loaders.MoviesFetchLoader;
import com.blogspot.pavankreddytadi.popularmovies.models.DataModelClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>
{
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    private List<DataModelClass> dataModelClasses;
    private List<DataModelClass> list2;
    public final static String LIST_STATE_KEY = "recycler_list_state";
    RecyclerView.LayoutManager mLayoutManager;
    /*
     * Keys for Performing Sorting of Movies according to POPULAR MOVIES and TOP RATED Criteria.
     * */
    public static final String SORT_ORDER_KEY = "sort_order";
    public static final String POPULAR_MOVIES = "popular_movies";
    public static final String FAV_MOVIES = "fav_movies";
    public static final String HIGHEST_RATED = "top_rated";

    /*
     * Keys required to perform JSON Parsing.
     **/
    private static final String RESULTS = "results";
    private static final String ORIGINAL_TITLE = "original_title";
    private static final String POSTER_PATH = "poster_path";
    private static final String OVERVIEW = "overview";
    private static final String VOTE_AVERAGE = "vote_average";
    private static final String RELEASE_DATE = "release_date";
    private static final String MOVIE_ID = "id";
    private static final String BACKDROP_PATH_KEY = "backdrop_path";

    /*
     * Shared Preferences Constants
     * */
    private static final String SHARED_PREFERENCES_FILE_NAME = "STAUS_OF_CURRENT_SCREEN";
    private static final String SHARED_PREFERENCES_KEY = "FAVOURITE_MOVIES";

    /*
     * Id for Loader
     * */
    private static final int LOADER_ID = 0;

    /*
     * For Managing Screen Rotations
     * */

    private SharedPreferences sharedPreferences;
    private Parcelable mListState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mLayoutManager = new GridLayoutManager(this,numberOfColumns());
        progressBar.setVisibility(View.VISIBLE);
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);
        if (sharedPreferences.getBoolean(SHARED_PREFERENCES_KEY, false)) {
            loadFavoriteMovies();
        } else if (getSupportLoaderManager().getLoader(LOADER_ID) != null && isInternetAvailable()) {
            getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        } else if (isInternetAvailable()) {
            Bundle args = new Bundle();
            args.putString(SORT_ORDER_KEY, POPULAR_MOVIES);
            getSupportLoaderManager().restartLoader(LOADER_ID, args, this);
        } else {
            loadFavoriteMovies();
        }

    }

    private void showInternetUnavaialbeAlert() {
        new AlertDialog.Builder(this).setTitle(R.string.alert_title)
                .setMessage(getResources().getString(R.string.alert_box_message))
                .setPositiveButton(getResources().getString(R.string.alert_pos_button_text), null)
                .setCancelable(true)
                .setIcon(R.drawable.no_internet).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sorting_order_menu, menu);
        return true;
    }

    private boolean isInternetAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        assert manager != null;
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (!isInternetAvailable()) {
            loadFavoriteMovies();
        }
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // You can change this divider to adjust the size of the poster
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2; //to keep the grid aspect
        return nColumns;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.popular_movies:
                if (isInternetAvailable()) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(SHARED_PREFERENCES_KEY, false);
                    editor.apply();
                    progressBar.setVisibility(View.VISIBLE);
                    Bundle args = new Bundle();
                    args.putString(SORT_ORDER_KEY, POPULAR_MOVIES);
                    getSupportLoaderManager().restartLoader(LOADER_ID, args, this);
                } else {
                    showInternetUnavaialbeAlert();
                }
                break;
            case R.id.highest_rated:
                if (isInternetAvailable()) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(SHARED_PREFERENCES_KEY, false);
                    editor.apply();
                    progressBar.setVisibility(View.VISIBLE);
                    Bundle args = new Bundle();
                    args.putString(SORT_ORDER_KEY, HIGHEST_RATED);
                    getSupportLoaderManager().restartLoader(LOADER_ID, args, this);
                } else {
                    showInternetUnavaialbeAlert();
                }
                break;
            case R.id.favourite_movies:
                loadFavoriteMovies();
                break;

        }
        return true;
    }

    private void loadFavoriteMovies() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SHARED_PREFERENCES_KEY, true);
        editor.apply();
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        }
        Cursor cursor = getContentResolver().query(FavouriteMoviesContract.FavouriteMoviesEntry.CONTENT_URI, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            List<DataModelClass> list2 = new ArrayList<>();
            do {
                String mt = cursor.getString(cursor.getColumnIndex(FavouriteMoviesContract.FavouriteMoviesEntry.COLOUMN_MOVIE_TITLE));
                String il = cursor.getString(cursor.getColumnIndex(FavouriteMoviesContract.FavouriteMoviesEntry.COLOUMN_POSTER_LINK));
                String ps = cursor.getString(cursor.getColumnIndex(FavouriteMoviesContract.FavouriteMoviesEntry.COLOUMN_PLOT_SYNOPSIS));
                double ur = cursor.getDouble(cursor.getColumnIndex(FavouriteMoviesContract.FavouriteMoviesEntry.COLOUMN_USER_RATING));
                String rd = cursor.getString(cursor.getColumnIndex(FavouriteMoviesContract.FavouriteMoviesEntry.COLOUMN_RELEASE_DATE));
                long id = cursor.getLong(cursor.getColumnIndex(FavouriteMoviesContract.FavouriteMoviesEntry.COLOUMN_MOVIE_ID));
                String bd = cursor.getString(cursor.getColumnIndex(FavouriteMoviesContract.FavouriteMoviesEntry.COLOUMN_BACKDROP_LINK));
                DataModelClass dataModelClass = new DataModelClass(mt, il, ps, ur, rd, id, bd);
                list2.add(dataModelClass);

            } while (cursor.moveToNext());
            cursor.close();
            int orientation = getResources().getConfiguration().orientation;
            recyclerView.setLayoutManager(mLayoutManager);
            RecyclerDataAdapter recyclerDataAdapter = new RecyclerDataAdapter(this, list2);
            recyclerView.setAdapter(recyclerDataAdapter);
        }
    }


    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        assert args != null;
        return new MoviesFetchLoader(this, args.getString(SORT_ORDER_KEY));
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        progressBar.setVisibility(View.GONE);
        dataModelClasses = new ArrayList<>();
        //parsing and storing the results in List
        try {
            JSONObject root = new JSONObject(data);
            JSONArray results = root.getJSONArray(RESULTS);
            for (int i = 0; i < results.length(); i++) {
                JSONObject one_movie_result = results.getJSONObject(i);
                long id = one_movie_result.getLong(MOVIE_ID);
                String original_title = one_movie_result.getString(ORIGINAL_TITLE);
                String poster_path = one_movie_result.getString(POSTER_PATH);
                String plot_synopsis = one_movie_result.getString(OVERVIEW);
                double vote_average = one_movie_result.getDouble(VOTE_AVERAGE);
                String release_date = one_movie_result.getString(RELEASE_DATE);
                String backdrop_path = one_movie_result.getString(BACKDROP_PATH_KEY);
                DataModelClass dataModelClass = new DataModelClass(original_title, poster_path, plot_synopsis, vote_average, release_date, id, backdrop_path);
                dataModelClasses.add(dataModelClass);
            }
            RecyclerDataAdapter recyclerDataAdapter = new RecyclerDataAdapter(this, dataModelClasses);
            int orientation = this.getResources().getConfiguration().orientation;
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(recyclerDataAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

}