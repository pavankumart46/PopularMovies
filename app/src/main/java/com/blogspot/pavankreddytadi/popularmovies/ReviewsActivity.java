package com.blogspot.pavankreddytadi.popularmovies;

import android.content.DialogInterface;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.pavankreddytadi.popularmovies.adapters.ReviewsDataAdapter;
import com.blogspot.pavankreddytadi.popularmovies.loaders.FetchReviews;
import com.blogspot.pavankreddytadi.popularmovies.models.ReviewDataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.blogspot.pavankreddytadi.popularmovies.MainActivity.LIST_STATE_KEY;

public class ReviewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>
{
    @BindView(R.id.movies_recyclerview)  RecyclerView recyclerView;
    @BindView(R.id.total_reviews)
    TextView textView;
    @BindView(R.id.review_progress)
    ProgressBar progressBar;
    private static final int LOADER_ID_REVIEWS = 2;
    private long id;

    /*
     *All Keys Here
     * */

    private static final String BUNDLE_KEY = "BUNDLE";
    private static final String JSON_KEY = "results";
    private static final String JSON_AUTHOR_KEY = "author";
    private static final String JSON_CONTENT_KEY ="content";
    private Parcelable listState;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        ButterKnife.bind(this);
        mLayoutManager = new LinearLayoutManager(this);
        id = getIntent().getLongExtra(DetailActivity.MOVIE_ID,0);
        setTitle(getIntent().getStringExtra(DetailActivity.TITLE_KEY));
        textView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        if(getSupportLoaderManager().getLoader(LOADER_ID_REVIEWS)!=null)
        {
            getSupportLoaderManager().initLoader(LOADER_ID_REVIEWS,null,this);
        }
        else
        {
            Bundle args = new Bundle();
            args.putLong(BUNDLE_KEY,id);
            getSupportLoaderManager().initLoader(LOADER_ID_REVIEWS,args,this);
        }
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int i, @Nullable Bundle args)
    {
        return new FetchReviews(this,id);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data)
    {
        textView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        try {
            JSONObject root = new JSONObject(data);
            JSONArray results = root.getJSONArray(JSON_KEY);
            List<ReviewDataModel> list = new ArrayList<>();
            if(results.length()==0)
            {
                textView.setVisibility(View.INVISIBLE);
                new AlertDialog.Builder(this)
                        .setTitle("No Reviews Found!")
                        .setMessage("Sorry, There are no reviews for this movie!\nClick Ok to Go Back!")
                        .setIcon(R.drawable.ic_mood_bad_black_24dp)
                        .setCancelable(true)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).show();
            }
            else
            {
                textView.append(String.valueOf(results.length()));
            }
            for(int a=0;a<results.length();a++)
            {
                JSONObject one_review_object = results.getJSONObject(a);
                String author = one_review_object.getString(JSON_AUTHOR_KEY);
                String review = one_review_object.getString(JSON_CONTENT_KEY);
                ReviewDataModel rdm = new ReviewDataModel(author,review);
                list.add(rdm);
            }
            recyclerView.setAdapter(new ReviewsDataAdapter(this,list));
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        // Save list state
        listState = mLayoutManager.onSaveInstanceState();
        state.putParcelable(LIST_STATE_KEY, listState);
    }

    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        // Retrieve list state and list/item positions
        if (state != null)
            listState = state.getParcelable(LIST_STATE_KEY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (listState != null) {
            mLayoutManager.onRestoreInstanceState(listState);
        }
    }
    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }
}
